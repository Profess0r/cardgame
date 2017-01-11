package net.gutsoft.cardgame.controller;

import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvatarImagesController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> imageIndexes = new ArrayList<>();

        // удастся ли таким образом получить getRealPath на хостинге?
        int count = new File(req.getServletContext().getRealPath("") + "/images/avatars").listFiles().length;

        for (int i = 0; i < count; i++) {
            imageIndexes.add("" + i);
        }

        req.setAttribute("imageIndexes", imageIndexes);
        req.getRequestDispatcher("avatarImages.jsp").forward(req, resp);
    }
}
