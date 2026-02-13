package com.custom.youtube;

import java.util.HashMap;

public class MyStorage {
    private static final MyStorage ourInstance = new MyStorage();
    public HashMap<String, Object> storage = new HashMap<>();

    public static MyStorage getInstance() {
        return ourInstance;
    }

    private MyStorage() {
    }
}