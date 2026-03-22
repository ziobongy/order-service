package it.adesso.management.ordermanagementservice.services.impl;

import it.adesso.management.ordermanagementservice.services.JwtUtilService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilServiceImplTest {

    @InjectMocks
    private JwtUtilServiceImpl jwtUtilService;

    @Test
    void testGetUserIdentifier() {
        // Act
        String result = jwtUtilService.getUserIdentifier();

        // Assert
        assertNotNull(result);
        assertEquals("QRT", result);
    }

    @Test
    void testGetUserIdentifier_MultipleCallsSameResult() {
        // Act
        String result1 = jwtUtilService.getUserIdentifier();
        String result2 = jwtUtilService.getUserIdentifier();

        // Assert
        assertEquals(result1, result2);
        assertEquals("QRT", result1);
    }
}

