import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class PlayersGUI {
    int level, score;
    char operator;

    final int T_QUESTIONS = 10;

    Random r_generator = new Random();

    JFrame frame;
    CardLayout card;
    JPanel mainPanel;

    // UI components
    JLabel questionLabel;
    JTextField answerField;
    JButton submitBtn;
    JLabel scoreLabel;

    int currentQuestion = 0;
    int correctAnswer;
    int attempts = 0;
    int x, y;

    PlayersGUI() {
        level = 0;
        score = 0;
        operator = '+';

        frame = new JFrame("Math Quiz Game");
        frame.setSize(450, 400); // Increased height slightly for better layout
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        card = new CardLayout();
        mainPanel = new JPanel(card);

        // Build static screens immediately
        welcomeScreen();
        operatorScreen();
        levelScreen();
        resultScreen(); 

        // Note: quizScreen() is built dynamically later 
        // to ensure the Keypad knows which operator was chosen!

        card.show(mainPanel, "welcome");
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // ========================== 1. WELCOME SCREEN ============================
    void welcomeScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); 
        gbc.gridx = 0; 

        JLabel icon = new JLabel("🎓"); 
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        gbc.gridy = 0;
        panel.add(icon, gbc);

        JLabel title = new JLabel("MATH PROFESSOR");
        title.setFont(new Font("Serif", Font.BOLD, 36)); 
        title.setForeground(new Color(44, 62, 80)); 
        gbc.gridy = 1;
        panel.add(title, gbc);

        JLabel mathSymbols = new JLabel("∑   π   √   x²   ÷");
        mathSymbols.setFont(new Font("Monospaced", Font.BOLD, 22));
        mathSymbols.setForeground(new Color(100, 149, 237)); 
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 20, 0); 
        panel.add(mathSymbols, gbc);

        JLabel subtitle = new JLabel("Sharpen your mind!");
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        subtitle.setForeground(Color.GRAY);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 15, 0); 
        panel.add(subtitle, gbc);

        JButton playBtn = new JButton("START QUIZ");
        playBtn.setPreferredSize(new Dimension(200, 55));
        playBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        playBtn.setBackground(new Color(46, 204, 113)); 
        playBtn.setForeground(Color.WHITE);
        playBtn.setFocusPainted(false);
        playBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        playBtn.addActionListener(e -> card.show(mainPanel, "operator"));

        gbc.gridy = 4;
        panel.add(playBtn, gbc);

        mainPanel.add(panel, "welcome");
    }

    // ========================= 2. OPERATOR SELECTION ===========================
    void operatorScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE); 
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Select Operation");
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(new Color(50, 50, 50)); 
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JPanel container = new JPanel(new GridLayout(2, 2, 15, 15)); 
        container.setPreferredSize(new Dimension(280, 200));
        container.setOpaque(false);

        String[] ops = {"+", "-", "*", "%"};

        for (String op : ops) {
            JButton btn = new JButton(op);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 32));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(230, 240, 255));
            btn.setForeground(new Color(0, 100, 200));
            btn.setBorder(BorderFactory.createEmptyBorder());

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(200, 225, 255));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(230, 240, 255));
                }
            });

            btn.addActionListener(e -> {
                operator = op.charAt(0);
                card.show(mainPanel, "level");
            });

            container.add(btn);
        }

        gbc.gridy = 0;
        panel.add(label, gbc);
        gbc.gridy = 1;
        panel.add(container, gbc);
        mainPanel.add(panel, "operator");
    }

    // ========================= 3. LEVEL SELECTION ===========================
    void levelScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE); 
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel label = new JLabel("Select Difficulty");
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(new Color(50, 50, 50));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        JPanel container = new JPanel(new GridLayout(1, 3, 20, 0)); 
        container.setPreferredSize(new Dimension(380, 80));
        container.setOpaque(false);

        for (int i = 1; i <= 3; i++) {
            int levelNum = i;
            JButton btn = new JButton(String.valueOf(i));
            btn.setFont(new Font("Segoe UI", Font.BOLD, 28));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(245, 245, 245)); 
            btn.setForeground(Color.BLACK);
            btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(220, 220, 220)); 
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(245, 245, 245));
                }
            });

            btn.addActionListener(e -> {
                level = levelNum;
                // Important: Build quiz UI here so it knows if it needs a minus button
                quizScreen(); 
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

    // ========================= 4. QUIZ SCREEN ===========================
    void quizScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE); 
        GridBagConstraints gbc = new GridBagConstraints();

        // --- 1. TOP HEADER (Exit Button) ---
        // JPanel header = new JPanel(new BorderLayout());
        // header.setBackground(Color.WHITE);
        // header.setPreferredSize(new Dimension(400, 30)); 

        JButton exitBtn = new JButton("EXIT");
        exitBtn.setPreferredSize(new Dimension(150, 45));
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setBackground(new Color(255, 116, 116));
        exitBtn.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        exitBtn.setFocusPainted(false);

        // ACTION: End game immediately and show current score
        exitBtn.addActionListener(e -> showResult());

        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.insets = new Insets(5, 10, 0, 10);

        // --- 2. QUESTION LABEL ---
        questionLabel = new JLabel("Question...");
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));

        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(questionLabel, gbc);

        // --- 3. ANSWER FIELD ---
        answerField = new JTextField(10);
        answerField.setFont(new Font("Segoe UI", Font.BOLD, 28));
        answerField.setHorizontalAlignment(JTextField.CENTER);
        answerField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        answerField.setPreferredSize(new Dimension(250, 50));
        
        answerField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) checkAnswer();
            }
        });

        gbc.gridy = 2;
        panel.add(answerField, gbc);

        // --- 4. KEYPAD (Dynamic) ---
        JPanel keypad = new JPanel(new GridLayout(4, 3, 10, 10)); 
        keypad.setPreferredSize(new Dimension(300, 220));
        keypad.setOpaque(false);

        for (int i = 1; i <= 9; i++) {
            keypad.add(makeKeyButton(String.valueOf(i)));
        }

        // DYNAMIC LOGIC: Show '-' button ONLY if operator is subtraction
        if (operator == '-') {
            keypad.add(makeKeyButton("-"));
        } else {
            keypad.add(makeSpecialButton("C", () -> answerField.setText("")));
        }

        keypad.add(makeKeyButton("0"));

        // Backspace/Delete Button
        keypad.add(makeSpecialButton("DEL", () -> {
            String text = answerField.getText();
            if (!text.isEmpty()) {
                answerField.setText(text.substring(0, text.length() - 1));
            }
        }));

        gbc.gridy = 3;
        gbc.insets = new Insets(15, 0, 0, 0);
        panel.add(keypad, gbc);

        // --- 5. BUTTONS AT BOTTOM (EXIT left, SUBMIT right) ---
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(300, 50));

        exitBtn.setPreferredSize(new Dimension(145, 45));
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setBackground(new Color(255, 116, 116));
        exitBtn.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(e -> showResult());

        buttonPanel.add(exitBtn, BorderLayout.WEST);

        submitBtn = new JButton("SUBMIT");
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        submitBtn.setPreferredSize(new Dimension(145, 45));
        submitBtn.setBackground(new Color(28, 255, 100)); 
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setFocusPainted(false);
        submitBtn.addActionListener(e -> checkAnswer());

        buttonPanel.add(submitBtn, BorderLayout.EAST);

        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(buttonPanel, gbc);

        mainPanel.add(panel, "quiz");
    }

    // ========================= 5. RESULT SCREEN ===========================
    void resultScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel icon = new JLabel("📊");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        gbc.gridy = 0;
        panel.add(icon, gbc);

        JLabel title = new JLabel("Quiz Finished!");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(new Color(50, 50, 50));
        gbc.gridy = 1;
        panel.add(title, gbc);

        scoreLabel = new JLabel("Final Score: 0");
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        scoreLabel.setForeground(new Color(0, 100, 200)); 
        gbc.gridy = 2;
        panel.add(scoreLabel, gbc);

        JButton exitBtn = new JButton("Close Application");
        exitBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        exitBtn.setBackground(new Color(220, 53, 69)); 
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.setPreferredSize(new Dimension(200, 50));
        
        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridy = 3;
        gbc.insets = new Insets(30, 0, 0, 0); 
        panel.add(exitBtn, gbc);

        mainPanel.add(panel, "result");
    }

    // ========================= HELPER BUTTON METHODS ===========================
    private JButton makeKeyButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(207, 226, 255));
        btn.addActionListener(e -> answerField.setText(answerField.getText() + text));
        return btn;
    }

    private JButton makeSpecialButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 200, 200)); 
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // ========================= LOGIC & HELPERS ===========================

    // Helper to switch to result screen
    void showResult() {
        scoreLabel.setText("Final Score: " + score + " / " + T_QUESTIONS);
        card.show(mainPanel, "result");
    }

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
            showResult();
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
        answerField.requestFocus(); // Focus so user can type immediately
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
                JOptionPane.showMessageDialog(frame, "Wrong! Attempts left : " + (3 - attempts) );
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

// ========================= MAIN CLASS ===========================
public class application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlayersGUI());
    }
}
