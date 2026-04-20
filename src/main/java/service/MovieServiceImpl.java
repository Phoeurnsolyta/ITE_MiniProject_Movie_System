package service;

import model.GenreResponse;
import model.MovieDetail;
import model.MovieResponse;
import model.VideoResponse;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class MovieServiceImpl implements MovieService {
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String ACCESS_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiMGM3NjE0YjdhYWZiNjAwMGU1ZGVjODNmMGZlMzg2NSIsIm5iZiI6MTc3NTk3NDA5Ni42NjYsInN1YiI6IjY5ZGIzNmQwZjZhNzRlMDY5ZTcwYTI1MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.whpwHaKZcbWkILZjeKeP-v-cJz1YSnTm69yptY_e_HY";

    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final ObjectMapper mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();

    private HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .GET()
                .build();
    }

    @Override
    public MovieResponse searchMovies(String query, int page) {
        String url = String.format("%s/search/movie?query=%s&page=%d", BASE_URL, query, page);
        try {
            HttpResponse<String> response = client.send(
                    buildRequest(url), HttpResponse.BodyHandlers.ofString()
            );
            return mapper.readValue(response.body(), MovieResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MovieDetail getMovieDetail(int movieId) {
        String url = String.format("%s/movie/%d?language=en-US", BASE_URL, movieId);
        try {
            HttpResponse<String> response = client.send(
                    buildRequest(url), HttpResponse.BodyHandlers.ofString()
            );
            return mapper.readValue(response.body(), MovieDetail.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public VideoResponse getMovieVideos(int movieId) {
        String url = String.format("%s/movie/%d/videos", BASE_URL, movieId);
        try {
            HttpResponse<String> response = client.send(
                    buildRequest(url), HttpResponse.BodyHandlers.ofString()
            );
            return mapper.readValue(response.body(), VideoResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GenreResponse getGenres() {
        String url = String.format("%s/genre/movie/list?language=en", BASE_URL);
        try {
            HttpResponse<String> response = client.send(
                    buildRequest(url), HttpResponse.BodyHandlers.ofString()
            );
            return mapper.readValue(response.body(), GenreResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public MovieResponse searchMoviesByGenre(int genreId, int page) {
        String url = String.format("%s/discover/movie?with_genres=%d&page=%d", BASE_URL, genreId, page);
        try {
            HttpResponse<String> response = client.send(
                    buildRequest(url), HttpResponse.BodyHandlers.ofString()
            );
            return mapper.readValue(response.body(), MovieResponse.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}