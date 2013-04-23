package tdd.pragmatists.movielist.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HelloEntity {

	public String name;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long id;

	public HelloEntity() {
		
	}
	
	public HelloEntity(String name) {
		this.name = name;
	}

}
