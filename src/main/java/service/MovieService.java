package service;

import model.MovieDetail;
import model.MovieResponse;
import model.VideoResponse;

public interface MovieService {
    MovieResponse searchMovies(String query, int page);

    MovieDetail getMovieDetail(int movieId);

    VideoResponse getMovieVideos(int movieId);
}