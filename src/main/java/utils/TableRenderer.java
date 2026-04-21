package utils;

import model.*;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import service.MovieService;

import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.SwingConstants.CENTER;
import static javax.swing.SwingConstants.LEFT;

public class TableRenderer {
    private static final CellStyle CENTER = new CellStyle(CellStyle.HorizontalAlign.CENTER);
    private static final CellStyle LEFT = new CellStyle(CellStyle.HorizontalAlign.LEFT);

    // display movie list table
    public static void displayMovieTable(MovieResponse response, MovieService movieService) {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        String[] columns = {"ID", "Title", "Release Date", "Rating", "Trailer"};
        for (String column : columns) {
            table.addCell(column, CENTER);
        }

        for (Movie movie : response.getResults()) {
            String trailerUrl = "N/A";
            try {
                VideoResponse videoResponse = movieService.getMovieVideos(movie.getId());
                if (videoResponse.getResults() != null) {
                    trailerUrl = videoResponse.getResults().stream()
                            .filter(v -> "Trailer".equals(v.getType()) && "YouTube".equals(v.getSite()))
                            .findFirst()
                            .map(v -> "https://youtube.com/watch?v=" + v.getKey())
                            .orElse("N/A");
                }
            } catch (Exception ignored) {}

            table.addCell(movie.getId().toString(), CENTER);
            table.addCell(movie.getTitle() != null ? movie.getTitle() : "N/A", LEFT);
            table.addCell(movie.getReleaseDate() != null ? movie.getReleaseDate() : "N/A", CENTER);
            table.addCell(movie.getVoteAverage() != null ? String.format("%.1f", movie.getVoteAverage()) : "N/A", CENTER);
            table.addCell(trailerUrl, LEFT);
        }

        System.out.println(table.render());
    }

    public static void displayGenreTable(GenreResponse genreResponse) {
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        String[] columns = {"ID", "Genre"};
        for (String column : columns) {
            table.addCell(column, CENTER);
        }

        for (Genre genre: genreResponse.getGenres()) {
            table.addCell(genre.getId().toString(), CENTER);
            table.addCell(genre.getName(), LEFT);
        }

        System.out.println(table.render());
    }

    public static void displayMovieDetail(MovieDetail detail, String trailerUrl) {
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        String genres = detail.getGenres() != null
                ? detail.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", "))
                : "N/A";

        String origin = detail.getOriginCountry() != null
                ? String.join(", ", detail.getOriginCountry())
                : "N/A";

        String[][] rows = {
                {"Title",        detail.getTitle() != null ? detail.getTitle() : "N/A"},
                {"Release Date", detail.getReleaseDate() != null ? detail.getReleaseDate() : "N/A"},
                {"Rating",       detail.getVoteAverage() != null ? String.format("%.1f", detail.getVoteAverage()) : "N/A"},
                {"Runtime",      detail.getRuntime() != null ? detail.getRuntime() + " min" : "N/A"},
                {"Budget",       detail.getBudget() != null ? "$" + String.format("%,d", detail.getBudget()) : "N/A"},
                {"Genres",       genres},
                {"Origin",       origin},
                {"Trailer",      trailerUrl}
        };

        for (String[] row : rows) {
            table.addCell(row[0], CENTER);
            table.addCell(row[1], LEFT);
        }

        System.out.println(table.render());
    }

    public static void displayMainMenu() {
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        table.addCell("Option", CENTER);
        table.addCell("Description", CENTER);

        table.addCell("[1]", CENTER);
        table.addCell("Search Movie By Title", LEFT);

        table.addCell("[2]", CENTER);
        table.addCell("Search Movie By Genre", LEFT);

        table.addCell("[3]", CENTER);
        table.addCell("Popular Movies", LEFT);

        table.addCell("[4]", CENTER);
        table.addCell("Top Rated Movies", LEFT);

        table.addCell("[0]", CENTER);
        table.addCell("Exit", LEFT);

        System.out.println(table.render());
    }
}
