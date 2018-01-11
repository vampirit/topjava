package ru.javawebinar.topjava.util;

import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class ErrorUtil {

    public static String resultToStringInHtml(BindingResult result) {
        StringJoiner joiner = new StringJoiner("<br>");
        result.getFieldErrors().forEach(
                fe -> {
                    String msg = fe.getDefaultMessage();
                    if (msg != null && !msg.startsWith(fe.getField())) {
                        msg = fe.getField() + ' ' + msg;
                    }
                    joiner.add(msg);
                });
        return joiner.toString();
    }
}
