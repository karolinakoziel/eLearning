package GUI;

import Pages.AddWordsPage;
import Pages.EditWordsPage;
import Pages.FirstPage;
import Pages.StudyPage;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class Frame extends JPanel {
    FirstPage firstPage;

    Frame() {
        this.firstPage = new FirstPage(this::modeChoice);
        this.setLayout(new BorderLayout());
        this.add(firstPage, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(900, 600));
    }

    private void modeChoice(ActionEvent e) {
        if (e.getActionCommand().equals("STUDY")) {
            StudyPage studyPage = new StudyPage(this::goBack);
            this.remove(firstPage);
            this.add(studyPage, BorderLayout.CENTER);
        } else if (e.getActionCommand().equals("ADD_WORDS")) {
            AddWordsPage addWordsPage = new AddWordsPage(this::goBack);
            this.remove(firstPage);
            this.add(addWordsPage, BorderLayout.CENTER);
        } else {
            EditWordsPage editWordsPage = new EditWordsPage(this::goBack);
            this.remove(firstPage);
            this.add(editWordsPage, BorderLayout.CENTER);
        }
        this.revalidate();
        this.repaint();
    }

    private void goBack(ActionEvent e) {
        this.removeAll();
        this.add(firstPage, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
