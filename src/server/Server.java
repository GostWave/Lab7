
package server;

import common.MovieDAO;
import common.Request;
import common.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import server.IO.FileManager;
import server.commands.Command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Server {
    private static final int PORT = 5011;
    private static final int BUFFER_SIZE = 8192;
//    private static final Logger logger = LogManager.getLogger(Server.class);

    private CollectionManager collectionManager;
//    private ConsoleCaller consoleCaller;
//    private FileManager fileManager;
    private CommandManager commandManager;
    private static Server server;
    private MovieDAO movieDAO;

    private Server() {}

    public static Server getServer() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }


    public void start() {
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
////            fileManager.fileWriter();
////            logger.info("Сервер остановлен. Коллекция сохранена в файл.");
//            System.out.println("Сервер остановлен. Коллекция сохранена в файл.");
//        }));
//        new Thread(consoleCaller).start();

        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.bind(new InetSocketAddress(PORT));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);


//            logger.info("Сервер запущен на порту " + PORT);
            System.out.println("Сервер запущен на порту " + PORT);

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                selector.select();
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        handleAccept(key, selector);
                    } else if (key.isReadable()) {
                        handleRead(key, buffer);
                    }
                }
            }

        } catch (IOException e) {
//            logger.error("Ошибка сервера: " + e.getMessage());
            System.out.println("Ошибка сервера: " + e.getMessage());
        }
    }

    private void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();

        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
//            logger.info("Клиент подключен: " + clientChannel.getRemoteAddress());
            System.out.println("Клиент подключен: " + clientChannel.getRemoteAddress());
        }

    }

    private void handleRead(SelectionKey key, ByteBuffer buffer) {
        SocketChannel clientChannel = (SocketChannel) key.channel();

        try {
            buffer.clear();
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                clientChannel.close();
//                logger.info("Клиент отключился.");
                System.out.println("Клиент отключился.");
                return;
            }

            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);


            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Request request = (Request) ois.readObject();

//            logger.info("Получена команда: " + request.getCommandName());
            System.out.println("Получена команда: " + request.getCommandName());

            Command command = commandManager.getCommandByKey(request.getCommandName());
            Response response;
            if (command != null) {
                response = command.execute(request.getCommandStrArg(), request.getCommandObjArg());
            } else {
                response = new Response("Неизвестная команда: " + request.getCommandName());
            }



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            oos.flush();

            byte[] responseBytes = baos.toByteArray();
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
            clientChannel.write(responseBuffer);

        } catch (IOException | ClassNotFoundException e) {
            try {
//                logger.error("Ошибка при чтении/обработке клиента");
                System.out.println("Ошибка при чтении/обработке клиента");
                clientChannel.close();
            } catch (IOException ex) {
//                logger.error("Не удалось закрыть соединение" );
                System.out.println("Не удалось закрыть соединение");
            }
        }
    }
    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

//    public void setConsoleCaller(ConsoleCaller consoleCaller) {
//        this.consoleCaller = consoleCaller;
//    }

//    public void setFileManager(FileManager fileManager) {
//        this.fileManager = fileManager;
//    }

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

//    public ConsoleCaller getConsoleCaller() {
//        return consoleCaller;
//    }

//    public FileManager getFileManager() {
//        return fileManager;
//    }


    public void setMovieDAO(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public MovieDAO getMovieDAO() {
        return movieDAO;
    }
}

