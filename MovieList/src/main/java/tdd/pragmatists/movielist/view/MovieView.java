package tdd.pragmatists.movielist.view;

import java.util.List;

import tdd.pragmatists.movielist.movies.Movie;

public interface MovieView {

	String getNewTitle();

	void showMovies(List<Movie> movies);

	Movie getSelectedMovie();

}
