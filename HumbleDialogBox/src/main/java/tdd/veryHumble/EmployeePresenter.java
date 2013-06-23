package tdd.veryHumble;

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
    
    public void onSave() {
        
        save(view.getFirstName(), view.getLastName());
        view.showEmployeeAddedMessage();
    }

    public void onEdit(Employee employee) {
        view.setFirstName(employee.getFirstName());
        view.setLastName(employee.getLastName());
    }

    private void save(String firstName, String lastName) {
        employeeService.save(new Employee(firstName, lastName));
    }

}
