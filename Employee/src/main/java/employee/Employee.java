package employee;

import java.util.*;

public class Employee {
	private Type type;
	private double base;
	private double achievements;
	private double companyResult;
	private double achievementsFactor;

	enum Type {
		SALES, HR, WORKER, CEO
	}

	private Map<Type, SalaryCalculator> calculators = new HashMap<Employee.Type, SalaryCalculator>();

	public Employee() {
		calculators.put(Type.CEO, new CeoCalculator());
		calculators.put(Type.HR, new HrSalaryCalculator());
		calculators.put(Type.SALES, new SalesCalculator());
		calculators.put(Type.WORKER, new WorkerSalaryCalculator());
	}

	public double getSalary() {
		if (calculators.containsKey(type))
		{
			return calculators.get(type).getSalary(this);
		}
		else
			throw new IllegalStateException("Employee type unspecified");

	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setBase(double base) {
		this.base = base;
	}

	public double getBase() {
		return base;
	}

	public void setAchievements(double achievements) {
		this.achievements = achievements;
	}

	public double getAchievements() {
		return achievements;
	}

	public void setCompanyResult(double companyResult) {
		this.companyResult = companyResult;
	}

	public double getCompanyResult() {
		return companyResult;
	}

	public void setAchievementsFactor(double achievementsFactor) {
		this.achievementsFactor = achievementsFactor;
	}

	public double getAchievementsFactor() {
		return achievementsFactor;
	}
}
