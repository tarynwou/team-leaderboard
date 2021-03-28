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

        JPanel leftPanel = new JPanel();
//            public Dimension getMaximumSize() {
//                Dimension pref = getPreferredSize();
//                return new Dimension(Integer.MAX_VALUE,
//                        pref.height);
//            }
//        };
        JPanel middlePanel;
        JPanel rightPanel;

        leftPanel.setPreferredSize(new Dimension(SCREEN_WIDTH / 4, SCREEN_HEIGHT));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.add(createLeftButtons());

        add(leftPanel);


        setLayout(new FlowLayout());
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

    protected JComponent createLeftButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.TRAILING));

        JButton button = new JButton("Quit");
        button.addActionListener(this);
        button.setActionCommand("quit");
        panel.add(button);

        button = new JButton("Save");
        button.addActionListener(this);
        button.setActionCommand("save");
        panel.add(button);

        button = new JButton("Load");
        button.addActionListener(this);
        button.setActionCommand("load");
        panel.add(button);

        button = new JButton("Reset");
        button.addActionListener(this);
        button.setActionCommand("reset");
        panel.add(button);

        //Match the SpringLayout's gap, subtracting 5 to make
        //up for the default gap FlowLayout provides.
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0,
                GAP - 5, GAP - 5));
        return panel;
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
