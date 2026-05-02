package com.bits.library;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class LibraryApplicationTests {

    @Test
    void contextLoads() {
        // Verifies the full Spring application context wires up cleanly with H2.
    }
}
