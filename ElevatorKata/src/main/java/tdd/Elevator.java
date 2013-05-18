package tdd;

import static tdd.Elevator.Status.AWAITING;
import tdd.DoorsDriver.DoorsListener;

public class Elevator implements DoorsListener {

    public enum Status{
        GOING_UP, GOING_DOWN, AWAITING, NEED_MAINTENENCE;
    }

    private DoorsDriver doorsDriver;

    private Status status = AWAITING;

    private int currentFloor;
    private int requestedFloor;
    
    public Elevator(DoorsDriver doorsDriver) {
        this(0, doorsDriver);
    }

    public Elevator(int startingFloor, DoorsDriver doorsDriver) {
        this.currentFloor = startingFloor;
        this.doorsDriver = doorsDriver;
    }

    public int currentFloor() {
        return currentFloor;
    }
    
    public void pushButton(int floorNumber){
        
        this.requestedFloor = floorNumber;
        if(floorNumber != currentFloor())
            doorsDriver.closeDoors(this);
    }

    public Status status(){
        return status;
    }

    @Override
    public void doorsClosed() {
        
        if(currentFloor < requestedFloor){
            up();
        }

        if(currentFloor > requestedFloor){
            down();
        }
    }

    private void down() {
        status = Status.GOING_DOWN;
    }

    private void up() {
        status = Status.GOING_UP;
    }

}
