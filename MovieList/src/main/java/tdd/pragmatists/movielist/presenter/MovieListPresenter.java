package tdd.pragmatists.movielist.presenter;

import java.util.List;

import tdd.pragmatists.movielist.movies.Movie;
import tdd.pragmatists.movielist.view.MovieView;

public class MovieListPresenter {

    private final MovieView movieView;
    private List<Movie> movies;

    public MovieListPresenter(MovieView movieView, List<Movie> movies) {
        this.movieView = movieView;
        this.movies = movies;
    }

    public void add() {
        movies.add(new Movie(movieView.getNewTitle()));
        movieView.showMovies(movies);
    }

}
