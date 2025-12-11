import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.NoSuchElementException;
import java.util.Random;

class PlayersGUI {
    int level, score;
    char operator;

    final int T_QUESTIONS = 10;
    final byte EOF = -1;

    Random r_generator = new Random();

    JFrame frame;
    CardLayout card;
    JPanel mainPanel;

    // UI components for operator screen
    JComboBox<String> operatorBox;

    // UI components for level screen
    JTextField levelField;

    // UI for quiz screen
    JLabel questionLabel;
    JTextField answerField;
    JButton submitBtn;

    int currentQuestion = 0;
    int correctAnswer;
    int attempts = 0;

    int x, y;

    PlayersGUI() {
        level = 0;
        score = 0;
        operator = '+';

        frame = new JFrame("Math Quiz Game");
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        card = new CardLayout();
        mainPanel = new JPanel(card);

        welcomeScreen();
        operatorScreen();
        levelScreen();
        quizScreen();
        resultScreen();

        card.show(mainPanel, "welcome");
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    //==========================WELCOME SCREEN ============================


    void welcomeScreen() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(new Color(245, 248, 255)); // soft background

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 0, 10, 0);

    JLabel title = new JLabel("Professor");
    title.setFont(new Font("Segoe UI", Font.BOLD, 34));

    JLabel subtitle = new JLabel("A math quiz game");
    subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 18));

    JButton playBtn = new JButton("PLAY");
    playBtn.setPreferredSize(new Dimension(150, 50));
    playBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));

    playBtn.addActionListener(e -> {
        card.show(mainPanel, "operator");
    });

    gbc.gridy = 0;
    panel.add(title, gbc);

    gbc.gridy = 1;
    panel.add(subtitle, gbc);

    gbc.gridy = 2;
    gbc.insets = new Insets(20, 0, 0, 0); // extra margin
    panel.add(playBtn, gbc);

    mainPanel.add(panel, "welcome");
}


    // ========================= OPERATOR SELECTION ===========================
 void operatorScreen() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(new Color(240, 248, 255));
    GridBagConstraints gbc = new GridBagConstraints();

    JLabel label = new JLabel("Choose an operator");
    label.setFont(new Font("Segoe UI", Font.BOLD, 26));
    label.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

    // MAIN CONTAINER (like a <div>)
    JPanel container = new JPanel();
    container.setLayout(new GridLayout(2, 2, 20, 20));
    container.setPreferredSize(new Dimension(300, 250)); // fixed height & width
    container.setOpaque(false); // transparent bg

    String ops[] = {"+", "-", "*", "%"};
    for (String op : ops) {
        JButton playBtn = new JButton(op);
        playBtn.setFont(new Font("Segoe UI", Font.PLAIN, 36));

        playBtn.addActionListener(e -> {
            operator = op.charAt(0);
            card.show(mainPanel, "level");
        });

        container.add(playBtn);
    }

    gbc.gridy = 0;
    panel.add(label, gbc);

    gbc.gridy = 1;
    panel.add(container, gbc);

    mainPanel.add(panel, "operator");
    }

    // ========================= LEVEL SELECTION ===========================

void levelScreen() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(new Color(240, 255, 240));
    GridBagConstraints gbc = new GridBagConstraints();

    JLabel label = new JLabel("Choose a Level");
    label.setFont(new Font("Segoe UI", Font.BOLD, 26));
    label.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

    JPanel container = new JPanel(new GridLayout(1, 3, 20, 20));
    container.setPreferredSize(new Dimension(350, 100));
    container.setOpaque(false);

    for (int i = 1; i <= 3; i++) {
        int levelNum = i;

        JButton btn = new JButton(String.valueOf(i));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            level = levelNum;
            startQuestion();
            card.show(mainPanel, "quiz");
        });

        container.add(btn);
    }

    gbc.gridy = 0;
    panel.add(label, gbc);

    gbc.gridy = 1;
    panel.add(container, gbc);

    mainPanel.add(panel, "level");
}

// =============================== QUIZ SCREEN ================================

