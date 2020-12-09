@api
Feature: Api tests

  Scenario Outline: Create project and check that project exist /projects
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Open user account page
    Then Save account page api key to the environment variable
    Then Check that pathmind API return project with name <project name>
    Then Check API /projects '<project name>' archived 'false'

    Examples:
      | project name   |
      | ApiTestProject |

  Scenario Outline: Create project and check API is archived
    Given Login to the pathmind
    When Open projects page
    When Click create new project button
    When Input name of the new project <project name> and click Create project button
    When Open projects page
    When Click ApiTestProject project archive/unarchive button
    When In confirmation dialog click in 'Archive' button
    When Open user account page
    Then Save account page api key to the environment variable
    Then Check API /projects '<project name>' archived 'true'

    Examples:
      | project name   |
      | ApiTestProject |

  Scenario Outline: /signup create new user verify email and login
    Given API create new user with '<firstName>', '<lastName>', '<password>' and status code '201'
    When Get email and verify user email
    When Open pathmind page
    Then Login with new user email and <password>
    Then Check that user <firstName> <lastName> successfully logged in

    Examples:
      | firstName    | lastName    | password |
      | ApiFirstName | ApiLastName | Qa1234   |

  Scenario: /signup negative case create new user with 5 chars password
    Given API create new user with 'ApiFirstName', 'ApiLastName', 'Qa123' and status code '400'

  Scenario: /signup negative case create new user with no lowercase password
    Given API create new user with 'ApiFirstName', 'ApiLastName', 'QA1234' and status code '400'

  Scenario: /signup negative case create new user with no uppercase password
    Given API create new user with 'ApiFirstName', 'ApiLastName', 'qa1234' and status code '400'

  Scenario: /signup negative case create new user with 251 chars firstName
    Given API create new user with 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretiumpp', 'ApiLastName', 'qa1234' and status code '400'

  Scenario: /signup negative case create new user with 251 chars lastName
    Given API create new user with 'ApiFirstName', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretiumpp', 'qa1234' and status code '400'
