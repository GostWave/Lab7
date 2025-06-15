package server.commands;

import server.DB.MovieDBProvider;
import common.Response;
import common.collectionObject.Movie;
import server.CollectionManager;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;

public class Add extends Command {


    @Override
    public Response execute(String strArg, Serializable objArg, Integer userId)  {
        CollectionManager collectionManager = Server.getServer().getCollectionManager();
        Movie movie = (Movie) objArg;
        movie.setOwnerId(userId);

        MovieDBProvider movieDBProvider = Server.getServer().getMovieDBProvider();
        try {
            collectionManager.addMovie(movieDBProvider.addMovie(movie,userId));
        } catch (SQLException e) {
            return new Response("Ошибка при добавлении элемента в базу данных: " + e.getMessage());
        }

        return new Response("Элемент успешно добавлен в коллекцию");
    }


    @Override
    public String getDescription() {
        return "добавить новый элемент в коллекцию";
    }
}
