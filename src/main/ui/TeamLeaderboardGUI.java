package ui;

import exceptions.NotOnLeaderboardException;
import model.Leaderboard;
import model.Profile;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;


// CREDITS: LabelChanger and TextInputDemo

// Creates the graphical user interfaces for Team Leaderboard
public class TeamLeaderboardGUI extends JFrame implements ActionListener {
    // Save and reload fields
    private static final String JSON_STORE = "./data/leaderboard.json";
    private JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    private JsonReader jsonReader = new JsonReader(JSON_STORE);

    //TODO: redo this lol + add documentation
    private JLabel label;
    private JTextField field;

    private ArrayList<Profile> team = new ArrayList<Profile>();
    private Leaderboard leaderboard = new Leaderboard(team);

    // Fields
    JTextField nameField = new JTextField();
    JTextField commentField = new JTextField();
    JTextField teammateField = new JTextField();
    JSpinner actionField;

    // Labels
    JLabel nameLabel;
    JLabel actionLabel;
    JLabel commentLabel;
    JLabel teammateLabel;
    JLabel display;

    private static int GAP = 10;
    private static int SCREEN_WIDTH = 800;
    private static int SCREEN_HEIGHT = 400;


    public TeamLeaderboardGUI() {
        super("Team Leaderboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        setUp();

        JButton btn = new JButton("Add");
        btn.setActionCommand("myButton");
        btn.addActionListener(this); // Sets "this" object as an action listener for btn
        // so that when the btn is clicked,
        // this.actionPerformed(ActionEvent e) will be called.
        // You could also set a different object, if you wanted
        // a different object to respond to the button click
        label = new JLabel("flag");
        field = new JTextField(5);
        add(field);
        add(btn);
        add(label);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    private void setUp() {
        JPanel leftPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setBounds(0, 0, SCREEN_WIDTH / 4, SCREEN_HEIGHT);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.add(createLeftButtons());
        leftPanel.setBackground(Color.BLUE);

        middlePanel.setBounds(SCREEN_WIDTH / 4, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
        middlePanel.setBackground(Color.CYAN);
        middlePanel.add(setUpMiddlePanel());

        rightPanel.setBounds(SCREEN_WIDTH * 3 / 4, 0, SCREEN_WIDTH / 4, SCREEN_HEIGHT);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        rightPanel.add(setUpRightPanel());
        rightPanel.setBackground(Color.GREEN);

        add(leftPanel);
        add(middlePanel);
        add(rightPanel);
    }

    private Component setUpMiddlePanel() {
        JPanel middlePanelSetup = new JPanel();
        display = new JLabel();
        middlePanelSetup.add(display);
        updateDisplay();
        return middlePanelSetup;
    }

    private void updateDisplay() {
        display.setText(formatLeaderboard());
    }

    private String formatLeaderboard() {
        StringBuffer sb = new StringBuffer();
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


    private Component setUpRightPanel() {
        JPanel rightPanelSetup = new JPanel();
        JPanel teammatePanel = new JPanel();
        JPanel entryPanel = new JPanel();
        JLabel teammatesTitle = new JLabel("TEAMMATES");
        JLabel entryTitle = new JLabel("LOG ENTRY");

        rightPanelSetup.setBounds(0, 0, SCREEN_WIDTH / 4, SCREEN_HEIGHT);
        rightPanelSetup.add(teammatePanel);
        rightPanelSetup.add(entryPanel);
        rightPanelSetup.setBackground(Color.PINK);

        teammatePanel.setLayout(new BoxLayout(teammatePanel, BoxLayout.Y_AXIS));
        //TODO: setBounds doesn't work
        teammatePanel.setBounds(0, 0, SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2);
        teammatePanel.setBackground(Color.ORANGE);
        teammatePanel.add(teammatesTitle);
        addTextField(teammatePanel, nameLabel, nameField, "Name: ");
        addButton(teammatePanel, "Add", "add");
        addButton(teammatePanel, "Remove", "remove");

//        entryPanel.setLayout(new SpringLayout(teammatePanel, SpringLayout.HEIGHT));
//        entryPanel.setLayout(new SpringLayout());
//        entryPanel.makeCompactGrid();
        //TODO: setBounds doesn't work
        entryPanel.setBounds(0, SCREEN_HEIGHT / 2, SCREEN_WIDTH / 4, SCREEN_HEIGHT / 2);
        entryPanel.setBackground(Color.RED);
        entryPanel.add(entryTitle);
        actionLabel = new JLabel("Action: ");
        actionField = new JSpinner();
        entryPanel.add(actionLabel);
        entryPanel.add(actionField);


        addTextField(entryPanel, commentLabel, commentField, "Comment: ");
        addTextField(entryPanel, teammateLabel, teammateField, "Teammate: ");

        return rightPanelSetup;
    }

    public void addTextField(JPanel panel, JLabel label, JTextField field, String text) {
        label = new JLabel(text);
        field.setColumns(10);
        panel.add(label);
        panel.add(field);
    }

    protected JComponent createLeftButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        addButton(panel, "Quit", "quit");

        addButton(panel, "Save", "save");

        addButton(panel, "Load", "load");

        addButton(panel, "Reset", "reset");

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                GAP - 5, GAP - 5));
        return panel;
    }

    private void addButton(JPanel panel, String name, String command) {
        JButton button;
        button = new JButton(name);
        button.addActionListener(this);
        button.setActionCommand(command);
        panel.add(button);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("myButton")) {
            label.setText(field.getText());
        } else if (e.getActionCommand().equals("quit")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("add")) {
            addTeammate();
        } else if (e.getActionCommand().equals("remove")) {
            removeTeammate();
        } else if (e.getActionCommand().equals("save")) {
            save();
        } else if (e.getActionCommand().equals("load")) {
            load(leaderboard);
        } else if (e.getActionCommand().equals("reset")) {
            Leaderboard.resetLeaderboard(team);
        }
        updateDisplay();
    }

    private void addTeammate() {
        String name = nameField.getText();
        Profile newTeammate = new Profile(name);
        leaderboard.addProfile(newTeammate);
        leaderboard.sortLeaderboard(team);
        nameField.setText("");
    }

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

    // EFFECTS: saves the leaderboard to file
    private void save() {
        try {
            leaderboard.sortLeaderboard(team);
            jsonWriter.open();
            jsonWriter.write(leaderboard);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            // do nothing
        }
    }

    // MODIFIES: this
    // EFFECTS: loads leaderboard from file
    private void load(Leaderboard currentLeaderboard) {
        try {
            leaderboard = jsonReader.read(currentLeaderboard);
        } catch (NotOnLeaderboardException | IOException e) {
            // do nothing
        }
    }

    public static void main(String[] args) {
        new TeamLeaderboardGUI();
    }
}
