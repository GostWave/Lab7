package server.commands;

import common.Response;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;

public class Register extends Command {
    String login;
    String pass;

    @Override
    public Response execute(String strArg, Serializable objArg) {
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
            if (Server.getServer().getUserDAO().register(login, pass) != null) {
                return new Response("Пользователь успешно зарегистрирован.");
            } else {
                return new Response("Не удалось зарегистрировать пользователя. Возможно, пользователь с таким именем уже существует.");
            }
        } catch (SQLException e) {
            return new Response("Произошла ошибка при регистрации: ");
        }

    }

    @Override
    public String getDescription() {
        return "регистрация нового пользователя";
    }
}
