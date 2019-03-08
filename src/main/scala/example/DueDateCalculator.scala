package example
import java.time.{DayOfWeek, LocalDateTime, LocalTime}

class DueDateCalculator {
  private val WORKDAY_BEGIN = LocalTime.of(9, 0, 0)
  private val WORKDAY_END = LocalTime.of(17, 0, 0)
  private val NONWORKDAYS = List(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

  def calculateDueDate(submitDate: LocalDateTime, turnAroundTime: Int): LocalDateTime = ???

  private[example] def checkWorkDay(submitDate: LocalDateTime): Boolean = submitDate.getDayOfWeek match {
    case workday if !NONWORKDAYS.contains(workday) => true
    case _ => false
  }

  private[example] def checkWorkHours(submitDate: LocalDateTime): Boolean = submitDate match {
    case workdayDate if checkWorkDay(workdayDate) => workdayDate.toLocalTime.isAfter(WORKDAY_BEGIN) && workdayDate.toLocalTime.isBefore(WORKDAY_END)
    case _ => false
  }

  private[example] def checkTurnAroundTime(hours: Int): Boolean = hours match {
    case valid if valid > 0 => true
    case _ => false
  }

  private[example] def defineNewSubmitDate(submitDate: LocalDateTime): LocalDateTime = submitDate match {
    case fridayAfterWorkHours if !checkWorkHours(fridayAfterWorkHours) && fridayAfterWorkHours.getDayOfWeek == DayOfWeek.FRIDAY  =>
      fridayAfterWorkHours.plusDays(3).withHour(9)
    case nonWorkDay if !checkWorkDay(submitDate) =>
      val newSubmitDate = nonWorkDay.getDayOfWeek match {
        case DayOfWeek.SATURDAY => nonWorkDay.plusDays(2).withHour(9)
        case DayOfWeek.SUNDAY => nonWorkDay.plusDays(1).withHour(9)
        case _ => throw new UnsupportedOperationException()
      }
      newSubmitDate
    case workDayAfterWorkHours if !checkWorkHours(workDayAfterWorkHours) =>
      workDayAfterWorkHours.plusDays(1).withHour(9)
    case _ => throw new UnsupportedOperationException()
  }
}
