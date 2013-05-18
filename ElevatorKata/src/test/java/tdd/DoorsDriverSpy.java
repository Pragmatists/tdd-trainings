package tdd;

public class DoorsDriverSpy implements DoorsDriver {

    private DoorsListener listener;
    private boolean doorClosingHasBeenRequested;

    public boolean doorClosingHasBeenRequested() {
        return doorClosingHasBeenRequested;
    }

    @Override
    public void closeDoors(DoorsListener listener) {
        this.listener = listener;
        doorClosingHasBeenRequested = true;
    }

    public void doorsClosed() {
        this.listener.doorsClosed();
    }
}