package tdd.pragmatists.movielist.presenter;

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
	private MovieListPresenter 	movieListPresenter;

	@Test
	public void shouldLoadMoviesOnShow() throws Exception {
		List<Movie> moviesList = new ArrayList<Movie>();
		moviesList.add(new Movie("m"));
		Mockito.when(movies.findAll()).thenReturn(moviesList);
		
		movieListPresenter.shown();
		
		Mockito.verify(movieView).showMovies(moviesList);
	}
	
	//TODO : usun implementacje metody 'add' i zacznij od testów na 'add'
	
}
