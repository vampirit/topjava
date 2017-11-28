package ru.javawebinar.topjava;


import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.HashMap;
import java.util.Map;

public class TimerTest {
    private static String className;
    private static Map<String, Long> testTimer = new HashMap<>();
    @Rule
    public Stopwatch timer = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            testTimer.put(description.getTestClass().getSimpleName()+"."+description.getMethodName(), nanos/1000000);
            className = description.getTestClass().getSimpleName();
        }
    };

    @AfterClass
    public static void printResultTimer(){
        System.out.println("==========================================================");
        System.out.println("=                   TEST TIMER RESULT                    =");
        System.out.println("=                   "+className+".class              =");
        System.out.println("==========================================================");

        for (Map.Entry<String, Long> test : testTimer.entrySet()) {
            System.out.printf("Method: %s, time: %d ms\n",
                    test.getKey(), test.getValue());
        }
        System.out.println("==========================================================");
    }
}
