package employee;

public class HrSalaryCalculator implements SalaryCalculator {

	@Override
	public double getSalary(Employee employee) {
		return employee.getBase() + employee.getCompanyResult() * 0.0000002;
	}

}
