package com.gregswebserver.catan.common.profiler;

import java.util.HashMap;

/**
 * Created by Greg on 11/10/2014.
 * A slice of the time pie
 */
public class TimeSlice {

    private String name;
    private HashMap<String, TimeSlice> slices;
    private long time;

    public TimeSlice(String name) {
        this.name = name;
        slices = new HashMap<>();
    }

    public void addTime(String[] path, int depth, long time) {
        if (depth == path.length)
            this.time += time;
        else {
            String curr = path[depth];
            TimeSlice child = slices.get(curr);
            if (child == null) {
                child = new TimeSlice(curr);
                slices.put(curr, child);
            }
            child.addTime(path, depth + 1, time);
        }
    }

    public long getTime() {
        long total = time;
        for (TimeSlice t : slices.values())
            total += t.getTime();
        return total;
    }

    public HashMap<String, String> percentages() {
        long total = getTime();
        HashMap<String, String> out = new HashMap<>();
        for (String s : slices.keySet()) {
            long subTime = slices.get(s).getTime();
            out.put(s, formatPercent(((double) subTime) / total));
        }
        out.put("undefined", formatPercent(((double) time) / total));
        return out;
    }

    public HashMap<String, String> times() {
        HashMap<String, String> out = new HashMap<>();
        for (String s : slices.keySet())
            out.put(s, formatTime(slices.get(s).getTime()));
        out.put("undefined", formatTime(time));
        return out;
    }

    private String formatTime(long in) {
        if (in < 1e3)
            return in + "ns";
        if (in < 1e6)
            return formatDouble(in / 1e3, 3) + "us";
        if (in < 1e9)
            return formatDouble(in / 1e6, 3) + "ms";
        return formatDouble(in / 1e9, 3) + "s";
    }

    private String formatPercent(double in) {
        in *= 100;
        return formatDouble(in, 2) + "%";
    }

    private String formatDouble(double in, int places) {
        int whole = (int) in;
        double decimal = in - whole;
        String part = ("" + decimal);
        String out = "";
        out += whole;
        if (places > 0) {
            if (Math.abs(decimal) > 0.1 * places)
                out += part.substring(1, Math.min(places + 2, part.length()));
        }
        return out;
    }
}
