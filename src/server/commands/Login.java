package server.commands;

import common.Response;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;


public class Login extends Command{
    String login;
    String pass;
    @Override
    public Response execute(String strArg, Serializable objArg, Integer userId) {
        try {
            login = strArg.split(" ")[0];
            pass = strArg.split(" ")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Response("Неверный формат ввода.");
        }

        if (login == null || login.isEmpty()) {
            return new Response("Имя пользователя не может быть пустым.");
        }
        if (pass == null || pass.isEmpty()) {
            return new Response("Пароль не может быть пустым.");
        }
        try {
            if (Server.getServer().getUserDAO().authenticate(login, pass)!=null) {
                return new Response("Успешный вход в систему.");
            } else {
                return new Response("Неверное имя пользователя или пароль.");
            }
        } catch (SQLException e) {
            return new Response("Произошла ошибка при входе в систему: ");
        }

    }

    @Override
    public String getDescription() {
        return "вход в систему для зарегистрированного пользователя";
    }
}
