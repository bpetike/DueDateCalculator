package example
import java.time.{DayOfWeek, LocalDateTime, LocalTime}

class DueDateCalculator {
  private val WORKDAY_BEGIN = new LocalTime(9, 0, 0)
  private val WORKDAY_END = new LocalTime(9, 0, 0)
  private val NONWORKDAYS = List(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)

  def calculateDueDate(submitDate: LocalDateTime, turnAroundTime: Int): LocalDateTime = ???

  private[example] def checkWorkDay(submitDate: LocalDateTime): Boolean = ???

  private[example] def checkWorkHours(submitDate: LocalDateTime): Boolean = ???

  private[example] def defineNewSubmitDate(submitDate: LocalDateTime): LocalDateTime = ???
}
