package server;

import common.collectionObject.Movie;
import server.DB.MovieDBProvider;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;


import java.util.concurrent.ConcurrentHashMap;

///**
// * Менеджер коллекции, отвечающий за управление и обработку коллекции фильмов.
// * Позволяет добавлять, обновлять, удалять и сортировать объекты коллекции.
// */
//public class CollectionManager {
//    /**
//     * Коллекция фильмов, хранящаяся в виде связного списка.
//     */
//    private LinkedList<Movie> collection = new LinkedList<>();
//
//    /**
//     * Текущий id для фильмов.
//     */
//    private Long currentid = 0L;
//
//    /** Объект для заполнения информации о фильме. */
//    MovieFiller movieFiller = new MovieFiller();
//
//    /**
//     * Получает коллекцию фильмов.
//     *
//     * @return коллекция фильмов
//     */
//    public LinkedList<Movie> getCollection() {
//        return collection;
//    }
//
//    /**
//     * Устанавливает максимальный id в коллекции.
//     */
//    public void setCurrentid() {
//        for (Movie movie : collection) {
//            if (movie.getId() > currentid) {
//                currentid = movie.getId();
//            }
//        }
//    }
//    public Long getCurrentid(){
//        return ++currentid;
//    }
//
//    /**
//     * Устанавливает и сортирует текущую коллекцию.
//     *
//     * @param movies объект, содержащий новую коллекцию фильмов
//     */
//    public void setCollection(Movies movies) {
//        this.collection = movies.getMovies();
//        checkCollection(movies);
//        setCurrentid();
//        Collections.sort(collection);
//    }
//
//    /**
//     * Создает новый фильм с уникальным идентификатором и заполняет его данными.
//     *
//     * @return созданный объект фильма
//     */
//    public Movie createMovie() {
//        Movie movie = new Movie();
//        movie.setId(++currentid);
//        movie = movieFiller.fill(movie);
//        return movie;
//    }
//
//    /**
//     * Добавляет фильм в коллекцию.
//     *
//     * @param movie фильм для добавления
//     */
//    public void addMovie(Movie movie) {
//        movie.setId(++currentid);
//        collection.add(movie);
//    }
//
//    /**
//     * Выводит содержимое коллекции в консоль.
//     */
//    public void showCollection() {
//        if (collection.isEmpty()) {
//            System.out.println("Коллекция пуста");
//        } else {
//            for (Movie movie : collection) {
//                System.out.println(movie.toString());
//            }
//        }
//    }
//
//    /**
//     * Очищает коллекцию фильмов.
//     */
//    public void clearCollection() {
//        collection.clear();
//        currentid = 0L;
//    }
//
//    /**
//     * Перемешивает элементы коллекции в случайном порядке.
//     */
//    public void shuffleCollection() {
//        Collections.shuffle(collection);
//    }
//
//    /**
//     * Ищет фильм в коллекции по его id.
//     *
//     * @param id идентификатор фильма
//     * @return найденный фильм или null, если фильм не найден
//     */
//    public Movie findMovieById(Long id) {
//        for (Movie movie : collection) {
//            if (movie.getId().equals(id)) {
//                return movie;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Обновляет существующий элемент коллекции по его id.
//     *
//     * @param id    id фильма
//     * @param movie новый объект фильма
//     */
//    public void updateElement(Long id, Movie movie, Movie update) {
//        collection.remove(movie);
//        update.setId(id);
//        collection.add(update);
//    }
//
//    /**
//     * Удаляет фильм из коллекции.
//     *
//     * @param movie фильм для удаления
//     */
//    public void removeElement(Movie movie) {
//        collection.remove(movie);
//    }
//
//    /**
//     * Вставляет новый элемент в указанную позицию коллекции.
//     *
//     * @param index индекс, куда будет добавлен фильм
//     */
//    public void insertElement(Movie movie,int index) {
//        try {
//            collection.add(index, movie);
//            System.out.println("Элемент успешно добавлен в коллекцию");
//        } catch (IndexOutOfBoundsException e) {
//            System.out.println("Введён неверный индекс");
//        }
//    }
//
//    /**
//     * Получает уникальные значения MPAA рейтингов из коллекции.
//     *
//     * @return множество уникальных значений MPAA рейтинга
//     */
//    public HashSet<String> uniqueMpaa() {
//        HashSet<String> set = new HashSet<>();
//        for (Movie movie : collection) {
//            set.add(valueOf(movie.getMpaaRating()));
//        }
//        return set;
//    }
//
//
//    public String printOscarsCount() {
//        return collection.stream()
//                .map(Movie::getOscarsCount)
//                .sorted()
//                .map(String::valueOf)
//                .collect(Collectors.joining("\n"));
//
//
//    }
//
//    /**
//     * Определяет максимальное количество oscarsCount среди всех фильмов в коллекции.
//     *
//     * @return максимальное количество oscarsCount
//     */
//    public int getMaxOscarsCount() {
//        int maxOscars = Integer.MIN_VALUE;
//        for (Movie movie : collection) {
//            if (movie.getOscarsCount() > maxOscars) {
//                maxOscars = movie.getOscarsCount();
//            }
//        }
//        return maxOscars;
//    }
//    public void checkCollection(Movies movies){
//        for (Movie movie: movies.getMovies()){
//            System.out.println(movie.toString());
//        }
//    }
//}


public class CollectionManager {
    private ConcurrentHashMap<Long, Movie> movieMap = new ConcurrentHashMap<>();

//    public void loadFromDatabase(List<Movie> movies) {
//        movieMap.clear();
//        for (Movie movie : movies) {
//            movieMap.put(movie.getId(), movie);
//        }
//    }

    public void addMovie(Movie movie) {
        movieMap.put(movie.getId(), movie);
    }


    public void updateMovie(Long id, Movie updatedMovie) {
        movieMap.put(id, updatedMovie);
    }

    public void removeMovieById(long id) {
        movieMap.remove(id);
    }

    public void clearCollection(){
        movieMap.clear();
    }
    public void clearMoviesByUser(int userId) {
        movieMap.entrySet().removeIf(entry -> entry.getValue().getOwnerId() == userId);
    }


    public Collection<Movie> getCollection() {
        return movieMap.values();
    }

    public Movie findMovieById(Long id) {
        return movieMap.get(id);
    }

    public int getMaxOscarsCount() {
        return movieMap.values().stream()
                .mapToInt(Movie::getOscarsCount)
                .max().orElse(0);
    }

    public String printOscarsCount() {
        return movieMap.values().stream()
                .map(Movie::getOscarsCount)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining("\n"));
    }

    public HashSet<String> uniqueMpaa() {
        return movieMap.values().stream()
                .map(m -> m.getMpaaRating().toString())
                .collect(Collectors.toCollection(HashSet::new));
    }

    public int size() {
        return movieMap.size();
    }
    public void loadFromDatabase(MovieDBProvider movieDBProvider) {

        try {
            List<Movie> fromDb = movieDBProvider.getAllMovies();
            for (Movie m : fromDb) {
                movieMap.put(m.getId(), m);
            }
            System.out.println("Коллекция загружена из БД. Размер: " + movieMap.size());
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке коллекции из БД: " + e.getMessage());
        }
    }
    public void updateMovieById(long id, Movie newMovie) {
        newMovie.setId(id);
        movieMap.put(id, newMovie);
    }
}
