package ui;

import exceptions.NotOnLeaderboardException;
import model.Entry;
import model.Leaderboard;
import model.Profile;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;


// CREDITS: LabelChanger and TextInputDemo

// Creates the graphical user interfaces for Team Leaderboard
public class TeamLeaderboardGUI extends JFrame implements ActionListener {
    // ~~~ FIELDS ~~~
    // Save and reload fields
    private static final String JSON_STORE = "./data/leaderboard.json";
    private final JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    private final JsonReader jsonReader = new JsonReader(JSON_STORE);

    // Sounds
    private static String SAVE_SOUND = "./data/text_sounds.wav";
    private static String LOAD_SOUND = "./data/super_mario_mushroom.wav";

    //TODO: add documentation

    private ArrayList<Profile> team = new ArrayList<Profile>();
    private Leaderboard leaderboard = new Leaderboard(team);

    // Fields
    JTextField nameField = new JTextField();
    JTextField commentField = new JTextField();
    JTextField teammateField = new JTextField();
    JSpinner actionSpinner;

    // Labels
    JLabel nameLabel;
    JLabel display;

    // Constants
    private static int GAP = 10;
    private static int SCREEN_WIDTH = 800;
    private static int SCREEN_HEIGHT = 400;

    // ~~~ SETUP ~~~

    // EFFECTS: Constructs TeamLeaderboardGUI
    public TeamLeaderboardGUI() {
        super("Team Leaderboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        setUp();

        JButton btn = new JButton("");
        add(btn);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // EFFECTS: Creates and lays out 3 main panels
    private void setUp() {
        JPanel leftPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setBounds(0, 0, SCREEN_WIDTH / 8, SCREEN_HEIGHT);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createLeftButtons());

        middlePanel.setBounds(SCREEN_WIDTH / 8, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
        middlePanel.add(setUpMiddlePanel());

        rightPanel.setBounds(SCREEN_WIDTH * 5 / 8, 0, SCREEN_WIDTH * 3 / 8, SCREEN_HEIGHT);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(setUpRightPanel());

        add(leftPanel);
        add(middlePanel);
        add(rightPanel);
    }

    // EFFECTS: Creates a button and adds it to a panel
    private void addButton(JPanel panel, String name, String command) {
        JButton button;
        button = new JButton(name);
        button.addActionListener(this);
        button.setActionCommand(command);
        panel.add(button);
    }

    // ~~~ LEFT PANEL ~~~

    // EFFECTS:
    protected JComponent createLeftButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addButton(panel, "Quit", "quit");

        addButton(panel, "Save", "save");

        addButton(panel, "Load", "load");

        addButton(panel, "Reset", "reset");

        //Match the SpringLayout's gap, subtracting 5 to make up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                GAP - 5, GAP - 5));
        return panel;
    }

    // ~~~ MIDDLE PANEL ~~~

    // EFFECTS: Sets up the middle panel with the leaderboard display
    private Component setUpMiddlePanel() {
        JPanel middlePanelSetup = new JPanel();
        display = new JLabel();
        middlePanelSetup.add(display);
        updateDisplay();
        return middlePanelSetup;
    }

    // EFFECTS: Updates the leaderboard display
    private void updateDisplay() {
        display.setText(formatLeaderboard());
    }

    // EFFECTS: Formats the leaderboard
    private String formatLeaderboard() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><p align=center>");
        sb.append("<br>");
        sb.append("TEAM LEADERBOARD");
        sb.append("<br>");
        sb.append("<br>");
        for (Profile p: team) {
            sb.append(p.getName()); // get name
            sb.append(" - ");
            sb.append(p.getPoints()); // get points
            sb.append("<br>");
        }
        sb.append("</p></html>");
        return sb.toString();
    }

    // ~~~ RIGHT PANEL ~~~

    // EFFECTS: Sets up the right panel with two sub-panels
    private Component setUpRightPanel() {
        JPanel rightPanelSetup = new JPanel();
        JPanel teammatePanel = new JPanel();
        JPanel entryPanel = new JPanel();

        rightPanelSetup.setLayout(new GridLayout(2, 1));
        rightPanelSetup.add(teammatePanel);
        rightPanelSetup.add(entryPanel);
        setUpTeammatePanel(teammatePanel);
        setUpEntryPanel(entryPanel);

        return rightPanelSetup;
    }

    // EFFECTS: Sets up the Log Entry panel
    private void setUpEntryPanel(JPanel entryPanel) {
        JLabel entryTitle = new JLabel("LOG ENTRY");
        entryPanel.setLocation(0, SCREEN_HEIGHT / 2);
        entryPanel.add(entryTitle);
        entryPanel.add(createEntryFields());
        addButton(entryPanel, "Log Entry", "log");
    }

    // EFFECTS: Sets up the Manage Teammates panel
    private void setUpTeammatePanel(JPanel teammatePanel) {
        JLabel teammatesTitle = new JLabel(formatTeammateTitle());
        JPanel namePanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        teammatePanel.add(teammatesTitle);
        teammatePanel.add(namePanel);
        teammatePanel.add(buttonPanel);

        nameLabel = new JLabel("Name: ");
        nameField.setColumns(10);
        namePanel.add(nameLabel);
        namePanel.add(nameField);

        addButton(buttonPanel, "Add", "add");
        addButton(buttonPanel, "Remove", "remove");
    }

