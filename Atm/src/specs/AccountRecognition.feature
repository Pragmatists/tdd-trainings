Feature: Atm recognizes accounts

  Scenario: Show error when no Account
    Given no existing Accounts
    When a user uses card '1111'
    Then ATM shows error 'No Account'
    
  Scenario: Withdraw succesfully when account exists
    Given account for card '1111' with balance of 200 PLN
    When a user withdraws 100 PLN with card '1111'
    Then user gets banknotes (100) PLN