package persistence;

import model.Entry;
import model.Profile;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkProfile(Profile profile, String name, int points, ArrayList<Entry> entries) {
        assertEquals(name, profile.getName());
        assertEquals(points, profile.getPoints());
        assertEquals(0, profile.getEntries().size());
        assertEquals(null, entries);
    }
}
