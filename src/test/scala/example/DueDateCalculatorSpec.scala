package example
import java.time.LocalDateTime

import org.scalatest.{Matchers, WordSpec}

class DueDateCalculatorSpec extends WordSpec with Matchers {
  "A DueDateCalculator" should {
    "check if submit is on a work day" should  {
      "return true if submit date is a work day" in {
        val submitDate = LocalDateTime.of(2019, 3, 4, 5, 0, 0)
        val calculator = new DueDateCalculator
        calculator.checkWorkDay(submitDate) shouldBe true
      }

      "return false if submit date is a non-work day" in {
        val submitDate = LocalDateTime.of(2019, 3, 3, 5, 0, 0)
        val calculator = new DueDateCalculator
        calculator.checkWorkDay(submitDate) shouldBe false
      }
    }

    "check if submitted issue is in work hours on a work day" should {
      "return true if submit date is in work hours" in {
        val submitDate = LocalDateTime.of(2019, 3, 7, 11, 12, 0)
        val calculator = new DueDateCalculator
        calculator.checkWorkHours(submitDate) shouldBe true
      }

      "return false if submit date is not in work hours on a work day" in {
        val submitDate = LocalDateTime.of(2019, 3, 7, 21, 34, 0)
        val calculator = new DueDateCalculator
        calculator.checkWorkHours(submitDate) shouldBe false
      }

      "return false if submit date is in work hours but on a non-work day" in {
        val submitDate = LocalDateTime.of(2019, 3, 2, 13, 5, 0)
        val calculator = new DueDateCalculator
        calculator.checkWorkHours(submitDate) shouldBe false
      }
    }


    "check if turnaround time is greater than zero" should {
      "return true if the given hours value is greater than zero" in {
        val turnAroundTime = 9
        val calculator = new DueDateCalculator
        calculator.checkTurnAroundTime(turnAroundTime) shouldBe true
      }

      "return false if the given hours value is less than zero" in {
        val turnAroundTime = 9
        val calculator = new DueDateCalculator
        calculator.checkTurnAroundTime(turnAroundTime) shouldBe false
      }

      "return false if turnaround time value is zero" in {
        val turnAroundTime = 0
        val calculator = new DueDateCalculator
        calculator.checkTurnAroundTime(turnAroundTime) shouldBe false
      }
    }

    "calculate due date" in {

    }
  }
}
