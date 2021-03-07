package persistence;

import exceptions.NotOnLeaderboardException;
import model.Entry;
import model.Leaderboard;
import model.Profile;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {

    private ArrayList<Profile> team = new ArrayList<Profile>();

    @Test
    void testWriterInvalidFile() {
        try {
            Leaderboard leaderboard = new Leaderboard(team);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyLeaderboard() {
        try {
            Leaderboard leaderboard = new Leaderboard(team);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyLeaderboard.json");
            writer.open();
            writer.write(leaderboard);
            writer.close();

            ArrayList<Profile> team = new ArrayList<Profile>();
            Leaderboard cl = new Leaderboard(team);

            JsonReader reader = new JsonReader("./data/testWriterEmptyLeaderboard.json");
            leaderboard = reader.read(cl);
            assertEquals(0, leaderboard.numProfiles());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (NotOnLeaderboardException e) {
            fail("Shouldn't call this");
        }
    }

    @Test
    void testWriterGeneralLeaderboard() {
        try {
            Leaderboard leaderboard = new Leaderboard(team);
            leaderboard.addProfile(new Profile("Alex"));
            leaderboard.addProfile(new Profile("Serena"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralLeaderboard.json");
            writer.open();
            writer.write(leaderboard);
            writer.close();

            ArrayList<Profile> team = new ArrayList<Profile>();
            Leaderboard cl = new Leaderboard(team);

            JsonReader reader = new JsonReader("./data/testWriterGeneralLeaderboard.json");
            leaderboard = reader.read(cl);
            ArrayList<Profile> profiles = leaderboard.getProfiles();
            assertEquals(2, profiles.size());
            checkProfile(leaderboard.getProfile(1), "Alex", 0, null);
            checkProfile(leaderboard.getProfile(2), "Serena", 0, null);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        } catch (NotOnLeaderboardException e) {
            fail("Shouldn't call this");
        }
    }
}