void quizScreen() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(new Color(235, 245, 255)); // soft blue
    GridBagConstraints gbc = new GridBagConstraints();

    // QUESTION LABEL
    questionLabel = new JLabel("Question Appears Here");
    questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
    questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

    // INPUT FIELD
    answerField = new JTextField(10);
    answerField.setFont(new Font("Segoe UI", Font.BOLD, 28));
    answerField.setHorizontalAlignment(JTextField.CENTER);
    answerField.setPreferredSize(new Dimension(250, 55));

    // KEYBOARD SUPPORT
    answerField.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                checkAnswer();
            }
        }
    });

    // NUMPAD GRID (4x3)
    JPanel keypad = new JPanel(new GridLayout(4, 3, 15, 15));
    keypad.setPreferredSize(new Dimension(350, 300));
    keypad.setOpaque(false);

    // BUTTONS 1–9
    for (int i = 1; i <= 9; i++) {
        keypad.add(makeKeyButton(String.valueOf(i)));
    }

    // CLEAR BUTTON
    keypad.add(makeSpecialButton("C", () -> {
        answerField.setText("");
    }));

    // BUTTON 0
    keypad.add(makeKeyButton("0"));

    // DELETE BUTTON
    keypad.add(makeSpecialButton("DEL", () -> {
        String text = answerField.getText();
        if (!text.isEmpty()) {
            answerField.setText(text.substring(0, text.length() - 1));
        }
    }));

    // SUBMIT BUTTON
    submitBtn = new JButton("Submit");
    submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 22));
    submitBtn.setPreferredSize(new Dimension(150, 50));
    submitBtn.setBackground(new Color(70, 130, 180));
    submitBtn.setForeground(Color.WHITE);
    submitBtn.setFocusPainted(false);
    submitBtn.addActionListener(e -> checkAnswer());

    // ADD COMPONENTS
    gbc.gridy = 0;
    panel.add(questionLabel, gbc);

    gbc.gridy = 1;
    panel.add(answerField, gbc);

    gbc.gridy = 2;
    gbc.insets = new Insets(20, 0, 0, 0);
    panel.add(keypad, gbc);

    gbc.gridy = 3;
    gbc.insets = new Insets(25, 0, 0, 0);
    panel.add(submitBtn, gbc);

    mainPanel.add(panel, "quiz");
}

private JButton makeKeyButton(String text) {
    JButton btn = new JButton(text);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 26));
    btn.setFocusPainted(false);
    btn.setBackground(new Color(207, 226, 255));

    // Hover effect
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(180, 205, 255));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(207, 226, 255));
        }
    });

    btn.addActionListener(e -> {
        answerField.setText(answerField.getText() + text);
    });

    return btn;
}

private JButton makeSpecialButton(String text, Runnable action) {
    JButton btn = new JButton(text);
    btn.setFont(new Font("Segoe UI", Font.BOLD, 24));
    btn.setFocusPainted(false);
    btn.setBackground(new Color(255, 200, 200)); // light red

    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(255, 160, 160));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
            btn.setBackground(new Color(255, 200, 200));
        }
    });

    btn.addActionListener(e -> action.run());

    return btn;
}


// ========================= RESULT SCREEN ===========================

    JLabel scoreLabel;
    void resultScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 255));

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Quiz Results");
        title.setFont(new Font("Arial", Font.BOLD, 26));

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 22));

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridy = 1;
        panel.add(scoreLabel, gbc);

        gbc.gridy = 2;
        panel.add(exitBtn, gbc);

        mainPanel.add(panel, "result");
    }

    // ========================= LOGIC HANDLING ===========================

    int random_number_generator(int level) {
        if (level == 1)
            return r_generator.nextInt(0, 9);  
        else {
            int lower = (int) Math.pow(10, level - 1);
            int upper = (int) Math.pow(10, level) - 1;
            return r_generator.nextInt(lower, upper);
        }
    }

    void startQuestion() {
        if (currentQuestion >= T_QUESTIONS) {
            scoreLabel.setText("Score : " + score);
            card.show(mainPanel, "result");
            return;
        }

        attempts = 0;

        x = random_number_generator(level);
        y = (level == 1) ? r_generator.nextInt(1, 9) : random_number_generator(level);

        switch (operator) {
            case '+': correctAnswer = x + y; break;
            case '-': correctAnswer = x - y; break;
            case '*': correctAnswer = x * y; break;
            case '%': correctAnswer = x % y; break;
        }

        questionLabel.setText(x + " " + operator + " " + y + " = ?");
        answerField.setText("");
    }

    void checkAnswer() {
        int userAnswer;

        try {
            userAnswer = Integer.parseInt(answerField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Enter a valid number!");
            return;
        }

        if (userAnswer == correctAnswer) {
            score++;
            currentQuestion++;
            startQuestion();
        } else {
            attempts++;
            if (attempts < 3) {
                JOptionPane.showMessageDialog(frame, "Wrong answer!! \n Attempts left : " + (3 - attempts) );
                answerField.setText("");
            } else {
                JOptionPane.showMessageDialog(frame, "Correct Answer: " + correctAnswer);
                answerField.setText("");
                currentQuestion++;
                startQuestion();
            }
        }
    }
}

public class application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlayersGUI());
    }
}
