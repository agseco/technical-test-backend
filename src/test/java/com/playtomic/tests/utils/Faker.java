package com.playtomic.tests.utils;

import java.util.UUID;

public class Faker {
    public static final com.github.javafaker.Faker faker = new com.github.javafaker.Faker();

    public static UUID uuid() {
        return UUID.randomUUID();
    }
}
