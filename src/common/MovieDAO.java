package common;

import common.collectionObject.*;

import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    private final Connection connection;

    public MovieDAO(Connection connection) {
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
                long id = rs.getLong("id");
                movie.setId(id);
                return movie;
            } else {
                throw new SQLException("Не удалось получить сгенерированный ID фильма.");
            }
        }
    }

}