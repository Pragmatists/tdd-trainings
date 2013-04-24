package tdd.pragmatists.movielist.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import tdd.pragmatists.movielist.db.Movie;

@SuppressWarnings("serial")
public class MovieListModel extends AbstractListModel {

	private List<Movie> movies = new ArrayList<Movie>();

	@Override
	public int getSize() {
		return movies.size();
	}

	@Override
	public Object getElementAt(int index) {
		return movies.get(index);
	}

	public void update(List<Movie> movies) {
		this.movies = movies;
		fireAllContentsChanged();
	}

	private void fireAllContentsChanged() {
		super.fireContentsChanged(this, 0, getSize() - 1);
	}

}
