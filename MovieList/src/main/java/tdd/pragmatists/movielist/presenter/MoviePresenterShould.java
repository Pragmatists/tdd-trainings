package tdd.pragmatists.movielist.presenter;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tdd.pragmatists.movielist.movies.Movie;
import tdd.pragmatists.movielist.view.MovieView;

public class MoviePresenterShould {

	private static final String STAR_WARS = "Star wars";
	private MovieView movieView = mock(MovieView.class);

	@Test
	public void addAMovie() throws Exception {
		MovieListPresenter movieListPresenter = presenterWithNoMovies();
		when(movieView.getNewTitle()).thenReturn(STAR_WARS);

		movieListPresenter.add();

		verify(movieView).showMovies(list(movieTitled(STAR_WARS)));
	}

	private Movie movieTitled(String title) {
		return new Movie(title);
	}

	private MovieListPresenter presenterWithNoMovies() {
		return new MovieListPresenter(movieView, emptyList(Movie.class));
	}

	private <T> List<T> emptyList(Class<T> clazz) {
		return new ArrayList<T>();
	}

	private <T> List<T> list(T... elements) {
		List<T> list = new ArrayList<T>();
		for (T e : elements) {
			list.add(e);
		}
		return list;
	}
}
