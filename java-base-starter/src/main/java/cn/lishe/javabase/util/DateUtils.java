package cn.lishe.javabase.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 类功能描述
 *
 * @author YeJin
 * @date 2019/10/25 14:36
 */
public class DateUtils {

    /**
     * Transfer a date to localDate
     *
     * @param date date to transfer
     * @return the localDate of date
     */
    public static LocalDate dateToLocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Transfer a date to localDateTime
     *
     * @param date date to transfer
     * @return the localDateTime of date
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Transfer a localDate to date
     *
     * @param localDate localDate to transfer
     * @return the date of localDate
     */
    public static Date localDateToDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * Transfer a localDateTime to date
     *
     * @param localDateTime localDateTime
     * @return date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Predicating that is it weekend the date
     *
     * @param date date
     * @return boolean
     */
    public static boolean isWeekEnd(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        return dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY;
    }

    /**
     * A string date to localDate
     *
     * @param date a string date to transfer
     * @return LocalDate
     */
    public static LocalDate stringToLocalDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
