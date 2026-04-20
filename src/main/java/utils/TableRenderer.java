package utils;

import model.Genre;
import model.Movie;
import model.MovieDetail;
import model.MovieResponse;
import model.Video;
import model.VideoResponse;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;
import service.MovieService;

import java.util.List;
import java.util.stream.Collectors;

public class TableRenderer {
    private static final CellStyle CENTER = new CellStyle(CellStyle.HorizontalAlign.CENTER);
    private static final CellStyle LEFT = new CellStyle(CellStyle.HorizontalAlign.LEFT);

    // display movie list table
    public static void displayMovieTable(MovieResponse response, MovieService movieService) {
        Table table = new Table(5, BorderStyle.CLASSIC, ShownBorders.ALL);

        String[] columns = {"ID", "Title", "Release Date", "Rating", "Trailer"};
        for (String column : columns) {
            table.addCell(column, CENTER);
        }

        for (Movie movie : response.getResults()) {
            // fetch trailer for each movie
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

    // display movie detail
    public static void displayMovieDetail(MovieDetail detail, String trailerUrl) {
        Table table = new Table(2, BorderStyle.CLASSIC, ShownBorders.ALL);

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
}
