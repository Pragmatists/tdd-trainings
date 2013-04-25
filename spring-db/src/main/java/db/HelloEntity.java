package db;

import java.io.*;

public class HelloEntity {

	String id;

	public Serializable getId() {
		return id;
	}

	public HelloEntity id(String string) {
		this.id = string;
		return this;
	}

}
