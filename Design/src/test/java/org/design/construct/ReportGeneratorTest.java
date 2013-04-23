package org.design.construct;

import java.io.IOException;

import org.design.statical.User;
import org.fest.assertions.api.Assertions;
import org.junit.Test;

public class ReportGeneratorTest {

	@Test
	public void shouldGenerateReport() throws IOException {
		new ReportGenerator().generateReportFor(aUser("mike"));
		
		//how to assert that something was written to a file?
		//Assertions.assertThat(report).isEqualTo("normal,mike");
	}

	private User aUser(String name) {
		return new User(name + "@email.com",name);
	}
	
}
