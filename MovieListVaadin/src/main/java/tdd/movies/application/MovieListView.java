package tdd.movies.application;

import tdd.movies.domain.Movie;

public interface MovieListView {

	public void addMovieToList(Movie movie);

	public void showErrorMessage(String string);

}
