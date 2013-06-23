package tdd.veryHumble;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tdd.Employee;
import tdd.EmployeeService;

import tdd.veryHumble.EmployeePresenter;

@RunWith(MockitoJUnitRunner.class)
public class EmployeePresenterTest {

    @Mock
    private EmployeeService employeeService;
    @Mock
    private EmployeeView view;
    
    private EmployeePresenter presenter;

    @Before
    public void setUp() {

        presenter = new EmployeePresenter(employeeService, view);
    }
    
    @Test
    public void shouldUpdateEmployeeOnSave() throws Exception {

        // given:
        when(view.getFirstName()).thenReturn("John");
        when(view.getLastName()).thenReturn("Doe");
        
        // when:
        presenter.onSave();
        
        // then:
        verify(employeeService).save(new Employee("John", "Doe"));
    }

    @Test
    public void shouldDisplayMessageAfterEmployeeHasBeenAdded() throws Exception {
        
        // given:
        
        // when:
        presenter.onSave();
        
        // then:
        verify(view).showEmployeeAddedMessage();
    }

    @Test
    public void shouldPopulateViewOnEdit() throws Exception {

        // given:
        
        // when:
        presenter.onEdit(new Employee("Sherlock", "Holmes"));
        
        // then:
        verify(view).setFirstName("Sherlock");
        verify(view).setLastName("Holmes");
    }


}
