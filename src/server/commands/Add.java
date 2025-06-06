package server.commands;

import common.MovieDAO;
import common.Response;
import common.collectionObject.Movie;
import server.CollectionManager;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;

public class Add extends Command {


    @Override
    public Response execute(String strArg, Serializable objArg)  {
        CollectionManager collectionManager = Server.getServer().getCollectionManager();
        Movie movie = (Movie) objArg;


        MovieDAO movieDAO = Server.getServer().getMovieDAO();
        try {
            collectionManager.addMovie(movieDAO.addMovie(movie,1));
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
