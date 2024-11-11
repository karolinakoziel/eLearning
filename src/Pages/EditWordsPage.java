package Pages;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EditWordsPage extends JPanel {
    JButton goBackButton;
    JTextField idInputField;
    JButton deleteButton;
    JTextArea wordsDisplayArea;
    Connection connectionWithDatabase;

    public EditWordsPage(ActionListener goBackAction) {
        this.setLayout(new BorderLayout());
        initializeDatabase();
        addGoBackButton(goBackAction);
        addComponents();
        loadWords();
    }

    private void initializeDatabase() {
        try {
            connectionWithDatabase = DriverManager.getConnection("jdbc:sqlite:words.db");

            String createTableQuery = "CREATE TABLE IF NOT EXISTS Words (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "word TEXT NOT NULL, " +
                    "translation TEXT NOT NULL, " +
                    "correctInARow INTEGER DEFAULT 0, " +
                    "whenUsed INTEGER DEFAULT 0)";
            try (Statement stmt = connectionWithDatabase.createStatement()) {
                stmt.execute(createTableQuery);
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

    private void addComponents() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel instructionLabel = new JLabel("Enter the ID of the word you want to delete:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        idInputField = new JTextField(12);
        idInputField.setMaximumSize(new Dimension(250, 45));
        idInputField.setFont(new Font("Arial", Font.PLAIN, 20));
        idInputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        deleteButton = new JButton("Delete Word ");
        deleteButton.setPreferredSize(new Dimension(100,40));
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.addActionListener(this::deleteWordById);
        deleteButton.setBackground(Color.PINK);
        deleteButton.setBorder(new LineBorder(Color.BLACK, 2, true));

        wordsDisplayArea = new JTextArea(10, 30);
        wordsDisplayArea.setEditable(false);
        wordsDisplayArea.setFont(new Font("Arial", Font.PLAIN, 16));
        wordsDisplayArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(wordsDisplayArea);

        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(idInputField);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(deleteButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(scrollPane);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void loadWords() {
        StringBuilder wordsList = new StringBuilder();
        String query = "SELECT id, word, translation FROM Words";
        boolean hasWords = false;

        try (Statement stmt = connectionWithDatabase.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                hasWords = true;
                int id = rs.getInt("id");
                String word = rs.getString("word");
                String translation = rs.getString("translation");
                wordsList.append("ID: ").append(id).append(" | Word: ").append(word)
                        .append(" | Translation: ").append(translation).append("\n");
            }

            if (hasWords) {
                wordsDisplayArea.setText(wordsList.toString());
            } else {
                showNoWordsMessage();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteWordById(ActionEvent e) {
        String idText = idInputField.getText().trim();

        try {
            int id = Integer.parseInt(idText);
            String deleteQuery = "DELETE FROM Words WHERE id = ?";

            try (PreparedStatement pstmt = connectionWithDatabase.prepareStatement(deleteQuery)) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Word deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No word found with that ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                loadWords();
                idInputField.setText("");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showNoWordsMessage() {
        wordsDisplayArea.setText("No words in the database. Please add words first.");
    }
}
