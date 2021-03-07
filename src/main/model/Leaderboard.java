package model;

import exceptions.NotOnLeaderboardException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collections;

//TODO: Fix tests

// Represents the leaderboard with teammates and respective points
public class Leaderboard implements Writable {
    private ArrayList<Profile> team = new ArrayList<Profile>();

    /*
     * EFFECTS: sets the team leaderboard given a list of profiles
     */
    public Leaderboard(ArrayList<Profile> profiles) {
        team = profiles;
    }

    public Profile getProfile(int number) {
        return team.get(number - 1);
    }

    // EFFECTS: returns a list of profiles on this leaderboard
    public ArrayList<Profile> getProfiles() {
        return team;
    }

    // EFFECTS: returns number of profiles on this leaderboard
    public int numProfiles() {
        return team.size();
    }

    /*
     * MODIFIES: profiles
     * EFFECTS: adds a profile to the list of profiles
     */
    public void addProfile(Profile profile) {
        team.add(profile);
    }

    /*
     * MODIFIES: profiles
     * EFFECTS: removes the profile with given name from the leaderboard
     */
    public void removeProfile(String name) throws NotOnLeaderboardException {
        if (doesNotContain(name) == true) {
            throw new NotOnLeaderboardException();
        }

        for (Profile teammate : team) {
            if (teammate.getName().contains(name)) {
                team.remove(teammate);
            }
        }

    }

    /*
     * MODIFIES: this
     * EFFECTS: removes all profiles from the leaderboard
     */
    public void clearLeaderboard() {
        team.clear();
    }

    /*
     * EFFECTS: returns false if the list does not contain s
     */
    public boolean doesNotContain(String s) {
        boolean b = true;
        for (Profile teammate : team) {
            if (teammate.getName().contains(s)) {
                b = false;
            }
        }
        return b;
    }

    /*
     * REQUIRES: non-empty team
     * EFFECTS: prints out the leaderboard
     */
    public static String showLeaderboard(ArrayList<Profile> team) {
        String leaderboardstatement = ("\nLEADERBOARD");
        for (Profile t : team) {
            leaderboardstatement = leaderboardstatement + t.profileLine();
        }
        return leaderboardstatement;
    }

    /*
     * REQUIRES: profiles is a non-empty list
     * MODIFIES: profiles
     * EFFECTS: resets all the profiles' points to zero
     */
    public static String resetLeaderboard(ArrayList<Profile> team) {
        for (Profile t : team) {
            t.clearPoints();
        }
        return showLeaderboard(team);
    }

    // Used to test how swap works
    public void swap(int num) {
        Collections.swap(team, num - 1, num - 2);
    }

    /*
     * REQUIRES: rank is >=1
     * MODIFIES: profiles
     * EFFECTS: moves a profile up the list (closer to index 0) if it has more points than the profile in front of it
     */

    public void moveUp(int rank) {
        if (rank == 1) {
            showLeaderboard(team);
        } else if (team.get(rank - 1).getPoints() > team.get(rank - 2).getPoints()) {
            Collections.swap(team, rank - 1, rank - 2);
            moveUp(rank - 1);
        }
    }

    /*
     * REQUIRES: profiles is a non-empty list
     * MODIFIES: profiles
     * EFFECTS: sorts the leaderboard by number of points in each profile; the smaller the index, the more points
     */
    public void sortLeaderboard(ArrayList<Profile> team) {
        for (Profile t : team) {
            moveUp(team.size());
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("profiles", profilesToJson());
        return json;
    }

    /*
     * EFFECTS: returns profiles on this leaderboard as a JSON array
     */
    private JSONArray profilesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Profile p : team) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }
}
