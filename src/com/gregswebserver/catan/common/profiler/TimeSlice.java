package com.gregswebserver.catan.common.profiler;

import java.util.HashMap;

/**
 * Created by Greg on 11/10/2014.
 * A slice of the time pie
 */
public class TimeSlice {

    private final String name;
    private final HashMap<String, TimeSlice> slices;
    private final long creationTime;
    private long time;

    public TimeSlice(String name) {
        this.name = name;
        slices = new HashMap<>();
        creationTime = System.nanoTime();
    }

    public void addChild(TimeSlice child) {
        if (child != null)
            slices.put(child.name, child);
    }

    public void markTime() {
        time = System.nanoTime() - creationTime;
    }

    public long getTime() {
        return time;
    }

    public HashMap<String, String> percents() {
        long total = 0;
        HashMap<String, String> out = new HashMap<>();
        for (String s : slices.keySet()) {
            long subTime = slices.get(s).getTime();
            total += subTime;
            out.put(s, formatPercent(((double) subTime) / time));
        }
        out.put("undefined", formatPercent(((double) time - total) / time));
        return out;
    }

    public HashMap<String, String> times() {
        HashMap<String, String> out = new HashMap<>();
        int total = 0;
        for (String s : slices.keySet()) {
            total += slices.get(s).getTime();
            out.put(s, formatTime(slices.get(s).getTime()));
        }
        out.put("undefined", formatTime(time - total));
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

    public String print(int depth, int current) {
        if (current > depth)
            return "";
        StringBuilder out = new StringBuilder();
        for(String name : slices.keySet()) {
            for (int i = 0; i < current; i++)
                out.append('\t');
            out.append(times().get(name));
            out.append('\t');
            out.append(percents().get(name));
            out.append('\t');
            out.append(name);
            out.append('\n');
            out.append(slices.get(name).print(depth, current+1));
        }
        if (slices.size() > 0) {
            for (int i = 0; i < current; i++)
                out.append('\t');
            out.append(times().get("undefined"));
            out.append('\t');
            out.append(percents().get("undefined"));
            out.append('\t');
            out.append("undefined");
            out.append('\n');
        }
        return out.toString();
    }
}