    // EFFECTS: Formats the Manage Teammates title
    private String formatTeammateTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><p align=center>");
        sb.append("<br>");
        sb.append("MANAGE TEAMMATES");
        sb.append("<br>");
        sb.append("</p></html>");
        return sb.toString();
    }

    // EFFECTS: Creates the fields for Log Entry panel
    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {
                "Action: ",
                "Comment: ",
                "Teammate: "
        };

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        initializeEntryFields(fields, fieldNum);
        pairLabelsAndFields(panel, labelStrings, labels, fields);
        SpringUtilities.makeCompactGrid(panel,
                labelStrings.length, 2,
                GAP, GAP, //init x,y
                GAP, GAP / 2);//xpad, ypad
        return panel;
    }

    // EFFECTS: Initializes the fields for Log Entry panel
    private void initializeEntryFields(JComponent[] fields, int fieldNum) {
        String[] actions = getActions();
        actionSpinner = new JSpinner(new SpinnerListModel(actions));
        fields[fieldNum++] = actionSpinner;

        commentField  = new JTextField();
        commentField.setColumns(10);
        fields[fieldNum++] = commentField;

        teammateField = new JTextField();
        teammateField.setColumns(10);
        fields[fieldNum++] = teammateField;
    }

    // EFFECTS: Gets the actions for the action spinner field
    private String[] getActions() {
        String[] stateActions = {
                "Copywriting (150pts)",
                "Marketing (100pts)",
                "Research (100pts)",
                "Bonus (50pts)"
        };
        return stateActions;
    }

    // EFFECTS: Associate label/field pairs, add everything, and lay it out in Log Entry panel
    private void pairLabelsAndFields(JPanel panel, String[] labelStrings, JLabel[] labels, JComponent[] fields) {
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                    JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);
        }
    }

    // ~~~ ACTIONS ~~~

    // EFFECTS: Processes what button was pushed and performs the associated action and updates the leaderboard
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("quit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("add")) {
            addTeammate();
        } else if (e.getActionCommand().equals("remove")) {
            removeTeammate();
        } else if (e.getActionCommand().equals("log")) {
            logEntry();
        } else if (e.getActionCommand().equals("save")) {
            save();
        } else if (e.getActionCommand().equals("load")) {
            load(leaderboard);
        } else if (e.getActionCommand().equals("reset")) {
            Leaderboard.resetLeaderboard(team);
        }
        updateDisplay();
    }

    // EFFECTS: Adds a teammate to the leaderboard
    private void addTeammate() {
        String name = nameField.getText();
        Profile newTeammate = new Profile(name);
        leaderboard.addProfile(newTeammate);
        leaderboard.sortLeaderboard(team);
        nameField.setText("");
    }

    // EFFECTS: Removes a teammate from the leaderboard
    private void removeTeammate() {
        String name = nameField.getText();

        try {
            leaderboard.removeProfile(name);
        } catch (ConcurrentModificationException | NotOnLeaderboardException e) {
            //do nothing
        }
        leaderboard.sortLeaderboard(team);
        nameField.setText("");
    }

    // EFFECTS: Logs an entry for a particular teammate and adds points to their profile
    private void logEntry() {
        String actionType = (String)actionSpinner.getValue();
        String comment = commentField.getText();
        String name = teammateField.getText();

        Entry entry = new Entry(actionType, comment, name);
        for (Profile teammate : team) {
            if (teammate.getName().contains(name)) {
                teammate.addToEntryList(entry);
                if (actionType == "Copywriting (150pts)") {
                    teammate.addPoints(150);
                } else if (entry.getActionType() == "Marketing (100pts)") {
                    teammate.addPoints(100);
                } else if (entry.getActionType() == "Research (100pts)") {
                    teammate.addPoints(100);
                } else if (entry.getActionType() == "Bonus (50pts)") {
                    teammate.addPoints(50);
                }
            }
        }
        leaderboard.sortLeaderboard(team);
        commentField.setText("");
        teammateField.setText("");
    }

    // EFFECTS: Saves the leaderboard to file
    private void save() {
        try {
            leaderboard.sortLeaderboard(team);
            jsonWriter.open();
            jsonWriter.write(leaderboard);
            jsonWriter.close();
            playSaveSound();
        } catch (IOException e) {
            // do nothing
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads leaderboard from file
    private void load(Leaderboard currentLeaderboard) {
        try {
            leaderboard = jsonReader.read(currentLeaderboard);
            playLoadSound();
        } catch (NotOnLeaderboardException | IOException e) {
            // do nothing
        }
    }

    // Modelled after the example in http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
    // EFFECTS: plays the saving sound
    private void playSaveSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(SAVE_SOUND).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
        }
    }

    // Modelled after the example in http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
    // EFFECTS: plays the loading sound
    private void playLoadSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(LOAD_SOUND).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
        }
    }

    // EFFECTS: Runs the GUI
    public static void main(String[] args) {
        new TeamLeaderboardGUI();
    }


}
