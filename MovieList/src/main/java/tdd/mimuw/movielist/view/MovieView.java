package tdd.mimuw.movielist.view;

import java.util.List;

import tdd.mimuw.movielist.movies.Movie;

public interface MovieView {

	String getNewTitle();

	void showMovies(List<Movie> movies);

	Movie getSelectedMovie();

}
