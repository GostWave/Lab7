package server.commands;

import common.Response;
import common.collectionObject.Movie;

import server.CollectionManager;
import server.DB.MovieDBProvider;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Insert extends Command {


    @Override
    public Response execute(String strArg, Serializable objArg, Integer userId) {
        CollectionManager collectionManager = Server.getServer().getCollectionManager();
        MovieDBProvider movieDBProvider = Server.getServer().getMovieDBProvider();
        if (strArg == null || strArg.isEmpty()) {
            return new Response("Укажите индекс вставки.");
        }

        Long index;
        try {
            index = Long.parseLong(strArg);
        } catch (NumberFormatException e) {
            return new Response("Индекс должен быть числом.");
        }

        Movie newMovie = (Movie) objArg;
        List<Long> ids = collectionManager.getAllIds();

        if (index < 0 || index > Collections.max(ids)) {
            return new Response("Индекс вне допустимого диапазона.");
        }

        if (ids.contains(index)){
            return new Response("Элемент не будет добавлен в коллекцию, так как в ней уже содержится элемент с таки id.");
        } else {
            newMovie.setId(index);
            newMovie.setOwnerId(userId);
            collectionManager.addMovie(newMovie);
            try {
                movieDBProvider.addMovieWithId(newMovie,userId,index);
            } catch (SQLException e) {
                return new Response("Не удалось добавить элемент в базу данных.");
            }

            return new Response("Элемент успешно добавлен в коллекцию.");
        }


    }


    @Override
    public String getDescription() {
        return "добавить новый элемент в заданную позицию";
    }
}
