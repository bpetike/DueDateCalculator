package example
import java.time.LocalDateTime

import org.scalatest.{Matchers, WordSpec}

class DueDateCalculatorSpec extends WordSpec with Matchers {
  "A DueDateCalculator" should {
    "check if submit is on a work day" in {
      "return true if submit date is a work day" in {
        val submitDate = new LocalDateTime(2019, 3, 4, 5, 0, 0)
        val calculator = new DueDateCalculator
        calculator.checkWorkDay(submitDate) shouldBe true
      }
    }

    "check if submitted issue is in work hours on a workday" in {

    }


    "check if turnaround time is greater than zero" in {

    }

    "calculate due date" in {

    }
  }
}
