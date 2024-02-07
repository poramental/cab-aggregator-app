Feature: Driver accepts a ride

  Scenario: Correct driver tries to accept the ride
    When the driver with id 1 attempts to accept the ride
    Then the response should have status code 200
    And the driver id in the response should be 1

  Scenario: Incorrect driver tries to accept the ride
    When the driver with id 1 attempts to accept a ride that is waiting for another driver
    Then the response should have status code 400
    And the error message in the response should be "Ride is waiting for another driver response."

  Scenario: Driver with isInRide=true tries to accept the ride
    When the driver with id 2 attempts to accept the ride
    Then the response should have status code 409
    And the error message in the response should be "Driver already have a ride."

  Scenario: Driver tries to accept a ride already accepted by another driver
    When the driver with id 3 attempts to accept the ride that already accepted by another driver
    Then the response should have status code 409
    And the error message in the response should be "Ride with id: 'c86f6a77-6a12-4f1e-9d1c-7fc5d9ebe9c7' already have a driver."