package server.commands;

import server.DB.MovieDBProvider;
import common.Response;
import server.CollectionManager;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;

public class Remove extends Command {


    @Override
    public Response execute(String strArg, Serializable objArg, Integer userId) {
        MovieDBProvider movieDBProvider = Server.getServer().getMovieDAO();
        CollectionManager collectionManager = Server.getServer().getCollectionManager();
        long movieId = Long.parseLong(strArg);
        try {
            if (!movieDBProvider.checkOwnership(movieId, userId)) {
                return new Response("Ошибка: фильм не принадлежит вам или не существует.");
            }

            movieDBProvider.deleteMovieById(movieId);
            collectionManager.removeMovieById(movieId);

            return new Response("Фильм успешно удалён.");
        } catch (SQLException e) {
            return new Response("Ошибка при удалении фильма из базы данных.");
        }
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }
}
