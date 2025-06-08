package client;

import common.MovieFiller;
import common.Request;
import common.Response;
import common.collectionObject.Movie;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    private static final String SERVER_HOST = "localhost";
    //    private static final String SERVER_HOST = "helios.cs.ifmo.ru";
    private static final int SERVER_PORT = 5011;
    private static final int BUFFER_SIZE = 8192;
    private static Client client;
    private final InetAddress host;
    private final int port;
    private String loginPass;
    private String login;
    private String pass;

    private Client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public static Client getClient() {
        if (client == null) {
            try {
                client = new Client(InetAddress.getByName(SERVER_HOST), SERVER_PORT);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }
        return client;
    }

    public void start() {
        while (true) {
            try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(SERVER_HOST, SERVER_PORT));
                 Scanner scanner = new Scanner(System.in)) {

                System.out.println("Подключено к серверу " + SERVER_HOST + ":" + SERVER_PORT);
                while (true) {
                    sendRequest(socketChannel, auth());
                    Response response = receiveResponse(socketChannel);
                    if (response != null) {
                        System.out.println(response.getMessage());
                        if (response.getMessage().equals("Пользователь успешно зарегистрирован.") || response.getMessage().equals("Успешный вход в систему.")) {
                            break;
                        }
                    } else {
                        System.out.println("Ошибка получения ответа от сервера.");
                    }
                }
                while (true) {
                    String argument = "";
                    String commandName = "";
                    try {
                        System.out.print("Введите команду (или 'exit' для выхода): ");
                        String[] text = scanner.nextLine().trim().split(" ");
                        commandName = text[0];

                        if (text.length > 1) {
                            argument = text[1];
                        }
                        if (commandName.equalsIgnoreCase("exit")) {
                            System.out.println("Отключение от сервера...");
                            System.exit(0);
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("Завершение по Ctrl+D...");
                        System.exit(0);
                    }

                    Request request = createRequest(login,pass,commandName, argument);


                    sendRequest(socketChannel, request);


                    Response response = receiveResponse(socketChannel);
                    if (response != null) {
                        System.out.println(response.getMessage());
                    } else {
                        System.out.println("Ошибка получения ответа от сервера.");
                    }
                }

            } catch (IOException e) {
                System.out.println("Сервер не доступен. Повторное подключение...");
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Работа клиента прервана");
                break;
            }

        }
    }

    private void sendRequest(SocketChannel socketChannel, Request request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(request);
        oos.flush();

        byte[] requestBytes = baos.toByteArray();
        ByteBuffer writeBuffer = ByteBuffer.wrap(requestBytes);
        socketChannel.write(writeBuffer);
    }

    private Response receiveResponse(SocketChannel socketChannel) {
        try {
            ByteBuffer readBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            socketChannel.read(readBuffer);
            readBuffer.flip();

            byte[] responseBytes = new byte[readBuffer.remaining()];
            readBuffer.get(responseBytes);

            ByteArrayInputStream bais = new ByteArrayInputStream(responseBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Response) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка получения ответа: " + e.getMessage());
            return null;
        }
    }

    public Request createRequest(String login, String pass, String command, String argument) {
        if (command.equalsIgnoreCase("add") || command.startsWith("update_id") || command.equalsIgnoreCase("add_if_max")) {
            MovieFiller movieFiller = new MovieFiller();
            Movie objArgument = movieFiller.fill(new Movie());
            if (objArgument != null) {
                return new Request(login,pass,command, argument, objArgument);
            } else {
                System.out.println("Ошибка при создании объекта Movie.");
                return null;
            }

        } else {
            return new Request(login, pass, command, argument);
        }
    }

    public Request auth() {
        String text = "";
        while (true) {
            System.out.println("Для авторизации введите login/для регистрации введите register");
            Scanner scanner = new Scanner(System.in);
            try {
                text = scanner.nextLine().trim();
            } catch (NoSuchElementException e) {
                System.out.println("Завершение по Ctrl+D...");
                System.exit(0);
            }
            
            if (text.equalsIgnoreCase("login") || text.equalsIgnoreCase("register")) {
                try {
                    System.out.println("Введите логин и пароль через пробел");
                    loginPass = scanner.nextLine().trim();
                    try {
                        login = loginPass.split(" ")[0];
                        pass = loginPass.split(" ")[1];
                        return new Request(login,pass,text,loginPass);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Неверный формат ввода. Пожалуйста, введите логин и пароль через пробел.");
                    }

                } catch (NoSuchElementException e) {
                    System.out.println("Завершение по Ctrl+D...");
                    System.exit(0);
                }

            } else {
                System.out.println("Неверная команда. Попробуйте снова.");
            }
        }

    }
}


// реализацию лямбд синглтон недостатки фабрика прокси декоратор сериал json xml отличие от сер java