package pl.pragmatists.refactoring.employee;

public class Employee {
	private Type type;
	private double base;
	private double achievements;
	private double companyResult;
	private double achievementsFactor;

	enum Type {
		SALES, HR, WORKER, CEO
	}

	public double getSalary() {
		switch (type) {
		case SALES:
			return getBase() + getAchievementsFactor() * getAchievements()
					+ getCompanyResult() * 0.0000001;
		case HR:
			return getBase() + getCompanyResult() * 0.0000002;
		case WORKER:
			return getBase();
		case CEO:
			return getBase() + getAchievements() * getAchievementsFactor()
					+ getCompanyResult() * 0.01;
		default:
			throw new IllegalStateException("Employee type unspecified");
		}
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
