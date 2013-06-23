package tdd.seams;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class WebParser {

    public static String parseStatic(String url) throws IOException, MalformedURLException {
        
        InputStream response = new URL(url).openStream();
        return IOUtils.toString(response);
    }

    public String parse(String url) throws IOException, MalformedURLException {

        return parseStatic(url);
    }

}
