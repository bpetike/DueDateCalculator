## Due date calculator

**Implementation language:** __Scala__


### Prerequisites
* Java 1.8
* SBT

To run the tests, type `sbt test`

### Description

This small application calculates due dates based on the submit date of an issue and
the turnaround time estimated for the solution given in hours.

#### Conditions

* Working hours are 9AM to 5PM every working day (Monday through Friday)
* The program does not deal with holidays, which means that a holiday on a Thursday is still
considered as a working day by the program. Also a working Saturday will still be considered as
a nonworking day by the system.
* The turnaround time is given in working hours, which means for example that 2 days are 16
hours. As an example: if a problem was reported at 2:12PM on Tuesday then it is due by
2:12PM on Thursday.
* A problem can only be reported during working hours, which means that all submit date values
fall between 9AM and 5PM. If a submit date is after work hours, the program creates a new submit date
with the next work day 9AM timestamp. After that, the due date is calculated.