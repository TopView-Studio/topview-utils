package com.topview.utils.captcha;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Albumen
 * @date 2020/2/6
 */
public class DefaultTokenStorage implements TokenStorage {
    private Map<String, Long> tokenMap = new ConcurrentHashMap<>(256);
    private final static long CYCLE = 60000;
    private long nextCheck = 0;

    @Override
    public void store(String token, long ttl) {
        tokenMap.put(token, System.currentTimeMillis() + ttl);
        checkValid();
    }

    @Override
    public boolean verify(String token) {
        checkValid();
        if (tokenMap.containsKey(token)) {
            if (isValid(token)) {
                tokenMap.remove(token);
                return true;
            } else {
                tokenMap.remove(token);
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isValid(String token) {
        return System.currentTimeMillis() < tokenMap.get(token);
    }

    private synchronized void checkValid() {
        if (System.currentTimeMillis() > nextCheck) {
            CompletableFuture.runAsync(() -> {
                tokenMap.entrySet().removeIf(entry -> System.currentTimeMillis() > entry.getValue());
            });
            nextCheck = nextCheck + CYCLE;
        }
    }
}
