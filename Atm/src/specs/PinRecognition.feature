Feature: Atm recognizes pin

  Scenario: Show error when invalid pin
    Given a card with pin '1234'
    When a user enters pin '9999'
    Then ATM shows error 'Invalid pin'

  Scenario: Withdraws money with valid
    Given a card with pin '1234'
    When a user enters pin '1234'
    Then can withdraw
