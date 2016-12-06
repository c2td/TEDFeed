package com.application.pm.tedfeed;

import android.support.v4.util.LruCache;

/* A helper class for image caching */
class Cache {

    private static Cache instance;
    private LruCache<Object, Object> lru;

    private Cache() {
        lru = new LruCache<>(1024);
    }

    static Cache getInstance() {

        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    LruCache<Object, Object> getLru() {
        return lru;
    }
}