package io.github.bpetike

import java.time.{DayOfWeek, Instant, LocalDateTime, LocalTime, ZoneId, ZoneOffset}

class DueDateCalculator {
  private val WORKDAY_BEGIN = LocalTime.of(9, 0, 0)
  private val WORKDAY_END = LocalTime.of(17, 0, 0)
  private val NONWORKDAYS = List(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)
  private val LENGTH_OF_WEEK = 7
  private val WORKHOURS_IN_A_WEEK = 40

  def calculateDueDate(submitDateInEpochSeconds: Long,
                       turnAroundTime: Int): Long = {
    val submitDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(submitDateInEpochSeconds), ZoneId.of("GMT"))
    submitDate match {
      case value =>
        val afterWorkHours = !checkWorkHours(value) && checkTurnAroundTime(turnAroundTime)
        if (afterWorkHours) {
          getSubmitDateAfterWorkHours(value, turnAroundTime).toEpochSecond(ZoneOffset.UTC)
        } else {
          getSubmitDateInWorkHours(value, turnAroundTime).toEpochSecond(ZoneOffset.UTC)
        }
      case _ => -1
    }
  }

  private[bpetike] def checkWorkDay(submitDate: LocalDateTime): Boolean =
    submitDate.getDayOfWeek match {
      case workday if !NONWORKDAYS.contains(workday) => true
      case _                                         => false
    }

  private[bpetike] def checkWorkHours(submitDate: LocalDateTime): Boolean =
    submitDate match {
      case workdayDate if checkWorkDay(workdayDate) =>
        workdayDate.toLocalTime.isAfter(WORKDAY_BEGIN) && workdayDate.toLocalTime
          .isBefore(WORKDAY_END)
      case _ => false
    }

  private[bpetike] def checkTurnAroundTime(hours: Int): Boolean = hours match {
    case valid if valid > 0 => true
    case _                  => false
  }

  private[bpetike] def defineNewSubmitDate(
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

  private def getNumberOfWorkDays(turnAroundTime: Int,
                                  numberOfWeeks: Int,
                                  modulus: Int): Int = {
    val weeksSubtracted = turnAroundTime - WORKHOURS_IN_A_WEEK * numberOfWeeks
    if (modulus == 0) (weeksSubtracted / 8) - 1 else weeksSubtracted / 8
  }

  private def getNumberOfWeeks(turnAroundTime: Int) = {
    if (turnAroundTime / WORKHOURS_IN_A_WEEK > 0)
      turnAroundTime / WORKHOURS_IN_A_WEEK
    else 0
  }

  private def getSubmitDateAfterWorkHours(
    submitDate: LocalDateTime,
    turnAroundTime: Int
  ): LocalDateTime = {
    val modulus = turnAroundTime % 8
    val newSubmitDate = defineNewSubmitDate(submitDate)
    val numberOfWeeks = getNumberOfWeeks(turnAroundTime)
    val workDays = getNumberOfWorkDays(turnAroundTime, numberOfWeeks, modulus)
    val fractionHours = if (modulus > 0) modulus else 8
    newSubmitDate
      .plusDays(workDays + LENGTH_OF_WEEK * numberOfWeeks)
      .plusHours(fractionHours)
  }

  private def getSubmitDateInWorkHours(submitDate: LocalDateTime,
                                       turnAroundTime: Int): LocalDateTime = {
    val modulus = turnAroundTime % 8
    val numberOfWeeks = getNumberOfWeeks(turnAroundTime)
    val workDays = getNumberOfWorkDays(turnAroundTime, numberOfWeeks, modulus)
    val fractionHours = if (modulus > 0) modulus else 8
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
