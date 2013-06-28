package tdd.movies.infrastructure;

import java.util.List;

import javax.persistence.EntityManager;

import tdd.movies.domain.Movie;
import tdd.movies.domain.MovieRepository;

public class JpaMovieRepository implements MovieRepository{

    public JpaMovieRepository(EntityManager entityManager) {
    }

    @Override
    public List<Movie> loadAll() {
        return null;
    }

}
