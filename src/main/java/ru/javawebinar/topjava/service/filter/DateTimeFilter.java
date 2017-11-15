package ru.javawebinar.topjava.service.filter;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeFilter {
    private final LocalDate startDay;
    private final LocalDate endDay;
    private final LocalTime startTime;
    private final LocalTime endTime;

    private DateTimeFilter(LocalDate startDay, LocalDate endDay, LocalTime startTime, LocalTime endTime) {
        this.startDay = startDay;
        this.endDay = endDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public static class DateTimeFilterBuilder {
        private LocalDate startDay;
        private LocalDate endDay;
        private LocalTime startTime;
        private LocalTime endTime;

        public DateTimeFilterBuilder() {
        }

        public DateTimeFilterBuilder setStartDay(String startDay){
            if (startDay == null || startDay.isEmpty())
                this.startDay = LocalDate.MIN;
            else
                this.startDay = LocalDate.parse(startDay);
            return this;
        }

        public DateTimeFilterBuilder setEndDay(String endDay){
            if (endDay == null || endDay.isEmpty())
                this.endDay = LocalDate.MAX;
            else
                this.endDay = LocalDate.parse(endDay);
            return this;
        }

        public DateTimeFilterBuilder setStartTime(String startTime){
            if (startTime == null || startTime.isEmpty())
                this.startTime = LocalTime.MIN;
            else
                this.startTime = LocalTime.parse(startTime);
            return this;
        }

        public DateTimeFilterBuilder setEndTime(String endTime){
            if (endTime == null || endTime.isEmpty())
                this.endTime = LocalTime.MAX;
            else
                this.endTime = LocalTime.parse(endTime);
            return this;
        }

        public DateTimeFilter build(){
            return new DateTimeFilter(startDay, endDay, startTime, endTime);
        }
    }
}
