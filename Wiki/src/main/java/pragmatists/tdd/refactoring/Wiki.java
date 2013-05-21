package pragmatists.tdd.refactoring;

import java.util.*;
import java.util.regex.*;

public class Wiki {

	public String parse(String wiki) {
		if (wiki.contains("_")) {
			if (wiki.contains("___")) {
				int end = wiki.indexOf("___") + 3;
				return parse(wiki.substring(0, wiki.indexOf("___"))) + "<hr/>" + (wiki.length() > end ? parse(wiki.substring(end)) : "");
			} else {
				int end = wiki.indexOf("_", wiki.indexOf("_") + 1);
				String text = wiki.substring(wiki.indexOf("_") + 1, end);
				return parse(wiki.substring(0, wiki.indexOf("_"))) + "<i>" + parse(text) + "</i>"
						+ (wiki.length() > end ? parse(wiki.substring(end + 1)) : "");
			}
		}
		if (wiki.contains("*")) {
			String fulltext = "";
			StringTokenizer st = new StringTokenizer(wiki, "\n");
			int numItems = 0;
			while (st.hasMoreTokens()) {
				String line = st.nextToken();
				if (Pattern.matches("^ \\* .*$", line)) {
					if (numItems++ == 0) {
						fulltext += "<ul>";
					}
					fulltext += "<li>" + parse(line.substring(3)) + "</li>";
				} else {
					int end = wiki.indexOf("*", wiki.indexOf("*") + 1);
					String text = wiki.substring(wiki.indexOf("*") + 1, end);
					fulltext += parse(wiki.substring(0, wiki.indexOf("*"))) + "<b>" + parse(text) + "</b>"
							+ (wiki.length() > end ? parse(wiki.substring(end + 1)) : "");
				}
			}
			if (numItems > 0)
				fulltext += "</ul>";
			return fulltext;
		}
		return wiki;
	}
}
