Feature: Atm allows withdrawal of money 

  Scenario: 
    Given a card identified by number '1111 2222 3333 4444' and pin '1234'
    When a user inserts card and enters pin '9999'
    Then ATM shows error 'Invalid pin'
 
