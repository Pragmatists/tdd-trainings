Feature: Atm allows withdrawal of money 

  Scenario: Invalid pin
    Given a card with pin '1234'
    When a user enters pin '9999'
    Then ATM shows error 'Invalid pin'
 
  Scenario: Not enough money
    Given a user with 200 PLN on his account
    When a user withdraws 500 PLN
    Then ATM shows error 'Not enough money'

  Scenario: Successful withdrawal
    Given a user with 200 PLN on his account
    When a user withdraws 500 PLN
    Then ATM shows error 'Not enough money'
 
 