Feature: 2FA
Verify TFA flow with webservices

  @WS
  @2FA
  @FNG-123
  Scenario Outline: When I have a invalid or empty device token, I should generate a valid device token
    When I send a POST request to the URL "<devicetoken>"
    Then The request will return 200
    And  The response should have deviceToken
    Examples:
      | devicetoken |
      | invalid     |
      |             |

  @WS
  @2FA
  @FNG-124
  Scenario Outline: Verify if my device token is still valid (valid deviceToken => generate the same devicetoken)
    When I send a POST request to the URL "<devicetoken>"
    Then The request will return 200
    And  The response should have deviceToken
    When I send a POST request to the URL
    Then The request will return 200
    And  The response should have the same deviceToken
    Examples:
      | devicetoken |
      | invalid     |

  @WS
  @2FA
  @FNG-125
  Scenario Outline: I connect to my account with a device token which is not trusted, it returns an unauthorized error 401,
  I retrieve the verification token
    When I send a POST request to the URL "<devicetoken>"
    Then The request will return 200
    And  The response should have deviceToken
    When I send a POST request to generate a token
    Then The request will return bad http code 401
    Examples:
      | devicetoken |
      | invalid     |


  @WS
    @2FA
    @FNG-126
  Scenario Outline: Simulate the authenticate two factor like in IHM for a user that already configure his wallet (setup number phone) and set the TFA method in his account
    When I send a POST request to the URL "<devicetoken>"
    Then The request will return 200
    And  The response should have deviceToken
    When I send a POST request to generate a token
    Then The request will return bad http code 401
    When I send a PUT request to verify the verification token
    Then The request will return 200
    And The response should have a token
    When I send a POST request to validate the two factor authentification
    Then The request will return 200
    And The response returns the user info which means that the tfa authentication is successful
    Examples:
      | devicetoken |
      | invalid     |

#  @WS
#  @2FA
#  @FNG-127
#  Scenario: Simulate the adding phone number for a new user and his method of authentification (TFA)
##    When I send a POST request to create an user
##    Then The request will return 200
##    And  The response should have userid
#    When I send a POST request to add a phone number to an user
#    Then The request will return 200
#    When I send a PUT request to add a method of authentification
#    Then The request will return 200















