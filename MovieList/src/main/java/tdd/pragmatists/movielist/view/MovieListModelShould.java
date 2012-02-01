package tdd.pragmatists.movielist.view;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.junit.Test;

import tdd.pragmatists.movielist.movies.Movie;

public class MovieListModelShould {

	@Test
	public void invokeListenersOnUpdate() throws Exception {
		MovieListModel movieListModel = new MovieListModel();
		ListDataListener listener = mock(ListDataListener.class);
		movieListModel.addListDataListener(listener);

		movieListModel.update(new ArrayList<Movie>());

		verify(listener).contentsChanged(any(ListDataEvent.class));
	}
}
