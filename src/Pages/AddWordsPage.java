package Pages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddWordsPage extends JPanel {
    JButton goBackButton;
    private JLabel promptLabel;
    private JTextField inputField;
    private String enteredWord;
    private String enteredTranslation;
    private boolean isEnteringWord = true;
    private Connection connectionWithDatabase;
    public AddWordsPage(ActionListener goBackAction) {
        initializeDatabase();
        this.setLayout(new BorderLayout());
        addGoBackButton(goBackAction);
        addWordEntering();
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
            connectionWithDatabase.createStatement().execute(createTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addWordEntering() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);


        promptLabel = new JLabel("Enter new word");
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 36));
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField = new JTextField(20);
        inputField.setMaximumSize(new Dimension(300, 45));
        inputField.setFont(new Font("Arial", Font.PLAIN, 30));
        inputField.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField.addActionListener(this::handleInput);

        centerPanel.add(promptLabel);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(inputField);

        this.add(centerPanel, BorderLayout.CENTER);
    }

    private void handleInput(ActionEvent e) {
        String input = inputField.getText().trim();
        inputField.setText("");

        if (isEnteringWord) {
            enteredWord = input;
            promptLabel.setText("Enter translation");
            isEnteringWord = false;
        } else {
            enteredTranslation = input;
            writeToDatabase();
            promptLabel.setText("Enter new word");
            enteredWord = null;
            enteredTranslation = null;
            isEnteringWord = true;
        }
    }

    private void writeToDatabase() {
        if (enteredWord != null && enteredTranslation != null) {
            String insertSQL = "INSERT INTO Words (word, translation, correctInARow, whenUsed) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmt = connectionWithDatabase.prepareStatement(insertSQL)) {
                pstmt.setString(1, enteredWord);
                pstmt.setString(2, enteredTranslation);
                pstmt.setInt(3, 0);
                pstmt.setInt(4,0);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

}