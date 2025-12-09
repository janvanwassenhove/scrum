package scrum;

import org.junit.jupiter.api.Test;
import scrum.exception.ImpedimentCode;
import scrum.exception.ScrumRuntimeException;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Scrum-inspired impediment reporting.
 */
public class ImpedimentTest {

    @Test
    public void testDivisionByZeroThrowsImpediment() {
        ScrumLanguage lang = new ScrumLanguage();
        
        ScrumRuntimeException exception = assertThrows(ScrumRuntimeException.class, () -> {
            lang.execute(Path.of("development/examples/test-errors/DivisionByZero.scrum"));
        });
        
        assertEquals(ImpedimentCode.SCRUM_RUNTIME_ARITH_001, exception.getImpedimentCode());
        assertEquals("DivisionByZero.scrum", exception.getFileName());
        assertTrue(exception.getMessage().contains("Division by zero"));
    }
}
