package persistence;

import exceptions.NotOnLeaderboardException;
import model.Leaderboard;
import model.Profile;
import org.junit.jupiter.api.BeforeEach;
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
            ArrayList<Profile> team = new ArrayList<Profile>();
            Leaderboard cl = new Leaderboard(team);
            Leaderboard leaderboard = reader.read(cl);
            fail("IOException expected");
        } catch (IOException | NotOnLeaderboardException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyLeaderboard() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyLeaderboard.json");
        try {
            ArrayList<Profile> team = new ArrayList<Profile>();
            Leaderboard cl = new Leaderboard(team);
            Leaderboard leaderboard = reader.read(cl);
            assertEquals(0, leaderboard.numProfiles());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (NotOnLeaderboardException e) {
            fail("Shouldn't call this");
        }
    }

    @Test
    void testReaderGeneralLeaderboard() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralLeaderboard.json");
        try {
            ArrayList<Profile> team = new ArrayList<Profile>();
            Leaderboard cl = new Leaderboard(team);
            Leaderboard leaderboard = reader.read(cl);
            ArrayList<Profile> profiles = leaderboard.getProfiles();
            assertEquals(2, profiles.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        } catch (NotOnLeaderboardException e) {
            fail("Shouldn't call this");
        }
    }
}
