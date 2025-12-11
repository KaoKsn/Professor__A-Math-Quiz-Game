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

    // ========================== WELCOME SCREEN (EDUCATIONAL VIBE) ============================

void welcomeScreen() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(Color.WHITE); // Clean, textbook-style background

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 0, 5, 0); // Tighter spacing
    gbc.gridx = 0; // Center everything vertically

    // 1. Icon / Mascot (Large Graduation Cap)
    JLabel icon = new JLabel("🎓"); 
    icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
    gbc.gridy = 0;
    panel.add(icon, gbc);

    // 2. Main Title (Serif font looks more academic)
    JLabel title = new JLabel("Professor Math");
    title.setFont(new Font("Serif", Font.BOLD, 36)); 
    title.setForeground(new Color(44, 62, 80)); // Dark academic grey/blue
    gbc.gridy = 1;
    panel.add(title, gbc);

    // 3. Decorative Math Symbols
    JLabel mathSymbols = new JLabel("∑   π   √   x²   ÷");
    mathSymbols.setFont(new Font("Monospaced", Font.BOLD, 22));
    mathSymbols.setForeground(new Color(100, 149, 237)); // Soft math blue
    gbc.gridy = 2;
    gbc.insets = new Insets(10, 0, 20, 0); // Add space below symbols
    panel.add(mathSymbols, gbc);

    // 4. Motivating Subtitle
    JLabel subtitle = new JLabel("Sharpen your mind!");
    subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 16));
    subtitle.setForeground(Color.GRAY);
    gbc.gridy = 3;
    gbc.insets = new Insets(0, 0, 15, 0); 
    panel.add(subtitle, gbc);

    // 5. "Start Learning" Button
    JButton playBtn = new JButton("START QUIZ");
    playBtn.setPreferredSize(new Dimension(200, 55));
    playBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
    playBtn.setBackground(new Color(46, 204, 113)); // Emerald Green (Motivating)
    playBtn.setForeground(Color.WHITE);
    playBtn.setFocusPainted(false);
    playBtn.setBorder(BorderFactory.createRaisedBevelBorder()); // 3D pop effect
    playBtn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand pointer on hover

    playBtn.addActionListener(e -> {
        card.show(mainPanel, "operator");
    });

    gbc.gridy = 4;
    panel.add(playBtn, gbc);

    mainPanel.add(panel, "welcome");
}

// ========================= OPERATOR SELECTION (SIMPLE & CLEAN) ===========================
void operatorScreen() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(Color.WHITE); // Clean white background
    GridBagConstraints gbc = new GridBagConstraints();

    // 1. Simple Title
    JLabel label = new JLabel("Select Operation");
    label.setFont(new Font("Segoe UI", Font.BOLD, 24));
    label.setForeground(new Color(50, 50, 50)); // Dark Gray
    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

    // 2. Button Container
    JPanel container = new JPanel(new GridLayout(2, 2, 15, 15)); // 15px gap
    container.setPreferredSize(new Dimension(280, 200));
    container.setOpaque(false);

    String[] ops = {"+", "-", "*", "%"};
    
    for (String op : ops) {
        JButton btn = new JButton(op);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 32));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(230, 240, 255)); // Very light blue
        btn.setForeground(new Color(0, 100, 200));   // Darker blue text
        btn.setBorder(BorderFactory.createEmptyBorder()); // Flat look

        // Simple Hover Effect
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



// ========================= LEVEL SELECTION (SIMPLE & CLEAN) ===========================
void levelScreen() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(Color.WHITE); 
    GridBagConstraints gbc = new GridBagConstraints();

    // 1. Simple Title
    JLabel label = new JLabel("Select Difficulty");
    label.setFont(new Font("Segoe UI", Font.BOLD, 24));
    label.setForeground(new Color(50, 50, 50));
    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

    // 2. Button Container
    JPanel container = new JPanel(new GridLayout(1, 3, 20, 0)); // 1 row, 3 cols, 20px gap
    container.setPreferredSize(new Dimension(380, 80));
    container.setOpaque(false);

    for (int i = 1; i <= 3; i++) {
        int levelNum = i;
        JButton btn = new JButton(String.valueOf(i));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 28));
        btn.setFocusPainted(false);
        
        // distinct clean colors
        btn.setBackground(new Color(245, 245, 245)); // Light Gray
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(220, 220, 220)); // Darker gray on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(245, 245, 245));
            }
        });

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


// ========================= QUIZ SCREEN ===========================

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
