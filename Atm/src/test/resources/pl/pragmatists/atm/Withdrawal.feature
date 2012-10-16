Feature: Cash Withdrawal
	Scenario: Successful withdrawal from an account
		Given my account has been credited with $100
		When I withdraw $20
		Then $20 should be dispensed
		And the balance of my account should be $80
	
	Scenario: User tries to withdraw more than their balance
		Given my account has been credited with $100	
		When I withdraw $200
		Then nothing should be dispensed
		And I should be told that I have insufficient funds in my account
		