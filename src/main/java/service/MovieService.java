package service;

import model.GenreResponse;
import model.MovieDetail;
import model.MovieResponse;
import model.VideoResponse;

public interface MovieService {
    MovieResponse searchMovies(String query, int page);

    MovieDetail getMovieDetail(int movieId);

    VideoResponse getMovieVideos(int movieId);

    GenreResponse getGenres();

    MovieResponse searchMoviesByGenre(int genreId, int page);

    MovieResponse getPopularMovies(int page);

    MovieResponse getTopRatedMovies(int page);

    MovieResponse getRecommendations(int movieId, int page);
}