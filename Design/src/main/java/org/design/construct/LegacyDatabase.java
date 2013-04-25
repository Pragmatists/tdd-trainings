package org.design.construct;

import java.io.IOException;
import java.net.URL;

import org.design.statical.User;

public class LegacyDatabase {

	private String content;

	public void connect() throws IOException {
		content = new URL("http://important/db").getContent().toString();
	}

	public UserData getDataFor(User user) {
		return new UserData(Boolean.parseBoolean(content));
	}

}
