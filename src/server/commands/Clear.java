package server.commands;

import server.DB.MovieDBProvider;
import common.Response;

import server.CollectionManager;
import server.Server;

import java.io.Serializable;
import java.sql.SQLException;

/**
 * Класс команды Clear, предназначенный для очистки коллекции.
 */
public class Clear extends Command {

    /**
     * Выполняет команду очистки коллекции.
     */
    @Override
    public Response execute(String strArg, Serializable objArg, Integer userId) {
        CollectionManager collectionManager = Server.getServer().getCollectionManager();
        MovieDBProvider movieDBProvider = Server.getServer().getMovieDAO();
        if (collectionManager.getCollection().isEmpty()) {
            return new Response("Коллекция не содержит элементов, которые можно было бы очистить");
        } else {
            collectionManager.clearMoviesByUser(userId);
            try {
                movieDBProvider.clearMoviesByUser(userId);
            } catch (SQLException e) {
                return new Response("Произошла ошибка при очистке коллекции" );
            }

            return new Response("Коллекция успешно очищена");
        }
    }

    /**
     * Возвращает описание команды.
     *
     * @return строка с описанием команды
     */
    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }
}
