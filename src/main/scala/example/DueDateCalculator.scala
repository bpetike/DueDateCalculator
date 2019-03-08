package example
import java.time.{DayOfWeek, LocalDateTime, LocalTime}

class DueDateCalculator {
  private val WORKDAY_BEGIN = LocalTime.of(9, 0, 0)
  private val WORKDAY_END = LocalTime.of(17, 0, 0)
  private val NONWORKDAYS = List(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
  private val LENGTH_OF_WEEK = 7
  private val WORKHOURS_IN_A_WEEK = 40

  def calculateDueDate(submitDate: LocalDateTime,
                       turnAroundTime: Int): LocalDateTime = {
    val afterWorkHours = !checkWorkHours(submitDate) && checkTurnAroundTime(
      turnAroundTime
    )
    if (notDefined(submitDate)) {
      throw new IllegalArgumentException("Submit date not defined")
    } else {
      if (!checkWorkHours(submitDate) && !checkTurnAroundTime(turnAroundTime)) {
        throw new UnsupportedOperationException()
      } else if (afterWorkHours) {
        val newSubmitDate = defineNewSubmitDate(submitDate)
        val (numberOfWeeks, workDays, fractionHours) =
          divideTurnAroundTime(turnAroundTime)
        newSubmitDate
          .plusDays(workDays + LENGTH_OF_WEEK * numberOfWeeks)
          .plusHours(fractionHours)
      } else {
        val (numberOfWeeks, workDays, fractionHours) =
          divideTurnAroundTime(turnAroundTime)
        var result = submitDate
        var addedDays = 0
        while (addedDays < workDays) {
          result = result.plusDays(1)
          if (!(result.getDayOfWeek == DayOfWeek.SATURDAY ||
                result.getDayOfWeek == DayOfWeek.SUNDAY)) {
            addedDays += 1
          }
        }
        submitDate
          .plusDays(addedDays + LENGTH_OF_WEEK * numberOfWeeks)
          .plusHours(fractionHours)
      }
    }
  }

  private[example] def checkWorkDay(submitDate: LocalDateTime): Boolean =
    submitDate.getDayOfWeek match {
      case workday if !NONWORKDAYS.contains(workday) => true
      case _                                         => false
    }

  private[example] def checkWorkHours(submitDate: LocalDateTime): Boolean =
    submitDate match {
      case workdayDate if checkWorkDay(workdayDate) =>
        workdayDate.toLocalTime.isAfter(WORKDAY_BEGIN) && workdayDate.toLocalTime
          .isBefore(WORKDAY_END)
      case _ => false
    }

  private[example] def checkTurnAroundTime(hours: Int): Boolean = hours match {
    case valid if valid > 0 => true
    case _                  => false
  }

  private[example] def defineNewSubmitDate(
    submitDate: LocalDateTime
  ): LocalDateTime = submitDate match {
    case fridayAfterWorkHours
        if !checkWorkHours(fridayAfterWorkHours) && fridayAfterWorkHours.getDayOfWeek == DayOfWeek.FRIDAY =>
      fridayAfterWorkHours.plusDays(3).withHour(9).withMinute(0).withSecond(0)
    case nonWorkDay if !checkWorkDay(submitDate) =>
      val newSubmitDate = nonWorkDay.getDayOfWeek match {
        case DayOfWeek.SATURDAY =>
          nonWorkDay.plusDays(2).withHour(9).withMinute(0).withSecond(0)
        case DayOfWeek.SUNDAY =>
          nonWorkDay.plusDays(1).withHour(9).withMinute(0).withSecond(0)
        case _ => throw new UnsupportedOperationException()
      }
      newSubmitDate
    case workDayAfterWorkHours if !checkWorkHours(workDayAfterWorkHours) =>
      workDayAfterWorkHours.plusDays(1).withHour(9).withMinute(0).withSecond(0)
    case _ => throw new UnsupportedOperationException()
  }

  private def divideTurnAroundTime(turnAroundTime: Int) = {
    val modulus = turnAroundTime % 8
    val numberOfWeeks =
      if (turnAroundTime / WORKHOURS_IN_A_WEEK > 0)
        turnAroundTime / WORKHOURS_IN_A_WEEK
      else 0
    val weeksSubtracted = turnAroundTime - WORKHOURS_IN_A_WEEK * numberOfWeeks
    val workDays =
      if (modulus == 0) (weeksSubtracted / 8) - 1 else weeksSubtracted / 8
    val fractionHours = if (modulus > 0) modulus else 8
    (numberOfWeeks, workDays, fractionHours)
  }

  private def notDefined(dateTime: LocalDateTime): Boolean = {
    if (dateTime == null) true else false
  }
}
