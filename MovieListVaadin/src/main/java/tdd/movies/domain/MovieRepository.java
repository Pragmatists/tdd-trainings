package tdd.movies.domain;

import java.util.List;

public interface MovieRepository {

	public List<Movie> loadAll();

	public void persistMovie(Movie movie);

}
