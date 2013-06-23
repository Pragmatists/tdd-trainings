package tdd.seams;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

public class Ping3 {

    public boolean ping(String url, WebParser parser) {
        
        try{
            
            String responseText = parser.parse(url);
            return responseText.contains("Status: OK");
            
        } catch(Exception e){
            return false;
        }
    }

    public static class WebParser {

        public String parse(String url) throws IOException, MalformedURLException {
            InputStream response = new URL(url).openStream();
            return IOUtils.toString(response);
        }
    }
    
}

