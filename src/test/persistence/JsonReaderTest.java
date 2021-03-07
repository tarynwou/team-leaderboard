package persistence;

import model.Leaderboard;
import model.Profile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Leaderboard leaderboard = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyLeaderboard.json");
        try {
            Leaderboard leaderboard = reader.read();
            assertEquals(0, leaderboard.numProfiles());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralLeaderboard.json");
        try {
            Leaderboard leaderboard = reader.read();
            ArrayList<Profile> profiles = leaderboard.getProfiles();
            assertEquals(2, profiles.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
