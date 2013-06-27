package tdd.movies.infrastructure;

import javax.persistence.EntityManager;

import tdd.movies.domain.Movie;

public class MovieBuilder {

	private String title = "matrix";
	String director = "bracia";
	int year = 2010;
	

	Movie build() {
		return new Movie(title, director, year);
	}

	public MovieBuilder titled(String title) {
		this.title = title;
		return this;
	}

	static MovieBuilder aMovie() {
		return new MovieBuilder();
	}

	public Movie persist(EntityManager entityManager) {
		Movie movie = build();
		entityManager.persist(movie);
		return movie;
	}

}
