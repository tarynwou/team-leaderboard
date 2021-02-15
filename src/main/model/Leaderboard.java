package model;

import java.util.ArrayList;
import java.util.Collections;


// Represents the leaderboard with teammates and respective points
public class Leaderboard {
    private static Profile profile;
    private static ArrayList<Profile> team = new ArrayList<Profile>();

    /*
     * EFFECTS: sets the team leaderboard given a list of profiles
     */
    public Leaderboard(ArrayList<Profile> profiles) {
        team = profiles;
    }

    public Profile getProfile(int number) {
        return team.get(number - 1);
    }

    /*
     * MODIFIES: profiles
     * EFFECTS: adds a profile to the list of profiles
     */
    public void addProfile(Profile profile) {
        team.add(profile);
    }

    /*
     * REQUIRES: rank <= size of profiles
     * MODIFIES: profiles
     * EFFECTS: removes the profile with given rank from the leaderboard
     */
    public void removeProfile(int rank) {
        team.remove(rank - 1);
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


}
