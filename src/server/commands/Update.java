//package server.commands;
//
//import common.Response;
//
//import common.collectionObject.Movie;
//import server.CollectionManager;
//import server.Server;
//
//import java.io.Serializable;
//
//
//public class Update extends Command {
//    @Override
//    public Response execute(String strArg, Serializable objArg) {
//        try {
//            CollectionManager collectionManager = Server.getServer().getCollectionManager();
//            if (collectionManager.findMovieById(Long.parseLong(strArg)) == null) {
//                return new Response("В коллекции не содержится элемента с таким индексом");
//            } else {
//                collectionManager.updateElement(Long.parseLong(strArg), collectionManager.findMovieById(Long.parseLong(strArg)), (Movie) objArg);
//                return new Response("Элемент успешно обновлён");
//            }
//        } catch (NumberFormatException e) {
//            return new Response("Введён неверный аргумент команды");
//        }
//    }
//
//
//    @Override
//    public String getDescription() {
//        return "обновить значение элемента коллекции, id которого равен заданному";
//    }
//}

package server.commands;


import common.Response;
import common.collectionObject.Movie;
import server.CollectionManager;
import server.DB.MovieDBProvider;
import server.DB.UserDBProvider;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;

public class Update extends Command {


    @Override
    public Response execute(String stringArg, Serializable objectArg, Integer userId) {
        long id;
        CollectionManager collectionManager = Server.getServer().getCollectionManager();
        MovieDBProvider movieDBProvider = Server.getServer().getMovieDBProvider();
        try {
            id = Long.parseLong(stringArg);
        } catch (NumberFormatException e) {
            return new Response("ID должен быть числом.");
        }

        if (!(objectArg instanceof Movie)) {
            return new Response("Объект должен быть экземпляром Movie.");
        }

        Movie newMovie = (Movie) objectArg;
        newMovie.setOwnerId(userId);

        try {

            if (!movieDBProvider.checkOwnership(id, userId)) {
                return new Response("Вы не можете изменить чужой фильм или фильм не найден.");
            }


            if (!movieDBProvider.updateMovieById(id, newMovie, userId)) {
                return new Response("Ошибка при обновлении фильма в базе данных.");
            }


            collectionManager.updateMovieById(id, newMovie);

            return new Response("Фильм с id = " + id + " успешно обновлён.");

        } catch (SQLException e) {
            e.printStackTrace();
            return new Response("SQL-ошибка при обновлении фильма.");
        }
    }

    @Override
    public String getDescription() {
        return "Обновляет фильм по его id. Синтаксис: update_by_id <id>";
    }
}
