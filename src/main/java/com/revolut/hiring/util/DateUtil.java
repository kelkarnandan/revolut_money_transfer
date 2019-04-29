package com.revolut.hiring.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import static java.util.TimeZone.getTimeZone;

public class DateUtil {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final ZoneOffset DEFAULT_TIMEZONE = ZoneOffset.UTC;

    public static final String getDateString(final Date date) {
        final Instant instant = date.toInstant();
        final LocalDateTime ldt = instant.atOffset(DEFAULT_TIMEZONE).toLocalDateTime();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return ldt.format(fmt);
    }

    public static final String getDateString(final Date date, final String format) {
        final Instant instant = date.toInstant();
        final LocalDateTime ldt = instant.atOffset(DEFAULT_TIMEZONE).toLocalDateTime();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        return ldt.format(fmt);
    }

    public static final String getDateString(final Date date, final String format, final ZoneOffset zone) {
        final Instant instant = date.toInstant();
        final LocalDateTime ldt = instant.atOffset(zone).toLocalDateTime();
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);
        return ldt.format(fmt);
    }

    public static final Date getDate(final String dateString) {
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        final LocalDate localDate = LocalDate.parse(dateString, fmt);
        return Date.from(localDate.atStartOfDay(DEFAULT_TIMEZONE).toInstant());
    }

    public static final Date getDate(final String dateString, final String dateFormat) {
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormat);
        final LocalDate localDate = LocalDate.parse(dateString, fmt);
        return Date.from(localDate.atStartOfDay(DEFAULT_TIMEZONE).toInstant());
    }

    public static final Date getDate(final String dateString, final String dateFormat, final ZoneOffset zone) {
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormat);
        final LocalDate localDate = LocalDate.parse(dateString, fmt);
        return Date.from(localDate.atStartOfDay(zone).toInstant());
    }

    public static String getDateString(final Long timeValue) {
        final LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeValue), DEFAULT_TIMEZONE);
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return fmt.format(ldt);
    }

    public static String getDateString(final Long timeValue, final String dateFormat) {
        final LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeValue), DEFAULT_TIMEZONE);
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormat);
        return fmt.format(ldt);
    }

    public static String getDateString(final Long timeValue, final String dateFormat, String timeZoneName) {
        final TimeZone zone = getTimeZone(timeZoneName);
        final LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeValue), zone.toZoneId());
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(dateFormat);
        return fmt.format(ldt);
    }

    public static final String getDateString(final Long timeValue, final ZoneId timeZone) {
        final LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeValue), timeZone);
        final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        return fmt.format(ldt);
    }
}
