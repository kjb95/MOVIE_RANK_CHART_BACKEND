package movierankchart.common.utils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class DateUtils {
    public static String localDateToString(LocalDate date, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTimeFormatter.format(date);
    }

    public static LocalDate stringToLocalDate(String dateString, String format) throws ParseException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(dateString, dateTimeFormatter);
    }

    public static List<LocalDate> getDatesRange(LocalDate startDate, long count, int interval) {
        return LongStream.range(0, count)
                .mapToObj(n -> startDate.plusDays(n * interval))
                .collect(Collectors.toList());
    }
}
