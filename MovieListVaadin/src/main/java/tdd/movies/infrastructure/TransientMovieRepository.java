package tdd.movies.infrastructure;

import java.util.ArrayList;
import java.util.List;

import tdd.movies.domain.Movie;
import tdd.movies.domain.MovieRepository;

public class TransientMovieRepository implements MovieRepository {

	private List<Movie> movies = new ArrayList<Movie>();

	public TransientMovieRepository() {

		movies.add(new Movie("Green Mile, The", "Frank Darabont", 1999));
		movies.add(new Movie("Forrest Gump", "Robert Zemeckis", 1994));
		movies.add(new Movie("Matix, The", "Andy Wachowski, Lana Wachowski",
				1999));
		movies.add(new Movie("Shawshank Redemption, The", "Frank Darabont",
				1994));
		movies.add(new Movie("Léon", "Luc Besson", 1994));
		movies.add(new Movie("Requiem for a Dream", "Darren Aronofsky", 2000));
	}

	@Override
	public List<Movie> loadAll() {
		return movies;
	}

	@Override
	public void persistMovie(Movie movie) {
		movies.add(movie);
	}
}
