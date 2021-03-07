package ui;

import exceptions.NotOnLeaderboardException;
import model.Entry;
import model.Leaderboard;
import model.Profile;

import persistence.JsonWriter;
import persistence.JsonReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

// CREDITS: TellarApp Project and JsonSerializationDemo for functionalities

// Team Leaderboard
public class TeamLeaderboard {
    private ArrayList<Profile> team = new ArrayList<Profile>();
    private Leaderboard leaderboard;
    private Profile teammate;
    private Scanner input;

    private static final String JSON_STORE = "./data/leaderboard.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    /*
     * EFFECTS: Runs the team leaderboard
     */
    public TeamLeaderboard() {
        leaderboard = new Leaderboard(team);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runLeaderboard();
    }


    /*
     * MODIFIES: this
     * EFFECTS: processes user input
     */
    private void runLeaderboard() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nCheck back soon!");
    }

    private void init() {
        input = new Scanner(System.in);
    }

    /*
     * EFFECTS: displays available options to user
     */
    private void displayMenu() {
        System.out.println("\nWhat would you like to do:");
        System.out.println("\ta -> add teammate");
        System.out.println("\td -> remove teammate");
        System.out.println("\te -> log entry");
        System.out.println("\tl -> show leaderboard");
        System.out.println("\tm -> deduct points");
        System.out.println("\tn -> reset points"); // --> are you sure confirmation message
        System.out.println("\tr -> reload previous leaderboard");
        System.out.println("\ts -> save leaderboard");
        System.out.println("\tq -> quit");
    }

    /*
     * MODIFIES: this
     * EFFECTS: processes user command
     */
    //TODO: Add viewEntries()/viewProfile method.
    private void processCommand(String command) {
        if (command.equals("a")) {
            addTeammate();
        } else if (command.equals("d")) {
            removeTeammate();
        } else if (command.equals("e")) {   //TODO: user should receive a message if the user DNE
            logEntry();
        } else if (command.equals("l")) {
            showLeaderboard();
        } else if (command.equals("m")) {
            deductPoints();
        } else if (command.equals("n")) {
            Leaderboard.resetLeaderboard(team);
        } else if (command.equals("r")) {
            reloadLeaderboard(leaderboard);
        } else if (command.equals("s")) {
            saveLeaderboard();
        } else {
            System.out.println("That is not a valid command. Try again.");
        }
    }

    /*
     * REQUIRES: person is on the list
     * EFFECTS: selects a person
     */
    private String selectAction() {
        String selection = "";  // force entry into loop

        while (!(selection.equals("c") || selection.equals("r") || selection.equals("m")
                || selection.equals("s"))) {
            System.out.println("c - copywriting (150pts)");
            System.out.println("m - marketing (100pts)");
            System.out.println("r - research (100pts)");
            System.out.println("s - something good (50pts)");
            selection = input.next();
            selection += input.nextLine();
            selection = selection.toLowerCase();
        }

        if (selection.equals("c")) {
            return "copywriting";
        } else if (selection.equals("r")) {
            return "research";
        } else if (selection.equals("m")) {
            return "marketing";
        } else if (selection.equals("s")) {
            return "something good";
        }
        return "Not an action";
    }

    /*
     * REQUIRES: name cannot already exist on the leaderboard
     * MODIFIES: this
     * EFFECTS: adds a person to the team leaderboard
     */
    private void addTeammate() {
        System.out.println("\nWho would you like to add to the team?");
        String name = input.next();
        name += input.nextLine();
        Profile newTeammate = new Profile(name);
        leaderboard.addProfile(newTeammate);
        System.out.println("\nYou added " + newTeammate.getName() + " to the team!");
        System.out.println(leaderboard.showLeaderboard(team));
    }

    /*
     * MODIFIES: this
     * EFFECTS: removes a person from the team leaderboard
     */
    private void removeTeammate() {
        System.out.println("Who would you like to remove from the team?");
        //User can input the name of the teammate they want to remove
        String name = input.next();
        name += input.nextLine();

        try {
            leaderboard.removeProfile(name);
        } catch (ConcurrentModificationException e) {
            System.out.println("\nYou removed " + name + " from the team!");
        } catch (NotOnLeaderboardException e) {
            System.out.println("\nThere is no teammate with that name on the leaderboard...");
        }
        System.out.println(leaderboard.showLeaderboard(team));
    }

    /*
     * REQUIRES: person must be on the team
     * MODIFIES: this
     * EFFECTS: logs an entry
     */
    public void logEntry() {
        System.out.println("What was the action?");
        String actionType = selectAction();
        System.out.println("Attach a comment on their performance.");
        String comment = input.next();
        comment += input.nextLine();
        System.out.println("Who completed this action?");
        String name = input.next();
        name += input.nextLine();

        Entry entry = new Entry(actionType, comment, name);
        for (Profile teammate : team) {
            if (teammate.getName().contains(name)) {
                teammate.addToEntryList(entry);
            }
        }

        System.out.println(leaderboard.showLeaderboard(team));

    }

    /*
     * REQUIRES: person must be on the team
     * MODIFIES: this
     * EFFECTS: deducts points from a teammate
     */
    public void deductPoints() {
        System.out.println("Who would you like to deduct points from?");
        String name = input.next();
        name += input.nextLine();

        System.out.println("How many points would you like to deduct?");
        int amount = input.nextInt();
        for (Profile teammate : team) {
            if (teammate.getName().contains(name)) {
                teammate.removePoints(amount);
            }
        }
        System.out.println(leaderboard.showLeaderboard(team));
    }

    /*
     * EFFECTS: shows leaderboard
     */
    public void showLeaderboard() {
        leaderboard.sortLeaderboard(team);
        System.out.println(leaderboard.showLeaderboard(team));
    }

    // EFFECTS: saves the workroom to file
    private void saveLeaderboard() {
        try {
            jsonWriter.open();
            jsonWriter.write(leaderboard);
            jsonWriter.close();
            System.out.println("Saved leaderboard" + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        } finally {
            System.out.println(leaderboard.showLeaderboard(team));
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void reloadLeaderboard(Leaderboard currentLeaderboard) {
        try {
            leaderboard = jsonReader.read(currentLeaderboard);
            System.out.println("Loaded leaderboard" + " from " + JSON_STORE);
        } catch (NotOnLeaderboardException e) {
            System.out.println("\nThere is no teammate with that name on the leaderboard...");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        } finally {
            System.out.println(leaderboard.showLeaderboard(team));
        }
    }
}