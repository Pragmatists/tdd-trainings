package tdd.pragmatists.movielist.presenter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import tdd.pragmatists.movielist.db.Movie;
import tdd.pragmatists.movielist.db.MovieRepository;
import tdd.pragmatists.movielist.view.MovieView;

@RunWith(MockitoJUnitRunner.class)
public class MovieListPresenterTest {

	@Mock
	private MovieView movieView;
	@Mock
	private MovieRepository movies;
	@InjectMocks
	private MovieListPresenter movieListPresenter;

	private static final String STAR_WARS = "Star wars";

	@Test
	public void shouldLoadMoviesOnShow() throws Exception {
		List<Movie> moviesList = new ArrayList<Movie>();
		moviesList.add(new Movie("m"));
		Mockito.when(movies.findAll()).thenReturn(moviesList);

		movieListPresenter.shown();

		Mockito.verify(movieView).showMovies(moviesList);
	}

	@Test
	public void addAMovie() {
		when(movieView.getNewTitle()).thenReturn(STAR_WARS);

		movieListPresenter.add();

		verify(movieView).showMovies(list(movieTitled(STAR_WARS)));
	}
	
	private Movie movieTitled(String title) {
		return new Movie(title);
	}

	private <T> List<T> list(T... elements) {
		List<T> list = new ArrayList<T>();
		for (T e : elements) {
			list.add(e);
		}
		return list;
	}

}
