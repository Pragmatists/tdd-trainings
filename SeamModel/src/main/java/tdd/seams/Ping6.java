package tdd.seams;

public class Ping6 {

    public boolean ping(String url) {

        try {

            String responseText = WebParser.parseStatic(url);
            return responseText.contains("Status: OK");

        } catch (Exception e) {
            return false;
        }
    }

}
