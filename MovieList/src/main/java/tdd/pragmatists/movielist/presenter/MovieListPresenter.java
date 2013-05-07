package tdd.pragmatists.movielist.presenter;

import java.util.ArrayList;
import java.util.List;

import tdd.pragmatists.movielist.db.Movie;
import tdd.pragmatists.movielist.db.MovieRepository;
import tdd.pragmatists.movielist.view.MovieView;

public class MovieListPresenter {

    private final MovieView movieView;
    private final MovieRepository movies;
    private final List<Movie> moviesList = new ArrayList<Movie>();

    public MovieListPresenter(MovieView movieView, MovieRepository movies) {
        this.movieView = movieView;
        this.movies = movies;
    }

    public void add() {
        Movie movie = new Movie(movieView.getNewTitle());
		movies.save(movie);
		moviesList.add(movie);
        movieView.showMovies(moviesList);
    }

	public void shown() {
		movieView.showMovies(movies.findAll());
	}

}
