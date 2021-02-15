package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EntryTest {
    private Entry testEntry;

    @BeforeEach
    public void setup() {
        testEntry = new Entry("Research", "Added X to Y", "Alex");
    }

    @Test
    public void testEntry() {
        assertEquals("Research", testEntry.getActionType());
        assertEquals("Added X to Y", testEntry.getComment());
        assertEquals("Alex", testEntry.getTeammate());
    }
}
