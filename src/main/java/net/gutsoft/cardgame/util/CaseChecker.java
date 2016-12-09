package net.gutsoft.cardgame.util;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.hibernate.DataBaseManager;

import java.util.List;

public class CaseChecker {
    public static void main(String[] args) {

        Account account = new Account();
        List<Account> accounts = DataBaseManager.getEntityListByParameter(account, "money", 500);
        System.out.println(accounts);
    }
}
