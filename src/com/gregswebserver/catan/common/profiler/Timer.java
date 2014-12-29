package com.gregswebserver.catan.common.profiler;

import java.util.HashMap;

/**
 * Created by Greg on 11/10/2014.
 * Timing class to profile code execution time.
 */
public class Timer {

    private TimeSlice rootSlice;

    public Timer() {
        rootSlice = new TimeSlice("root");
    }

    public void addTime(String complete, long time) {
        if (complete.equals(""))
            rootSlice.addTime(new String[]{"root"}, 0, time);
        rootSlice.addTime(complete.split("\\."), 1, time);
    }

    public String toString() {
        String out = "TimeSlice Name | Time | Percentage\n";
        out += "----------------------------------\n";
        HashMap<String, String> times = rootSlice.times();
        HashMap<String, String> percents = rootSlice.percentages();
        for (String name : times.keySet()) {
            out += name + " | " + times.get(name) + " | " + percents.get(name) + "\n";
        }
        return out;
    }
}
