package ui;

import model.Entry;
import model.Leaderboard;
import model.Profile;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Scanner;

// CREDITS: TellarApp Project for functionalities

// Team Leaderboard
public class TeamLeaderboard {
    private ArrayList<Profile> team = new ArrayList<Profile>();
    private Leaderboard leaderboard = new Leaderboard(team);
    private Profile teammate;
    private Scanner input;
//    private Profile alex = new Profile("Alex");
//    private Profile kaitlin = new Profile("Kaitlin");
//    private Profile anjali = new Profile("Anjali");
//    private Profile serena = new Profile("Serena");
//    private Profile daniel = new Profile("Daniel");

    // be able to add new users in the future

    /*
     * EFFECTS: Runs the team leaderboard
     */
    public TeamLeaderboard() {
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
        System.out.println("\tl -> log entry");
        System.out.println("\tm -> deduct points");
        System.out.println("\ts -> show leaderboard");
        System.out.println("\tr -> reset leaderboard");
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
        } else if (command.equals("l")) {
            logEntry();
        } else if (command.equals("m")) {
            deductPoints();
        }  else if (command.equals("s")) {
            showLeaderboard();
        } else if (command.equals("r")) {
            Leaderboard.resetLeaderboard(team);
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
                || selection.equals("g"))) {
            System.out.println("c - copywriting (150pts)");
            System.out.println("r - research (100pts)");
            System.out.println("m - marketing (100pts)");
            System.out.println("g - good deed (50pts)");
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
        } else if (selection.equals("g")) {
            return "good deed";
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
            leaderboard.removeProfile(name);
        }
        System.out.println("\nYou removed " + name + " from the team!");
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
}