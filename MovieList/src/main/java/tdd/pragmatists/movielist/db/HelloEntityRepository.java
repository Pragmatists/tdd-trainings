package tdd.pragmatists.movielist.db;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloEntityRepository extends JpaRepository<HelloEntity, String>{

	public List<HelloEntity> findByName(String name);
	
}
