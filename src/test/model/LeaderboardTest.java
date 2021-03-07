package model;

import exceptions.NotOnLeaderboardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LeaderboardTest {
    public Leaderboard testleaderboard;
    public ArrayList<Profile> team = new ArrayList<Profile>();
    public Profile alex;
    public Profile kaitlin;
    public Profile anjali;
    public Profile serena;


    @BeforeEach
    public void setup() {
        alex = new Profile("Alex");
        kaitlin = new Profile("Kaitlin");
        anjali = new Profile("Anjali");
        serena = new Profile("Serena");

        testleaderboard = new Leaderboard(team);
        testleaderboard.addProfile(alex);
        testleaderboard.addProfile(kaitlin);
        testleaderboard.addProfile(anjali);
    }

    @Test
    public void testLeaderboard() {
        assertEquals(alex, testleaderboard.getProfile(1));
    }

    @Test
    public void testAddProfile() {
        testleaderboard.addProfile(serena);

        assertEquals(alex, testleaderboard.getProfile(1));
        assertEquals(kaitlin, testleaderboard.getProfile(2));
        assertEquals(anjali, testleaderboard.getProfile(3));
        assertEquals(serena, testleaderboard.getProfile(4));
    }

    @Test
    public void testRemoveProfileToZeroV1() throws NotOnLeaderboardException {
        testleaderboard.removeProfile("Alex");
        testleaderboard.removeProfile("Kaitlin");
        testleaderboard.removeProfile("Anjali");
        assertEquals(0, team.size());
    }

    @Test
    public void testRemoveProfileToZeroV2() throws NotOnLeaderboardException {
        testleaderboard.removeProfile("Anjali");
        testleaderboard.removeProfile("Kaitlin");
        testleaderboard.removeProfile("Alex");
        assertEquals(0, team.size());
    }

    @Test
    public void testRemoveProfileLeftover() {
        try {
            testleaderboard.removeProfile("Alex");
            assertEquals(2, team.size());
            assertEquals(kaitlin, testleaderboard.getProfile(1));
            assertEquals(anjali, testleaderboard.getProfile(2));
        } catch (NotOnLeaderboardException e) {
            fail();
        }
    }

    @Test
    public void testShowLeaderboardNoPoints() {
        assertEquals("\nLEADERBOARD" +
                        "\n\tAlex   -   0" +
                        "\n\tKaitlin   -   0" +
                        "\n\tAnjali   -   0"
                , testleaderboard.showLeaderboard(team));
    }

    @Test
    public void testShowLeaderboardWithPoints() {
        alex.addPoints(500);
        assertEquals("\nLEADERBOARD" +
                        "\n\tAlex   -   500" +
                        "\n\tKaitlin   -   0" +
                        "\n\tAnjali   -   0",
                testleaderboard.showLeaderboard(team));
    }

    @Test
    public void testResetLeaderboard() {
        alex.addPoints(500);
        anjali.addPoints(800);
        testleaderboard.resetLeaderboard(team);
        assertEquals("\nLEADERBOARD" +
                        "\n\tAlex   -   0" +
                        "\n\tKaitlin   -   0" +
                        "\n\tAnjali   -   0",
                testleaderboard.showLeaderboard(team));
    }


    @Test
    public void testSwap() throws NotOnLeaderboardException {
        testleaderboard.removeProfile("Anjali");
        testleaderboard.swap(2);
        assertEquals("\nLEADERBOARD" +
                        "\n\tKaitlin   -   0" +
                        "\n\tAlex   -   0",
                testleaderboard.showLeaderboard(team));
    }

    @Test
    public void testMoveUpToFirst() {
        anjali.addPoints(800);
        testleaderboard.moveUp(3);
        assertEquals("\nLEADERBOARD" +
                        "\n\tAnjali   -   800" +
                        "\n\tAlex   -   0" +
                        "\n\tKaitlin   -   0",
                testleaderboard.showLeaderboard(team));
    }

    @Test
    public void testMoveUpToSecond() {
        alex.addPoints(800);
        anjali.addPoints(100);
        testleaderboard.moveUp(3);
        assertEquals("\nLEADERBOARD" +
                        "\n\tAlex   -   800" +
                        "\n\tAnjali   -   100" +
                        "\n\tKaitlin   -   0",
                testleaderboard.showLeaderboard(team));
    }

    @Test
    public void testSortLeaderboard() {
        alex.addPoints(500);
        anjali.addPoints(800);
        testleaderboard.sortLeaderboard(team);
        assertEquals("\nLEADERBOARD" +
                        "\n\tAnjali   -   800" +
                        "\n\tAlex   -   500" +
                        "\n\tKaitlin   -   0",
                testleaderboard.showLeaderboard(team));
    }
}
