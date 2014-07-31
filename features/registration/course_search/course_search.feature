@nightly @red_team

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
    Then courses containing  "<expected>" text options appear
  Examples:
    | text                    | expected                                                 |
    | ENGL101 CHEM231         | ENGL101, ENGL101H, ENGL101M, ENGL101S, ENGL101X, CHEM231 |
    | PHYS374 CHEM231         | PHYS374, CHEM231                                         |
    | ENGL211 BSCI120 ENGL212 | ENGL211, BSCI120, ENGL212                                |

  Scenario Outline: CR 19.2.3 Search by Subject Code
    When I search for a course with "<text>" text option
    Then courses containing  "<expected>" text option appears
  Examples:
    | text     | expected |
    | ENGL     | ENGL     |
    | BSCI     | BSCI     |

  Scenario Outline: CR 19.2.4 Search by Multiple Subject Code
    When I search for a course with "<text>" text option
    Then courses containing  "<expected>" text options appear
  Examples:
    | text       | expected  |
    | ENGL BSCI  | ENGL, BSCI |
    | CHEM HIST  | CHEM, HIST |

  Scenario Outline: CR 19.2.9 Search by Keyword
    When I search for a course with "<text>" text option
    Then courses containing  "<expected>" text options appear
  Examples:
    | text    | expected                                                                                            |
    | Atomic  | PHYS721, CHEM682, PHYS420, CHEM403, PHYS622                                                         |
    | Organic | CHEM640, CHEM491, CHEM242, CHEM105, CHEM231, CHEM889D, CHEM641, CHEM241, CHEM889G, CHEM232, CHEM460 |
