//package server.commands;
//
//import common.Response;
//import server.CollectionManager;
//import server.Server;
//
//import java.io.Serializable;
//
///**
// * Класс команды Shuffle, предназначенный для перемешивания элементов коллекции в случайном порядке.
// */
//public class Shuffle extends Command {
//
//    /**
//     * Выполняет команду перемешивания элементов коллекции.
//     */
//    @Override
//    public Response execute(String strArg, Serializable objArg) {
//        CollectionManager collectionManager = Server.getServer().getCollectionManager();
//        if (!collectionManager.getCollection().isEmpty()) {
//            collectionManager.shuffCollection();
//            return new Response("Коллекция успешно перемешана");
//        } else {
//            return new Response("Коллекция пуста, её нельзя перемешать");
//        }
//    }
//
//    /**
//     * Возвращает описание команды.
//     *
//     * @return строка с описанием команды
//     */
//    @Override
//    public String getDescription() {
//        return "перемешать элементы коллекции в случайном порядке";
//    }
//}

package server.commands;

import common.collectionObject.Movie;
import server.CollectionManager;
import common.Response;
import server.Server;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Shuffle extends Command {

    private CollectionManager collectionManager  = Server.getServer().getCollectionManager();



    @Override
    public Response execute(String strArg, Serializable objArg, Integer userId)  {
        List<Movie> movies = new ArrayList<>(collectionManager.getCollection());

        if (movies.isEmpty()) {
            return new Response("Коллекция пуста, нечего перемешивать.");
        }

        Collections.shuffle(movies);


        collectionManager.clearCollection();
        for (Movie movie : movies) {
            collectionManager.addMovie(movie);
        }

        return new Response("Коллекция успешно перемешана.");
    }

    @Override
    public String getDescription() {
        return "Перемешивает элементы коллекции в случайном порядке.";
    }
}
