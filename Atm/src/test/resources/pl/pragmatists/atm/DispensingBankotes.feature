Feature: Banknotes are dispensed in a smart way

	Scenario: Single banknote matches amount
		Given Atm has banknotes:
			| value | count |
			| $100  |     1 |
		When $100 is to be dispensed
		Then following banknotes are dispensed:
			| value | count |
			| $100  |     1 |
		
	Scenario: Two banknotes match amount
		Given Atm has banknotes:
			| value | count |
			| $50   |     2 |
		When $100 is to be dispensed
		Then following banknotes are dispensed:
			| value | count |
			| $50   |     2 |
			