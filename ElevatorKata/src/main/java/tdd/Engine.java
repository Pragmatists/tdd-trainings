package tdd;

public interface Engine {

    public void up() throws MalfunctionException;
    
    public void down() throws MalfunctionException;
    
    public void stop() throws MalfunctionException;

    public class MalfunctionException extends RuntimeException {
        private static final long serialVersionUID = 2072027119661881675L;
    }
    
}
