@draft @red_team

Feature: REG.Course Search
  CR 19.2 - As a student I want to search for courses so that I can view
            courses to potentially add them to my Reg Cart (Large format)

  Background:
    Given I am logged in as a Student

  #KSENROLL-13740 (this whole feature)
  Scenario Outline: CR 19.2.1 Search by Single Course Code
    When I search for a course with "<text>" text option
    Then courses containing  "<expected>" text option appears
  Examples:
    | text     | expected |
    | ENGL202  | ENGL202  |
    | BSCI120  | BSCI120  |

  Scenario Outline: CR 19.2.2 Search by Multiple Course Code
    When I search for a course with "<text>" text option
    Then "<expected_courses>" and courses matching at least one "<expected_component>" are returned
  Examples:
    | text                    |expected_courses                 | expected_component                             |
    | ENGL101 CHEM231         | ENGL101, CHEM231                | none                                           |
    | TEST345 CHEM231         | CHEM231                         | none                                           |
    | ENGL101 BSCI120 ENGL212 | ENGL101, BSCI120, ENGL212       | none                                           |

  Scenario Outline: CR 19.2.3 Search by Subject Code
    When I search for a course with "<text>" text option
    Then courses containing  "<expected>" text option appears
  Examples:
    | text     | expected |
    | ENGL     | ENGL     |
    | BSCI     | BSCI     |

  Scenario Outline: CR 19.2.4 Search by Single Course Level
    When I search for a course with "<text>" text option
    Then courses containing  "<expected>" text option appears
  Examples:
    | text   | expected |
    | ENGL2  | ENGL2    |
    | CHEM3  | CHEM3    |

