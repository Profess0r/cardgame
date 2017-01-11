package net.gutsoft.cardgame.controller.account;

import net.gutsoft.cardgame.entity.Account;
import net.gutsoft.cardgame.entity.Card;
import net.gutsoft.cardgame.entity.Deck;
import net.gutsoft.cardgame.hibernate.DataBaseManager;
import net.gutsoft.cardgame.inject.DependencyInjectionServlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//@WebServlet("/upload")
//@MultipartConfig
public class SetAvatarController extends DependencyInjectionServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

        int newAvatarImage = Integer.parseInt(req.getParameter("id"));
        Account account = (Account) req.getSession().getAttribute("account");

        if (account.getId() != newAvatarImage) {
            //set new image
            account.setAvatar(newAvatarImage);
            account = DataBaseManager.updateEntity(account);
            req.getSession().setAttribute("account", account);
            req.getRequestDispatcher("avatar.jsp").forward(req, resp);
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String description = req.getParameter("description"); // Retrieves <input type="text" name="description">
        Part filePart = req.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
        InputStream fileContent = filePart.getInputStream();

        File uploads = new File("d:/tmp");

        File file = new File(uploads, "image.png");
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, file.toPath());
        }
    }
}
