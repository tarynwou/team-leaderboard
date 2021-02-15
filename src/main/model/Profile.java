package model;

import java.util.ArrayList;

// Represents a teammate's profile on the leaderboard
public class Profile {
    private String name;                // name of teammate
    private int points;                 // # of accumulated points
                                        // add rank in future
    private ArrayList<Entry> entries = new ArrayList<Entry>();   // list of entries associated with this teammate

    /*
     * REQUIRES: profileName has a non-zero length
     * EFFECTS: name on profile is set to profileName and points on profile are set to 0
     */
    public Profile(String profileName) {
        name = profileName;
        points = 0;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public Entry getEntry(int number) {
        return entries.get(number);
    }

    // METHODS

    /*
     * REQUIRES: amount is a positive integer
     * MODIFIES: points
     * EFFECTS: adds a specified number of points from Profile's points
     */
    public int addPoints(int amount) {
        points = points + amount;
        return points;
    }

    /*
     * REQUIRES: amount is a positive integer
     * MODIFIES: points
     * EFFECTS: deducts a specified number of points from Profile's points
     */
    public int removePoints(int amount) {
        if (points - amount < 0) {
            points = 0;
        } else {
            points = points - amount;
        }
        return points;
    }

    /*
     * MODIFIES: points
     * EFFECTS: sets points to 0
     */
    public int clearPoints() {
        points = 0;
        return points;
    }

    /*
     * MODIFIES: entries
     * EFFECTS: adds an entry to the profile's list of entries
     */
    public void addToEntryList(Entry entry) {
        entries.add(entry);
        if (entry.getActionType() == "copywriting") {
            addPoints(150);
        } else if (entry.getActionType() == "research") {
            addPoints(100);
        } else if (entry.getActionType() == "marketing") {
            addPoints(100);
        } else if (entry.getActionType() == "good deed") {
            addPoints(50);
        }
    }

    /*
     * EFFECTS: prints the name and points of a given profile
     */
    public String seeProfile() {
        return "Profile Name: " + name + "; Points: " + points; // Implement entries later in the project
    }

    /*
     * EFFECTS: prints the name and points of a given profile for the leaderboard
     */
    public String profileLine() {
        return "\n\t" + name + "   -   " + points;
    }
}
