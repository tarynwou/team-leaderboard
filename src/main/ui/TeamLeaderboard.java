package ui;

import model.Entry;
import model.Leaderboard;
import model.Profile;

import java.util.ArrayList;
import java.util.Scanner;

// CREDITS: TellarApp Project for functionalities

// Team Leaderboard
public class TeamLeaderboard {
    private ArrayList<Profile> team = new ArrayList<Profile>();
    private Leaderboard leaderboard = new Leaderboard(team);
    private Profile teammate;
    private Scanner input;
    private Profile alex = new Profile("Alex");
    private Profile kaitlin = new Profile("Kaitlin");
    private Profile anjali = new Profile("Anjali");
    private Profile serena = new Profile("Serena");
    private Profile daniel = new Profile("Daniel");

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
    private Profile selectPerson() {
        String selection = "";  // force entry into loop

        while (!(selection.equals("alex") || selection.equals("kaitlin") || selection.equals("anjali")
                || selection.equals("serena") || selection.equals("daniel"))) {
            System.out.println("alex");
            System.out.println("kaitlin");
            System.out.println("anjali");
            System.out.println("serena");
            System.out.println("daniel");
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("alex")) {
            return alex;
        } else if (selection.equals("kaitlin")) {
            return kaitlin;
        } else if (selection.equals("anjali")) {
            return anjali;
        } else if (selection.equals("serena")) {
            return serena;
        } else if (selection.equals("daniel")) {
            return daniel;
        }
        return teammate;
    }

    /*
     * REQUIRES: input must be between 1 and 5
     * EFFECTS: selects a rank on the leaderboard
     */
    private int selectRank() {
        String selection = "";  // force entry into loop

        while (!(selection.equals("1") || selection.equals("2") || selection.equals("3")
                || selection.equals("4") || selection.equals("5"))) {
            System.out.println("1 for Rank 1");
            System.out.println("2 for Rank 2");
            System.out.println("3 for Rank 3");
            System.out.println("4 for Rank 4");
            System.out.println("5 for Rank 5");
            selection = input.next();
            selection = selection.toLowerCase();
        }

        if (selection.equals("1")) {
            return 1;
        } else if (selection.equals("2")) {
            return 2;
        } else if (selection.equals("3")) {
            return 3;
        } else if (selection.equals("4")) {
            return 4;
        } else if (selection.equals("5")) {
            return 5;
        }
        return 0;
    }

    /*
     * REQUIRES: person is on the list
     * EFFECTS: selects a person
     */
    private String selectAction() {
        String selection = "";  // force entry into loop

        while (!(selection.equals("copywriting") || selection.equals("research") || selection.equals("marketing")
                || selection.equals("good deed"))) {
            System.out.println("copywriting");
            System.out.println("research");
            System.out.println("marketing");
            System.out.println("good deed");
            selection = input.next();
            selection += input.nextLine();
            selection = selection.toLowerCase();
        }

        if (selection.equals("copywriting")) {
            return "copywriting";
        } else if (selection.equals("research")) {
            return "research";
        } else if (selection.equals("marketing")) {
            return "marketing";
        } else if (selection.equals("good deed")) {
            return "good deed";
        }
        return "Not an action";
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds a person to the team leaderboard
     */
    private void addTeammate() {
        System.out.println("\nWho would you like to add to the team?");
        Profile teammate = selectPerson();
        leaderboard.addProfile(teammate);
        System.out.println("\nYou added " + teammate.getName() + " to the team!");
        System.out.println(leaderboard.showLeaderboard(team));
    }

    /*
     * MODIFIES: this
     * EFFECTS: removes a person from the team leaderboard
     */
    private void removeTeammate() {
        System.out.println("Who would you like to remove from the team?");
        int rank = selectRank();
        System.out.println("\nYou removed " + team.get(rank - 1).getName() + " from the team!");
        leaderboard.removeProfile(rank);
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
        Profile teammate = selectPerson();

        Entry entry = new Entry(actionType, comment, teammate.getName());
        teammate.addToEntryList(entry);

        System.out.println(leaderboard.showLeaderboard(team));

    }

    /*
     * REQUIRES: person must be on the team
     * MODIFIES: this
     * EFFECTS: deducts points from a teammate
     */
    public void deductPoints() {
        System.out.println("Who would you like to deduct points from?");
        Profile teammate = selectPerson();
        System.out.println("How many points would you like to deduct?");
        int amount = input.nextInt();
        teammate.removePoints(amount);
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