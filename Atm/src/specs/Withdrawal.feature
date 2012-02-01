Feature: Atm allows withdrawal of money 

  Scenario: Show error when not enough money in ATM
    Given ATM has 50 PLN
    When a user withdraws 500 PLN
    Then ATM shows error 'Not enough money'

  Scenario: Withdraw successfully
    Given ATM has 50 PLN
    When a user withdraws 50 PLN
    Then user get banknotes (50 PLN)

  Scenario: Withdraw successfully
    Given ATM has (50, 20, 20, 20) PLN
    When a user withdraws 60 PLN
    Then user get banknotes (20, 20, 20) PLN
    
  Scenario: Show error when not enough money on Account
    Given account associated with card '1111' has 10 PLN
    When a user withdraws 50 PLN
    Then ATM shows error 'Not enough money on account'
