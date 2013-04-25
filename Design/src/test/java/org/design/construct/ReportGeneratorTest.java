package org.design.construct;

import java.io.IOException;

import org.design.statical.User;
import org.junit.Test;
/**
 * TODO:
 * uruchom test - zaobserwuj że rzuca wyjątkiem, pomimo braku asercji
 * obejrzyj implementację konstruktora new ReportGenerator()
 * i zaobserwuj problematyczne instrukcje
 * zrefaktoryzuj tak, aby test nie rzucał wyjątku
 * zrefaktoryzuj tak, aby udało się napisać asercję
 * zaimplementuj aby test przechodził
 *
 */
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
