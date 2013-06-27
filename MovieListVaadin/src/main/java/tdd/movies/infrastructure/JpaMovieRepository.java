package tdd.movies.infrastructure;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import tdd.movies.domain.Movie;
import tdd.movies.domain.MovieRepository;

public class JpaMovieRepository implements MovieRepository{

    private EntityManager entityManager;

	public JpaMovieRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
    }

    @Override
    public List<Movie> loadAll() {
        return entityManager.createQuery("from Movie").getResultList();
    }

	@Override
	public void persistMovie(Movie movie) {
		entityManager.persist(movie);
	}

}
