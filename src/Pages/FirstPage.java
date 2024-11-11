package Pages;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class FirstPage extends JPanel {
    JButton learnButton;
    JButton addWordsButton;
    JButton editWordsButton;

    static final int BUTTON_WIDTH = 250;
    static final int BUTTON_HEIGHT = 80;

    public FirstPage(ActionListener buttonListener) {
        learnButton = new JButton("STUDY");
        addWordsButton = new JButton("ADD WORDS");
        editWordsButton = new JButton("EDIT WORDS");

        Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        learnButton.setPreferredSize(buttonSize);
        learnButton.setMinimumSize(buttonSize);
        learnButton.setMaximumSize(buttonSize);

        addWordsButton.setPreferredSize(buttonSize);
        addWordsButton.setMinimumSize(buttonSize);
        addWordsButton.setMaximumSize(buttonSize);

        editWordsButton.setPreferredSize(buttonSize);
        editWordsButton.setMinimumSize(buttonSize);
        editWordsButton.setMaximumSize(buttonSize);

        learnButton.setBorder(new LineBorder(Color.BLACK, 4, true));
        addWordsButton.setBorder(new LineBorder(Color.BLACK, 4, true));
        editWordsButton.setBorder(new LineBorder(Color.BLACK, 4, true));

        learnButton.setFont(new Font("Arial", Font.BOLD, 18));
        addWordsButton.setFont(new Font("Arial", Font.BOLD, 18));
        editWordsButton.setFont(new Font("Arial", Font.BOLD, 18));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("WELCOME TO E-LEARNING");
        welcomeLabel.setFont(new Font("Aharoni", Font.PLAIN, 50));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        learnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addWordsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editWordsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        learnButton.setBackground(Color.PINK);
        addWordsButton.setBackground(Color.PINK);
        editWordsButton.setBackground(Color.PINK);

        learnButton.addActionListener(e -> buttonListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "STUDY")));
        addWordsButton.addActionListener(e -> buttonListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "ADD_WORDS")));
        editWordsButton.addActionListener(e -> buttonListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "EDIT_WORDS")));

        this.setBackground(Color.LIGHT_GRAY);
        this.add(Box.createVerticalStrut(70));
        this.add(welcomeLabel);
        this.add(Box.createVerticalStrut(60));
        this.add(learnButton);
        this.add(Box.createVerticalStrut(50));
        this.add(addWordsButton);
        this.add(Box.createVerticalStrut(50));
        this.add(editWordsButton);
    }
}
