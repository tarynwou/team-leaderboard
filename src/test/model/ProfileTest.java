package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {
    private Profile alex;
    private Entry testEntry;
    private ArrayList<Entry> entries = new ArrayList<Entry>();

    @BeforeEach
    public void setup() {
        alex = new Profile("Alex");
    }

    @Test
    public void testProfile() {
        assertEquals("Alex", alex.getName());
        assertEquals(0, alex.getPoints());
    }

    @Test
    public void testSeeProfileEmpty() {
        assertEquals("Profile Name: Alex; Points: 0", alex.seeProfile());
    }

    @Test
    public void testSeeProfileWithPoints() {
        alex.addPoints(100);
        assertEquals("Profile Name: Alex; Points: 100", alex.seeProfile());
    }

    @Test
    public void testProfileLine() {
        assertEquals("\n\tAlex   -   0", alex.profileLine());
    }

    @Test
    public void testAddPoints() {
        assertEquals(500, alex.addPoints(500));
    }

    @Test
    public void testRemovePointsEnough() {
        alex.addPoints(500);
        assertEquals(400, alex.removePoints(100));
    }

    @Test
    public void testRemovePointsOverflow() {
        alex.addPoints(500);
        assertEquals(0, alex.removePoints(900));
    }

    @Test
    public void testClearPoints() {
        alex.addPoints(500);
        assertEquals(0, alex.clearPoints());
    }

    @Test
    public void testAddToEntryList() {
        testEntry = new Entry("research", "Added X to Y", "Alex");
        alex.addToEntryList(testEntry);
        assertEquals(100, alex.getPoints());
    }

    @Test
    public void testAddToEntryLisMultiple() {
        testEntry = new Entry("research", "Added X to Y", "Alex");
        alex.addToEntryList(testEntry);
        testEntry = new Entry("copywriting", "Added X to Y", "Alex");
        alex.addToEntryList(testEntry);
        testEntry = new Entry("marketing", "Added X to Y", "Alex");
        alex.addToEntryList(testEntry);
        testEntry = new Entry("good deed", "Added X to Y", "Alex");
        alex.addToEntryList(testEntry);

        assertEquals(400, alex.getPoints());
    }

    @Test
    public void testGetEntry() {
        testEntry = new Entry("Research", "Added X to Y", "Alex");
        alex.addToEntryList(testEntry);
        assertEquals(testEntry, alex.getEntry(0));
    }

}
