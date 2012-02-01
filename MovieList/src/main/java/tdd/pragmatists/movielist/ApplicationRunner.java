package tdd.pragmatists.movielist;

import java.util.ArrayList;

import javax.swing.JFrame;

import tdd.pragmatists.movielist.movies.Movie;
import tdd.pragmatists.movielist.presenter.MovieListPresenter;
import tdd.pragmatists.movielist.view.SwingMovieList;

public class ApplicationRunner {
	public static void main(String[] args) {
		SwingMovieList swingMovieList = new SwingMovieList();
		MovieListPresenter movieListPresenter = new MovieListPresenter(
				swingMovieList, new ArrayList<Movie>());
		swingMovieList.observeWith(movieListPresenter);

		JFrame frame = new MainApplicationFrame(swingMovieList);
		frame.setVisible(true);
	}
}
