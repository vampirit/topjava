package ru.javawebinar.topjava;


import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class TimerTest extends Stopwatch {
    private static final Logger LOG = getLogger(TimerTest.class);
    private Map<String, Long> testTimer = new HashMap<>();

    @Override
    protected void finished(long nanos, Description description) {
        LOG.info("{} - {} ms", description.getMethodName(), nanos/1000000);
        testTimer.put(description.getTestClass().getSimpleName()+"."+description.getMethodName(), nanos/1000000);
    }

    public String getResult(){
        return testTimer.entrySet().stream()
                .map((entry) -> String.format("%s - %s ms", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }
}
