package server;

//import server.IO.FileManager;
import server.DB.DatabaseManager;
import server.DB.MovieDBProvider;
import server.DB.UserDBProvider;
import server.commands.*;

import java.sql.SQLException;


public class Main {


    public static void main(String[] args) {

        CollectionManager collectionManager = new CollectionManager();
        Server.getServer().setCollectionManager(collectionManager);
        try {
            DatabaseManager db = new DatabaseManager();
            MovieDBProvider movieDBProvider = new MovieDBProvider(db.getConnection());
            UserDBProvider userDBProvider = new UserDBProvider(db.getConnection());
            Server.getServer().setMovieDBProvider(movieDBProvider);
            Server.getServer().setUserDAO(userDBProvider);
            collectionManager.loadFromDatabase(movieDBProvider);
            System.out.println("Соединение с локальной БД успешно!");
        } catch (SQLException e) {
            System.out.println("Не удалось подключиться к локальной БД. Проверьте настройки подключения.");
            System.exit(0);
        }

        Server server = Server.getServer();


        CommandManager commandManager = new CommandManager();
        server.setCommandManager(commandManager);



//        server.setFileManager(new FileManager());
//        server.setConsoleCaller(new ConsoleCaller(new Save()));


        Help help = new Help();


        commandManager.registerCommand("help", help);
        commandManager.registerCommand("info", new Info());
        commandManager.registerCommand("add", new Add());
        commandManager.registerCommand("show", new Show());
        commandManager.registerCommand("clear", new Clear());
        commandManager.registerCommand("shuffle", new Shuffle());
        commandManager.registerCommand("update_id", new Update());
        commandManager.registerCommand("remove_by_id", new Remove());
        commandManager.registerCommand("print_unique_mpaa_rating", new PrintMpaa());
        commandManager.registerCommand("print_field_ascending_oscars_count", new PrintOscarsCount());
        commandManager.registerCommand("execute_script", new ExecuteScript());
        commandManager.registerCommand("group_counting_by_oscarsCount", new GroupCounting());
        commandManager.registerCommand("add_if_max", new AddIfMax());
        commandManager.registerCommand("register", new Register());
        commandManager.registerCommand("login", new Login());
        commandManager.registerCommand("insert_at",new Insert());





//        FileManager fileManager = new FileManager();
//        fileManager.importCollection();


        server.start();
    }
}
