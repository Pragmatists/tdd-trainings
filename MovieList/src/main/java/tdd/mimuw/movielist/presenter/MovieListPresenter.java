package tdd.mimuw.movielist.presenter;

import java.util.List;

import tdd.mimuw.movielist.movies.Movie;
import tdd.mimuw.movielist.view.GuiEventObserver;
import tdd.mimuw.movielist.view.MovieView;

public class MovieListPresenter implements GuiEventObserver {

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
