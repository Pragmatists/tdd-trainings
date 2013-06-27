package tdd.movies.application;

import tdd.movies.domain.Movie;
import tdd.movies.domain.MovieRepository;

public class MovieListPresenter {

	private MovieListView view;
	private MovieRepository repository;

	public MovieListPresenter(MovieRepository repository, MovieListView view) {

		this.view = view;
		this.repository = repository;
	}

	public void onStart() {

		for (Movie movie : repository.loadAll()) {
			view.addMovieToList(movie);
		}
	}

	public void addMovie(String title, String directors, String year) {
		if (isYearValid(year)) {
			Movie movie = new Movie(title, directors, Integer.parseInt(year));
			repository.persistMovie(movie);
			view.addMovieToList(movie);
		} else {
			view.showErrorMessage("rok musi być liczbą");
		}
	}

	private boolean isYearValid(String year) {
		try {
			Integer.parseInt(year);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}
