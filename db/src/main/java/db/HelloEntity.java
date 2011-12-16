package db;

import java.io.*;

import javax.persistence.*;

@Entity
public class HelloEntity {

	@Id
	String id;

	public Serializable getId() {
		return id;
	}

	public HelloEntity id(String string) {
		this.id = string;
		return this;
	}

}
