package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// CREDITS: LabelChanger and TextInputDemo

// Creates the graphical user interfaces for Team Leaderboard
public class TeamLeaderboardGUI extends JFrame implements ActionListener {
    //TODO: redo this lol
    private JLabel label;
    private JTextField field;


    //actual fields
    JTextField name;
    JTextField comment;
    JTextField teammate;
    JSpinner action;

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

        rightPanel.setBounds(SCREEN_WIDTH * 3 / 4, 0, SCREEN_WIDTH / 4, SCREEN_HEIGHT);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
//        rightPanel.add(setUpRightPanel());
        rightPanel.setBackground(Color.GREEN);

        add(leftPanel);
        add(middlePanel);
        add(rightPanel);
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
        }
    }

    public static void main(String[] args) {
        new TeamLeaderboardGUI();
    }
}
