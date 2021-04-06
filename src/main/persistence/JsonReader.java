package persistence;

import exceptions.NotOnLeaderboardException;
import model.Entry;
import model.Leaderboard;
import model.Profile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads leaderboard from JSON data stored in file
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads leaderboard from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Leaderboard read(Leaderboard currentLeaderboard) throws IOException, NotOnLeaderboardException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLeaderboard(jsonObject, currentLeaderboard);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // MODIFIES: this
    // EFFECTS: parses leaderboard from JSON object and returns it
    private Leaderboard parseLeaderboard(JSONObject jsonObject, Leaderboard cl) {
        cl.clearLeaderboard();
        addProfiles(cl, jsonObject);
        return cl;
    }

    // MODIFIES: leaderboard
    // EFFECTS: parses profiles from JSON object and adds them to leaderboard
    private void addProfiles(Leaderboard leaderboard, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("profiles");
        for (Object json : jsonArray) {
            JSONObject nextProfile = (JSONObject) json;
            addProfile(leaderboard, nextProfile);
        }
    }

    // MODIFIES: leaderboard
    // EFFECTS: parses profile from JSON object and adds it to leaderboard
    private void addProfile(Leaderboard leaderboard, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int points = jsonObject.getInt("points");
        Profile profile = new Profile(name);
        profile.addPoints(points);
        addEntries(profile, jsonObject);
        leaderboard.addProfile(profile);
    }

    // MODIFIES: profile
    // EFFECTS: parses entries from JSON object and adds them to the profile
    private void addEntries(Profile profile, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("entries");
        for (Object json : jsonArray) {
            JSONObject nextEntry = (JSONObject) json;
            addEntry(profile, nextEntry);
        }
    }

    // MODIFIES: profile
    // EFFECTS: parses an entry from JSON object and adds it to the profile
    private void addEntry(Profile profile, JSONObject jsonObject) {
        String actionType = jsonObject.getString("actionType");
        String comment = jsonObject.getString("comment");
        String teammate = jsonObject.getString("teammate");
        Entry entry = new Entry(actionType, comment, teammate);
        profile.addToEntryList(entry); //TODO: make sure it doesn't add points twice
    }

}
