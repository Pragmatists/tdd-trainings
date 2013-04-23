package tdd.pragmatists.movielist.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String>{

	public List<Movie> findByName(String name);
	
}
