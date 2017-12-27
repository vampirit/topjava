package ru.javawebinar.topjava.web.formatters;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (text == null || text.isEmpty())
            return null;
        return LocalDate.parse(text);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        if (object == null)
            return null;
        return object.toString();
    }
}
