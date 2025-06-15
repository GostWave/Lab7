package server.DB;

import common.collectionObject.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDBProvider {
    private final Connection connection;
    private long id;
    public MovieDBProvider(Connection connection) {
        this.connection = connection;
    }
    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> movieList = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getLong("id"));
                movie.setName(rs.getString("name"));
                movie.setOwnerId(rs.getInt("user_id"));


                Coordinates coords = new Coordinates();
                coords.setX(rs.getDouble("coordinates_x"));
                coords.setY(rs.getLong("coordinates_y"));
                movie.setCoordinates(coords);

                movie.setCreationDate(rs.getDate("creation_date").toLocalDate());
                movie.setOscarsCount(rs.getInt("oscars_count"));
                movie.setGenre(MovieGenre.valueOf(rs.getString("genre")));
                movie.setMpaaRating(MpaaRating.valueOf(rs.getString("mpaa_rating")));

                Person operator = new Person();
                operator.setName(rs.getString("operator_name"));
                operator.setBirthday(rs.getDate("operator_birthday").toLocalDate());
                operator.setPassportID(rs.getString("operator_passport_id"));
                movie.setOperator(operator);

                movieList.add(movie);
            }
        }

        return movieList;
    }
    public Movie addMovie(Movie movie, int userId) throws SQLException {
        String sql = "INSERT INTO movies " +
                "(name, coordinates_x, coordinates_y, creation_date, oscars_count, genre, mpaa_rating, " +
                "operator_name, operator_birthday, operator_passport_id, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, movie.getName());
            stmt.setDouble(2, movie.getCoordinates().getX());
            stmt.setLong(3, movie.getCoordinates().getY());
            stmt.setDate(4, Date.valueOf(movie.getCreationDate()));
            stmt.setInt(5, movie.getOscarsCount());
            stmt.setString(6, movie.getGenre().name());
            stmt.setString(7, movie.getMpaaRating().name());
            stmt.setString(8, movie.getOperator().getName());
            stmt.setDate(9, Date.valueOf(movie.getOperator().getBirthday()));
            stmt.setString(10, movie.getOperator().getPassportID());
            stmt.setInt(11, userId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getLong("id");
                movie.setId(id);
                return movie;
            } else {
                throw new SQLException("Не удалось получить сгенерированный ID фильма.");
            }
        }
    }
    public void clearMoviesByUser(int userId) throws SQLException {
        String sql = "DELETE FROM movies WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
        if (isMoviesTableEmpty()){
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("TRUNCATE TABLE movies RESTART IDENTITY CASCADE");
            }
        }

    }
    public boolean isMoviesTableEmpty() throws SQLException {
        String sql = "SELECT COUNT(*) FROM movies";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        }
        throw new SQLException("Не удалось проверить пустоту таблицы.");
    }
    public boolean checkOwnership(long movieId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM movies WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, movieId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
    public void deleteMovieById(long movieId) throws SQLException {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, movieId);
            stmt.executeUpdate();
        }
    }

    public boolean updateMovieById(long id, Movie movie, int userId) throws SQLException {
        String sql = "UPDATE movies SET " +
                "name = ?, coordinates_x = ?, coordinates_y = ?, creation_date = ?, " +
                "oscars_count = ?, genre = ?, mpaa_rating = ?, operator_name = ?, " +
                "operator_birthday = ?, operator_passport_id = ? " +
                "WHERE id = ? AND user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, movie.getName());
            stmt.setDouble(2, movie.getCoordinates().getX());
            stmt.setLong(3, movie.getCoordinates().getY());
            stmt.setDate(4, Date.valueOf(movie.getCreationDate()));
            stmt.setInt(5, movie.getOscarsCount());
            stmt.setString(6, movie.getGenre().name());
            stmt.setString(7, movie.getMpaaRating().name());
            stmt.setString(8, movie.getOperator().getName());
            stmt.setDate(9, Date.valueOf(movie.getOperator().getBirthday()));
            stmt.setString(10, movie.getOperator().getPassportID());
            stmt.setLong(11, id);
            stmt.setInt(12, userId);

            return stmt.executeUpdate() > 0;
        }
    }

    public void addMovieWithId(Movie movie, int userId, Long index) throws SQLException {
        String sql = "INSERT INTO movies " +
                "(id, name, coordinates_x, coordinates_y, creation_date, oscars_count, genre, mpaa_rating, " +
                "operator_name, operator_birthday, operator_passport_id, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, movie.getId());
            stmt.setString(2, movie.getName());
            stmt.setDouble(3, movie.getCoordinates().getX());
            stmt.setLong(4, movie.getCoordinates().getY());
            stmt.setDate(5, Date.valueOf(movie.getCreationDate()));
            stmt.setInt(6, movie.getOscarsCount());
            stmt.setString(7, movie.getGenre().name());
            stmt.setString(8, movie.getMpaaRating().name());
            stmt.setString(9, movie.getOperator().getName());
            stmt.setDate(10, Date.valueOf(movie.getOperator().getBirthday()));
            stmt.setString(11, movie.getOperator().getPassportID());
            stmt.setInt(12, userId);

            int rs = stmt.executeUpdate();

            if (rs==0){
                throw new SQLException("Фильм не был добавлен в базу данных");
            }
        }
    }


}