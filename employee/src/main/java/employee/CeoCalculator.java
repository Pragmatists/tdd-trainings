package employee;

public class CeoCalculator implements SalaryCalculator {

	public double getSalary(Employee employee) {
		return employee.getBase() + employee.getAchievements() * employee.getAchievementsFactor()
		        + employee.getCompanyResult() * 0.01;
	}

}
