package net.gutsoft.cardgame.util;

import net.gutsoft.cardgame.entity.Battle;

import java.util.Map;

public class BattleGarbageCollector {

    static boolean finishApplication = false;
    private static final long MAX_LIFETIME = 3 * 60 * 60 * 1000; // 3 hours

    static final Thread collectorThread = new Thread() {
        @Override
        public synchronized void run() {
            while (!finishApplication) {
                try {
                    this.wait(5 * 60 * 60 * 1000); // 5 hours
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkBattleMap();
            }
        }
    };;

    private static void checkBattleMap() {
        Map<Integer, Battle> battleMap = (Map<Integer, Battle>) ServletContextHolder.getServletContext().getAttribute("battles"); //возможно, не нужно извлекать battleMap каждый раз

        long currentTime = System.currentTimeMillis();

        for (Battle battle: battleMap.values()) {
            long lifetime = currentTime - battle.getCreateTime();
            if (lifetime > MAX_LIFETIME || finishApplication) {
                battle.forcedEndBattle();
                battleMap.remove(battle.getId());
                System.out.println("deleted " + battle);
            }
        }
    }

    public static void start() {
        collectorThread.start();
    }

    public static void stop() {
        finishApplication = true;
        synchronized (collectorThread) {
            collectorThread.notify();
        }
    }
}
