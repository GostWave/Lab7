package common;

import common.collectionObject.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                operator.setBirthday(ZonedDateTime.from(rs.getDate("operator_birthday").toLocalDate()));
                operator.setPassportID(rs.getString("operator_passport_id"));
                movie.setOperator(operator);

                movieList.add(movie);
            }
        }

        return movieList;
    }

}