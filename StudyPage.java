package Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class StudyPage extends JPanel {
    private JButton goBackButton;
    private JLabel wordLabel;
    private JTextField answerField;
    private String currentWord;
    private String currentTranslation;
    private int currentCorrectInARow;
    private int currentWordId;

    private Connection connectionWithDatabase;
    private ActionListener goBackAction;

    public StudyPage(ActionListener goBackAction) {
        this.goBackAction = goBackAction;
        this.setLayout(new BorderLayout());
        initializeDatabase();
        loadNextWord();
    }

    private void initializeDatabase() {
        try {
            connectionWithDatabase = DriverManager.getConnection("jdbc:sqlite:words.db");
            String createTableSQL = "CREATE TABLE IF NOT EXISTS Words (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "word TEXT NOT NULL, " +
                    "translation TEXT NOT NULL, " +
                    "correctInARow INTEGER DEFAULT 0, " +
                    "whenUsed INTEGER DEFAULT 0)";
            try (Statement stmt = connectionWithDatabase.createStatement()) {
                stmt.execute(createTableSQL);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addGoBackButton(ActionListener goBackAction) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        ImageIcon returnIcon = new ImageIcon("rsrc/returnIcon.png");
        Image returnIconScaled = returnIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        goBackButton = new JButton(new ImageIcon(returnIconScaled));
        goBackButton.setContentAreaFilled(false);
        goBackButton.setBorderPainted(false);
        goBackButton.addActionListener(goBackAction);

        topPanel.add(goBackButton);
        this.add(topPanel, BorderLayout.NORTH);
    }

    private void loadNextWord() {
        String query = "SELECT id, word, translation, correctInARow, whenUsed " +
                "FROM Words " +
                "ORDER BY CASE WHEN correctInARow = 0 THEN 0 ELSE 1 END, " +
                "whenUsed DESC " +
                "LIMIT 1";

        try (Statement stmt = connectionWithDatabase.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                currentWordId = rs.getInt("id");
                currentWord = rs.getString("word");
                currentTranslation = rs.getString("translation");
                currentCorrectInARow = rs.getInt("correctInARow");

                displayWord();
            } else {
                showNoWordsMessage();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayWord() {
        this.removeAll();

        addGoBackButton(this.goBackAction);

        wordLabel = new JLabel(currentWord);
        wordLabel.setFont(new Font("Arial", Font.PLAIN, 38));
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        answerField = new JTextField(20);
        answerField.setFont(new Font("Arial", Font.PLAIN, 30));
        answerField.setHorizontalAlignment(SwingConstants.CENTER);
        answerField.addActionListener(this::handleAnswer);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(wordLabel);
        centerPanel.add(Box.createVerticalStrut(70));
        centerPanel.add(answerField);
        centerPanel.add(Box.createVerticalGlue());

        this.add(centerPanel, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    private void handleAnswer(ActionEvent e) {
        String answer = answerField.getText().trim();
        answerField.setText("");

        if (answer.equalsIgnoreCase(currentTranslation)) {
            currentCorrectInARow++;
            JOptionPane.showMessageDialog(this, "Correct!", "Feedback", JOptionPane.INFORMATION_MESSAGE);
        } else {
            currentCorrectInARow = 0;
            JOptionPane.showMessageDialog(this, "Incorrect. The correct answer was: " + currentTranslation, "Feedback", JOptionPane.WARNING_MESSAGE);
        }

        updateWordData();
        updateWhenUsedCount();
        loadNextWord();
    }

    private void updateWordData() {
        String updateSQL = "UPDATE Words SET correctInARow = ?, whenUsed = 0 WHERE id = ?";
        try (PreparedStatement pstmt = connectionWithDatabase.prepareStatement(updateSQL)) {
            pstmt.setInt(1, currentCorrectInARow);
            pstmt.setInt(2, currentWordId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateWhenUsedCount() {
        String updateWhenUsedSQL = "UPDATE Words SET whenUsed = whenUsed + 1 WHERE id != ?";
        try (PreparedStatement pstmt = connectionWithDatabase.prepareStatement(updateWhenUsedSQL)) {
            pstmt.setInt(1, currentWordId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showNoWordsMessage() {
        JLabel noWordsLabel = new JLabel("You need to add words first.");
        noWordsLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        noWordsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(noWordsLabel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
