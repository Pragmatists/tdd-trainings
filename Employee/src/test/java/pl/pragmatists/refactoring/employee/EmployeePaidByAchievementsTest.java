package pl.pragmatists.refactoring.employee;

import org.junit.Test;
import pl.pragmatists.refactoring.employee.Employee.Type;

import static org.junit.Assert.assertEquals;



public class EmployeePaidByAchievementsTest {
	@Test
	public void shouldCalculateSalaryForCEO() {
		// given
		Employee ceo = new Employee();
		ceo.setType(Type.CEO);
		ceo.setBase(100);
		ceo.setCompanyResult(50000000);
		ceo.setAchievements(1);
		ceo.setAchievementsFactor(2);

		// when
		double salary = ceo.getSalary();

		// then
		assertEquals(100 + 1 * 2 + 50000000 * 0.01, salary, 0.1);
	}

	@Test
	public void shouldCalculateSalaryForSales() {
		// given
		Employee sales = new Employee();
		sales.setType(Type.SALES);
		sales.setBase(100);
		sales.setCompanyResult(50000000);
		sales.setAchievements(1);
		sales.setAchievementsFactor(2);

		// when
		double salary = sales.getSalary();

		// then
		assertEquals(100 + 50000000 * 0.0000001 + 1 * 2, salary, 0.1);
	}

	@Test
	public void shouldCalculateSalaryForHR() {
		// given
		Employee hr = new Employee();
		hr.setType(Type.HR);
		hr.setBase(100);
		hr.setCompanyResult(100);

		// when
		double salary = hr.getSalary();

		// then
		assertEquals(100 + 100 * 0.0000002, salary, 1);
	}

	@Test
	public void shouldCalculateSalaryForWorker() {
		// given
		Employee hr = new Employee();
		hr.setType(Type.WORKER);
		hr.setBase(100);

		// when
		double salary = hr.getSalary();

		// then
		assertEquals(100, salary, 1);
	}
}
