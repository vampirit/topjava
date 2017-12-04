package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public abstract class TestTimer {
    private static final Logger log = LoggerFactory.getLogger(TestTimer.class);

    private static StringBuilder results = new StringBuilder();
    private static String className;

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            className = description.getTestClass().getSimpleName();
            results.append(result);
            log.info("{}.{} {} ms", className, description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        }
    };

    @AfterClass
    public static void printResult() {
        log.info(
                "\n---------------------------------\n" +
                        className +
                        "\n---------------------------------" +
                        "\nTest                 Duration, ms" +
                        "\n---------------------------------" +
                        results +
                        "\n---------------------------------");
        results.delete(0, results.length());
    }
}
