package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        if (text == null || text.isEmpty())
            return null;
        return LocalTime.parse(text);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        if (object == null)
            return null;
        return object.toString();
    }
}
