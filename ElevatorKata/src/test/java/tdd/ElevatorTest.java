package tdd;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import tdd.Elevator.Status;

public class ElevatorTest {

    private Elevator elevator;
    
    private DoorsDriverSpy doorsDriver;

    @Before
    public void setUp() {
        doorsDriver = new DoorsDriverSpy();
        elevator = new Elevator(doorsDriver);
    }
    
    @Test
    public void shouldStartOnZeroFloor() throws Exception {

        // given:
        // when:
        int floor = elevator.currentFloor();
        
        // then:
        Assert.assertEquals(0, floor);
    }

    @Test
    public void shouldStartInAwaitingState() throws Exception {
        
        // given:
        // when:
        Status status = elevator.status();
        
        // then:
        Assert.assertEquals(Status.AWAITING, status);
    }
 
    @Test
    public void shouldCloseDoorsAfterPushingAnyButton() throws Exception {

        // given:
        // when:
        elevator.pushButton(3);
        
        // then:
        assertThat(doorsDriver.doorClosingHasBeenRequested()).isTrue();
    }

    @Test
    public void shouldNotCloseDoorsAfterPushingButtonWithCurrentFloor() throws Exception {
        
        // given:
        // when:
        elevator.pushButton(0);
        
        // then:
        assertThat(doorsDriver.doorClosingHasBeenRequested()).isFalse();
    }
    
    @Test
    public void shouldStartGoingUpAfterDoorsHaveBeenClosed() throws Exception {
        
        // given:
        // when:
        elevator.pushButton(3);
        doorsDriver.doorsClosed();
        
        // then:
        assertThat(elevator.status()).isEqualTo(Status.GOING_UP);
    }
    
    @Test
    public void shouldNotStartGoingUpUnlessDoorsHaveBeenClosed() throws Exception {
        
        // given:
        // when:
        elevator.pushButton(3);
        
        // then:
        assertThat(elevator.status()).isEqualTo(Status.AWAITING);
    }

    @Test
    public void shouldStartGoingDownAfterDoorsHaveBeenClosed() throws Exception {
        
        // given:
        elevator = new Elevator(3, doorsDriver);
        
        // when:
        elevator.pushButton(1);
        doorsDriver.doorsClosed();
        
        // then:
        assertThat(elevator.status()).isEqualTo(Status.GOING_DOWN);
    }
    
}
