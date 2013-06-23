package tdd.seams;

public class Ping7 {

    public boolean ping(String url) {

        try {

            String responseText = new WebParser().parse(url);
            return responseText.contains("Status: OK");

        } catch (Exception e) {
            return false;
        }
    }

}
