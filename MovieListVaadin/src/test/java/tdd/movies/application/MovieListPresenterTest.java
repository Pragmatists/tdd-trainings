package tdd.movies.application;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import tdd.movies.domain.Movie;
import tdd.movies.domain.MovieRepository;

@RunWith(MockitoJUnitRunner.class)
public class MovieListPresenterTest {

	@Mock
	private MovieRepository repository;
	@Mock
	private MovieListView view;

	private MovieListPresenter presenter;

	@Before
	public void setUp() {

		presenter = new MovieListPresenter(repository, view);
	}

	@Test
	public void shouldPopulateMoviesOnStart() throws Exception {

		// given:
		Movie movie1 = aMovie("Matrix, The");
		Movie movie2 = aMovie("Titanic, The");
		repositoryContainsFollowingMovies(movie1, movie2);

		// when:
		presenter.onStart();

		// then:
		verify(view).addMovieToList(movie1);
		verify(view).addMovieToList(movie2);
	}

	@Test
	public void shouldPersistAddedMovie() throws Exception {

		presenter.addMovie("matrix", "bracia wachowscy", "2010");

		verify(repository).persistMovie(
				new Movie("matrix", "bracia wachowwscy", 2010));
	}

	@Test
	public void shouldShowAddedMovie() throws Exception {

		presenter.addMovie("matrix", "bracia wachowscy", "2010");

		verify(view).addMovieToList(
				new Movie("matrix", "bracia wachowscy", 2010));
		Mockito.verifyNoMoreInteractions(view);
	}

	@Test
	public void shouldNotAllowToAddMovieWithoutFields() throws Exception {

		presenter.addMovie("", "", "");

		Mockito.verifyZeroInteractions(repository);
	}

	@Test
	public void shouldNotAllowToAddMovieWithNonNumericalYear() throws Exception {

		presenter.addMovie("", "", "rok smoka");

		Mockito.verifyZeroInteractions(repository);
	}

	@Test
	public void shouldDisplayValidationErrorMessageWhenMovieWithNonNumericalYearAdded()
			throws Exception {

		presenter.addMovie("", "", "rok smoka");

		verify(view).showErrorMessage("rok musi być liczbą");
	}

	// --

	private void repositoryContainsFollowingMovies(Movie... movies) {
		when(repository.loadAll()).thenReturn(Arrays.asList(movies));
	}

	private Movie aMovie(String title) {
		return new Movie(title, "", 2000);
	}

}
