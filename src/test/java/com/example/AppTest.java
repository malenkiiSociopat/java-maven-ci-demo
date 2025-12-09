package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    @Test
    void testGreet() {
        assertEquals("Hello, World!", App.greet("World"));
    }
}