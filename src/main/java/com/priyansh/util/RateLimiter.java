package com.priyansh.util;

import java.util.LinkedList;
import java.util.Queue;

public class RateLimiter {

    private static final Queue<Long> timestamps = new LinkedList<>();

    public static synchronized boolean allowRequest() {
        long now = System.currentTimeMillis();

        while (!timestamps.isEmpty() && now - timestamps.peek() > 60000) {
            timestamps.poll();
        }

        if (timestamps.size() >= 5) {
            return false;
        }

        timestamps.add(now);
        return true;
    }
}

