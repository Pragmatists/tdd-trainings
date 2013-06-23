package tdd.lessHumble;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tdd.Employee;
import tdd.EmployeeService;

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

        // when:
        presenter.onSave("John", "Doe");
        
        // then:
        verify(employeeService).save(new Employee("John", "Doe"));
    }

    @Test
    public void shouldDisplayMessageAfterEmployeeHasBeenAdded() throws Exception {
        
        // given:
        
        // when:
        presenter.onSave("John", "Doe");
        
        // then:
        verify(view).showEmployeeAddedMessage();
    }

    @Test
    public void shouldPopulateViewOnEdit() throws Exception {

        // given:
        Employee employee = new Employee("Sherlock", "Holmes");
        
        // when:
        presenter.onEdit(employee);
        
        // then:
        verify(view).populateEmployee(employee);
    }


}
