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
public class TeamLeaderboardGUI extends JFrame implements ActionListener, FocusListener {
    // Save and reload fields
    private static final String JSON_STORE = "./data/leaderboard.json";
    private final JsonWriter jsonWriter = new JsonWriter(JSON_STORE);
    private final JsonReader jsonReader = new JsonReader(JSON_STORE);

    // Sounds
    private static String SAVE_SOUND = "./data/text_sounds.wav";
    private static String LOAD_SOUND = "./data/super_mario_mushroom.wav";

    //TODO: redo this lol + add documentation
    private JLabel label;
    private JTextField field;

    private ArrayList<Profile> team = new ArrayList<Profile>();
    private Leaderboard leaderboard = new Leaderboard(team);

    // Fields
    JTextField nameField = new JTextField();
    JTextField commentField = new JTextField();
    JTextField teammateField = new JTextField();
    JSpinner actionSpinner;

    // Labels
    JLabel nameLabel;
    //TODO: review and delete these
//    JLabel actionLabel;
//    JLabel commentLabel;
//    JLabel teammateLabel;
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

        leftPanel.setBounds(0, 0, SCREEN_WIDTH / 8, SCREEN_HEIGHT);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createLeftButtons());
//        leftPanel.setBackground(Color.BLUE);

        middlePanel.setBounds(SCREEN_WIDTH / 8, 0, SCREEN_WIDTH / 2, SCREEN_HEIGHT);
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.PAGE_AXIS));
        middlePanel.setBackground(Color.CYAN);
        middlePanel.add(setUpMiddlePanel());

        rightPanel.setBounds(SCREEN_WIDTH * 5 / 8, 0, SCREEN_WIDTH * 3 / 8, SCREEN_HEIGHT);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
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


    private Component setUpRightPanel() {
        JPanel rightPanelSetup = new JPanel();
        JPanel teammatePanel = new JPanel();
        JPanel entryPanel = new JPanel();

        rightPanelSetup.setLayout(new GridLayout(2, 1));
        rightPanelSetup.add(teammatePanel);
        rightPanelSetup.add(entryPanel);
        rightPanelSetup.setBackground(Color.PINK);

        setUpTeammatePanel(teammatePanel);
        setUpEntryPanel(entryPanel);

        return rightPanelSetup;
    }

    private void setUpEntryPanel(JPanel entryPanel) {
        JLabel entryTitle = new JLabel("LOG ENTRY");
        entryPanel.setLocation(0, SCREEN_HEIGHT / 2);
        entryPanel.setBackground(Color.DARK_GRAY);
        entryPanel.add(entryTitle);
        entryPanel.add(createEntryFields());
        addButton(entryPanel, "Log Entry", "log");
    }

    private void setUpTeammatePanel(JPanel teammatePanel) {
        JLabel teammatesTitle = new JLabel("TEAMMATES");
        teammatePanel.setLayout(new BoxLayout(teammatePanel, BoxLayout.Y_AXIS));
        teammatePanel.setBackground(Color.ORANGE);
        teammatePanel.add(teammatesTitle);

        nameLabel = new JLabel("Name: ");
        nameField.setColumns(10);
        teammatePanel.add(nameLabel);
        teammatePanel.add(nameField);
        //TODO: Add button panel
        addButton(teammatePanel, "Add", "add");
        addButton(teammatePanel, "Remove", "remove");
    }

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

    private void pairLabelsAndFields(JPanel panel, String[] labelStrings, JLabel[] labels, JComponent[] fields) {
        // Associate label/field pairs, add everything, and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i],
                    JLabel.TRAILING);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);
        }
    }

    private void initializeEntryFields(JComponent[] fields, int fieldNum) {
        //Create the text field and set it up.
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

    private String[] getActions() {
        String[] stateActions = {
                "Copywriting (150pts)",
                "Marketing (100pts)",
                "Research (100pts)",
                "Bonus (50pts)"
        };
        return stateActions;
    }

    //TODO: review this and delete
    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor)editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: "
                    + spinner.getEditor().getClass()
                    + " isn't a descendant of DefaultEditor");
            return null;
        }
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
            label.setText(teammateField.getText());
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
        } else if (e.getActionCommand().equals("log")) {
            logEntry();
        }
        updateDisplay();
    }

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
            playSaveSound();
        } catch (IOException e) {
            // do nothing
        }
    }

    // MODIFIES: this
    // EFFECTS: loads leaderboard from file
    private void load(Leaderboard currentLeaderboard) {
        try {
            leaderboard = jsonReader.read(currentLeaderboard);
            playLoadSound();
        } catch (NotOnLeaderboardException | IOException e) {
            // do nothing
        }
    }

    // Modelled after the example in http://suavesnippets.blogspot.com/2011/06/add-sound-on-jbutton-click-in-java.html
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

    public static void main(String[] args) {
        new TeamLeaderboardGUI();
    }

    @Override
    public void focusGained(FocusEvent e) {
        Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
            selectItLater(c);
        } else if (c instanceof JTextField) {
            ((JTextField)c).selectAll();
        }
    }

    private void selectItLater(Component c) {
        if (c instanceof JFormattedTextField) {
            final JFormattedTextField ftf = (JFormattedTextField)c;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    ftf.selectAll();
                }
            });
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        // do nothing
    }
}
