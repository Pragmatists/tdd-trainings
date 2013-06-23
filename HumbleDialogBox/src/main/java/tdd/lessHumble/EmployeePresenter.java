package tdd.lessHumble;

import tdd.Employee;
import tdd.EmployeeService;

public class EmployeePresenter {

    private final EmployeeView view;
    private final EmployeeService employeeService;

    public EmployeePresenter(EmployeeService employeeService, EmployeeView view) {
        this.employeeService = employeeService;
        this.view = view;
    }

    public EmployeePresenter(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.view = new SwingEmployeeView(this);
    }
    
    public void onSave(String firstName, String lastName) {
        
        save(firstName, lastName);
        view.showEmployeeAddedMessage();
    }

    public void onEdit(Employee employee) {
        view.populateEmployee(employee);
    }

    private void save(String firstName, String lastName) {
        employeeService.save(new Employee(firstName, lastName));
    }

}
