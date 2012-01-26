package employee;

public class SalesCalculator implements SalaryCalculator {

	public double getSalary(Employee employee) {
		return employee.getBase() + employee.getAchievementsFactor() * employee.getAchievements()
		        + employee.getCompanyResult() * 0.0000001;
	}

}
