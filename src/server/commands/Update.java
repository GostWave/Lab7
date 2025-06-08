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
import server.MovieDAO;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;

public class Update extends Command {
    private final CollectionManager collectionManager = Server.getServer().getCollectionManager();
    private final MovieDAO movieDAO = Server.getServer().getMovieDAO();



    @Override
    public Response execute(String stringArg, Serializable objectArg, Integer userId) {
        if (stringArg == null || stringArg.isEmpty()) {
            return new Response("Не указан ID для обновления.");
        }

        long id;
        try {
            id = Long.parseLong(stringArg);
        } catch (NumberFormatException e) {
            return new Response("ID должен быть числом.");
        }

        if (!(objectArg instanceof Movie)) {
            return new Response("Объект должен быть экземпляром Movie.");
        }

        Movie newMovie = (Movie) objectArg;

        try {
            // 1. Проверка принадлежности
            if (!movieDAO.checkOwnership(id, userId)) {
                return new Response("Вы не можете изменить чужой фильм или фильм не найден.");
            }

            // 2. Обновление в БД
            boolean updatedInDb = movieDAO.updateMovieById(id, newMovie, userId);
            if (!updatedInDb) {
                return new Response("Ошибка при обновлении фильма в базе данных.");
            }

            // 3. Обновление в памяти
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
