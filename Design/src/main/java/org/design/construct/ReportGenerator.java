package org.design.construct;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.design.statical.User;

public class ReportGenerator {

	private LegacyDatabase db;
	private File reportFile;

	public ReportGenerator() throws IOException {
		db = new LegacyDatabase();
		db.connect();
		reportFile = new File("/var/log/report.log");
	}
	
	public void generateReportFor(User user) throws FileNotFoundException, IOException {
		UserData data = db.getDataFor(user);
		if(data.isSpecial()) {
			new FileOutputStream(reportFile).write(("special,"+user.email).getBytes());
		} else {
			new FileOutputStream(reportFile).write(("normal,"+user.email).getBytes());
		}
	}

}
