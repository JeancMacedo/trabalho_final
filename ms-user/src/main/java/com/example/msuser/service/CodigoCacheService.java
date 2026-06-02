package com.example.msuser.service;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class CodigoCacheService {
    private static class Entry { String code; Instant expiresAt; }

    private final Map<String, Entry> cache = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public CodigoCacheService() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.MINUTES);
    }

    public String generateCode(String email) {
        int val = 100000 + random.nextInt(900000);
        String code = String.valueOf(val);
        Entry e = new Entry();
        e.code = code;
        e.expiresAt = Instant.now().plusSeconds(5 * 60);
        cache.put(email.toLowerCase(), e);
        return code;
    }

    public String getCode(String email) {
        Entry e = cache.get(email.toLowerCase());
        if (e == null) return null;
        if (Instant.now().isAfter(e.expiresAt)) {
            cache.remove(email.toLowerCase());
            return null;
        }
        return e.code;
    }

    private void cleanup() {
        Instant now = Instant.now();
        cache.entrySet().removeIf(en -> en.getValue().expiresAt.isBefore(now));
    }
}
