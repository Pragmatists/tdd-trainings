package employee;

public class WorkerSalaryCalculator implements SalaryCalculator {

	@Override
	public double getSalary(Employee employee) {
		return employee.getBase();
	}

}
