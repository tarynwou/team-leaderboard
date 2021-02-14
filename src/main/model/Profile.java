package model;

import java.util.ArrayList;

public class Profile {
    private String name;                // name of teammate
    private int points;                 // # of accumulated points
    private ArrayList<Entry> entries;   // list of entries associated with this teammate

    // TEMPLATE -- DELETE LATER
    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS:
     */

    /*
     * REQUIRES: profileName has a non-zero length
     * MODIFIES:
     * EFFECTS:
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

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: adds a specified number of points from Profile's points
     */
    public void addPoints(int amount) {
        points = points + amount;
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: deducts a specified number of points from Profile's points
     */
    public void deductPoints(int amount) {
        if (points - amount < 0) {
            points = 0;
        } else {
            points = points - amount;
        }
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS:
     */
    public void clearPoints() {
        points = 0;
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS:
     */
    public void addToEntryList(Entry entry) {
        entries.add(entry);
    }

    public String seeProfile(Profile profile) {
        return "Profile Name: " + name + "; Points: " + points; // Implement entries later in the project
    }
}
