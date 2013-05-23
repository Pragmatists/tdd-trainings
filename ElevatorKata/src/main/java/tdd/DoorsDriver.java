package tdd;

public interface DoorsDriver {

    public interface DoorsListener {
        public void doorsClosed();
    }

    public void closeDoors(DoorsListener elevator);

}