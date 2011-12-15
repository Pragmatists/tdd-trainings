package employee;

import static employee.Employee.Type.*;
import static org.junit.Assert.*;

import org.junit.*;

public class EmployeeTest {
	@Test
	public void shouldCalculateSalaryForCEO() {
		// given
		Employee ceo = new Employee();
		ceo.setType(CEO);
		ceo.setBase(100);
		ceo.setCompanyResult(100);
		ceo.setAchievements(1);
		ceo.setAchievementsFactor(2);

		// when
		double salary = ceo.getSalary();

		// then
		assertEquals(100 + 100 * 0.01 + 1 * 2, salary, 1);
	}

	@Test
	public void shouldCalculateSalaryForSales() {
		// given
		Employee sales = new Employee();
		sales.setType(SALES);
		sales.setBase(100);
		sales.setCompanyResult(100);
		sales.setAchievements(1);
		sales.setAchievementsFactor(2);

		// when
		double salary = sales.getSalary();

		// then
		assertEquals(100 + 100 * 0.0000001 + 1 * 2, salary, 1);
	}

	@Test
	public void shouldCalculateSalaryForHR() {
		// given
		Employee hr = new Employee();
		hr.setType(HR);
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
		hr.setType(WORKER);
		hr.setBase(100);

		// when
		double salary = hr.getSalary();

		// then
		assertEquals(100, salary, 1);
	}
}
