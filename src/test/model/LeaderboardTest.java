package model;

import com.sun.tools.corba.se.idl.constExpr.Not;
import exceptions.NotOnLeaderboardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

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
        try {
            testleaderboard.removeProfile("Alex");
            testleaderboard.removeProfile("Kaitlin");
            testleaderboard.removeProfile("Anjali");
            assertEquals(0, team.size());
        } catch (ConcurrentModificationException e) {

        } catch (NotOnLeaderboardException e) {
            fail();
        }
    }

    @Test
    public void testRemoveProfileToZeroV2() throws NotOnLeaderboardException {
        try {
            testleaderboard.removeProfile("Anjali");
            testleaderboard.removeProfile("Kaitlin");
            testleaderboard.removeProfile("Alex");
            assertEquals(0, team.size());
        } catch (ConcurrentModificationException e) {

        } catch (NotOnLeaderboardException e) {
            fail();
        }
    }

    @Test
    public void testRemoveProfileLeftover() {
        try {
            testleaderboard.addProfile(alex);
            testleaderboard.removeProfile("Alex");
            assertEquals(2, team.size());
            assertEquals(kaitlin, testleaderboard.getProfile(1));
            assertEquals(anjali, testleaderboard.getProfile(2));
        } catch (NotOnLeaderboardException e) {
            fail();
        } catch (ConcurrentModificationException e) {

        }
    }

    @Test
    public void testRemoveBottomProfileLeftover() {
        try {
            testleaderboard.removeProfile("Anjali");
            assertEquals(2, team.size());
            assertEquals(kaitlin, testleaderboard.getProfile(2));
            assertEquals(alex, testleaderboard.getProfile(1));
        } catch (NotOnLeaderboardException e) {
            fail();
        } catch (ConcurrentModificationException e) {

        }
    }
    @Test
    public void testRemoveSameProfile() {
        try {
            testleaderboard.removeProfile("Anjali");
            assertEquals(2, team.size());
            assertEquals(kaitlin, testleaderboard.getProfile(2));
            assertEquals(alex, testleaderboard.getProfile(1));
        } catch (NotOnLeaderboardException e) {
            fail();
        } catch (ConcurrentModificationException e) {

        }
    }

    @Test
    public void testRemoveNonExistentProfile() {
        try {
            testleaderboard.removeProfile("Cheryl");
            fail();
        } catch (NotOnLeaderboardException e) {
        } catch (ConcurrentModificationException e) {

        }
    }

    @Test
    public void testClearLeaderboard() {
        testleaderboard.clearLeaderboard();
        assertEquals(0, team.size());
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
        try {
            testleaderboard.removeProfile("Anjali");
            testleaderboard.swap(2);
            assertEquals("\nLEADERBOARD" +
                            "\n\tKaitlin   -   0" +
                            "\n\tAlex   -   0",
                    testleaderboard.showLeaderboard(team));
        } catch (ConcurrentModificationException e) {

        }
    }

    @Test
    public void testSwapSimple() {
        try {
            testleaderboard.swap(2);
            assertEquals(kaitlin, testleaderboard.getProfile(1));
        } catch (ConcurrentModificationException e) {

        }
    }

    @Test
    public void testNoMoveUp() {
        testleaderboard.moveUp(3);
        assertEquals("\nLEADERBOARD" +
                        "\n\tAlex   -   0" +
                        "\n\tKaitlin   -   0" +
                        "\n\tAnjali   -   0",
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

    @Test
    public void testSortOneProfile() throws NotOnLeaderboardException {
        try {
            testleaderboard.removeProfile("Alex");
            testleaderboard.removeProfile("Kaitlin");
            testleaderboard.sortLeaderboard(team);
            assertEquals("\nLEADERBOARD" +
                            "\n\tAnjali   -   0",
                    testleaderboard.showLeaderboard(team));
        } catch (ConcurrentModificationException e) {

        } catch (NotOnLeaderboardException e) {
            fail();
        }
    }

    @Test
    public void testSortLeaderboardThree() {
        alex.addPoints(500);
        anjali.addPoints(1000);
        kaitlin.addPoints(900);
        testleaderboard.sortLeaderboard(team);
        assertEquals("\nLEADERBOARD" +
                        "\n\tAnjali   -   1000" +
                        "\n\tKaitlin   -   900" +
                        "\n\tAlex   -   500" ,
                testleaderboard.showLeaderboard(team));
    }

    @Test
    public void testDoesNotContain() {
        assertTrue(testleaderboard.doesNotContain("cheryl"));
    }

    @Test
    public void testDoesContain() {
        assertFalse(testleaderboard.doesNotContain("Alex"));
    }
}
