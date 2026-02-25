import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;

public class postFrame {
    private final SpringApi man = new SpringApi();

    public postFrame(String id) {
        JSONObject postJson = new JSONObject(man.callViewPost(id));

        JFrame questionFrame = new JFrame("Post Details");
        questionFrame.setSize(1300, 800);
        questionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        questionFrame.setResizable(true);
        questionFrame.setMinimumSize(new Dimension(980, 640));
        questionFrame.setLocationRelativeTo(null);

        Color pageBackground = new Color(243, 246, 250);
        Color cardBackground = Color.WHITE;
        Color borderColor = new Color(223, 229, 238);
        Color primaryText = new Color(30, 34, 41);
        Color secondaryText = new Color(92, 102, 116);
        Color placeholderColor = new Color(144, 153, 168);
        Color inputText = new Color(35, 39, 47);
        Color accent = new Color(46, 107, 255);

        String usernamePlaceholder = "Username";
        String answerPlaceholder = "Write your answer...";

        JPanel rootPanel = new JPanel(new BorderLayout(0, 16));
        rootPanel.setBackground(pageBackground);
        rootPanel.setBorder(new EmptyBorder(22, 28, 22, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        styleSecondaryButton(backButton);
        backButton.addActionListener(e -> questionFrame.dispose());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Post & Answers");
        title.setForeground(primaryText);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));

        JLabel subtitle = new JLabel("Read the question details and join the discussion.");
        subtitle.setForeground(secondaryText);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 6)));
        titlePanel.add(subtitle);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 14));
        centerPanel.setOpaque(false);

        JPanel questionCard = new JPanel();
        questionCard.setLayout(new BoxLayout(questionCard, BoxLayout.Y_AXIS));
        questionCard.setBackground(cardBackground);
        questionCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor),
            new EmptyBorder(16, 18, 16, 18)
        ));

        JLabel questionLabel = new JLabel("Question");
        questionLabel.setForeground(primaryText);
        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea questionTextArea = new JTextArea(postJson.optString("question", ""));
        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setFont(new Font("SansSerif", Font.PLAIN, 17));
        questionTextArea.setForeground(primaryText);
        questionTextArea.setBackground(cardBackground);
        questionTextArea.setBorder(new EmptyBorder(6, 0, 6, 0));

        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setForeground(primaryText);
        descriptionLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String descriptionText = postJson.optString("description", "").trim();
        if (descriptionText.isEmpty()) {
            descriptionText = "No description provided.";
        }
        JTextArea descriptionTextArea = new JTextArea(descriptionText);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descriptionTextArea.setForeground(secondaryText);
        descriptionTextArea.setBackground(cardBackground);
        descriptionTextArea.setBorder(new EmptyBorder(6, 0, 0, 0));

        questionCard.add(questionLabel);
        questionCard.add(questionTextArea);
        questionCard.add(Box.createRigidArea(new Dimension(0, 10)));
        questionCard.add(descriptionLabel);
        questionCard.add(descriptionTextArea);

        centerPanel.add(questionCard, BorderLayout.NORTH);

        JSONArray answersArray = new JSONArray(postJson.get("answers").toString());
        int[] answersCount = {answersArray.length()};

        JPanel answerSection = new JPanel(new BorderLayout(0, 10));
        answerSection.setOpaque(false);

        JLabel answerCountLabel = new JLabel(answersCount[0] + " answers");
        answerCountLabel.setForeground(primaryText);
        answerCountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        answerSection.add(answerCountLabel, BorderLayout.NORTH);

        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        answerPanel.setBackground(cardBackground);
        answerPanel.setBorder(new EmptyBorder(12, 12, 12, 12));

        for (int i = 0; i < answersArray.length(); i++) {
            String answerText = answersArray.getJSONObject(i).optString("answer", "").trim();
            if (!answerText.isEmpty()) {
                answerPanel.add(new AnswerItemPanel(
                    answersArray.getJSONObject(i).optString("username", "Unknown user"),
                    answerText
                ));
                answerPanel.add(Box.createRigidArea(new Dimension(0, 14)));
            }
        }

        JScrollPane answerScrollPanel = new JScrollPane(answerPanel);
        answerScrollPanel.setBorder(BorderFactory.createLineBorder(borderColor));
        answerScrollPanel.getVerticalScrollBar().setUnitIncrement(12);
        answerSection.add(answerScrollPanel, BorderLayout.CENTER);
        centerPanel.add(answerSection, BorderLayout.CENTER);

        rootPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel addAnswerPanel = new JPanel();
        addAnswerPanel.setLayout(new BoxLayout(addAnswerPanel, BoxLayout.Y_AXIS));
        addAnswerPanel.setBackground(cardBackground);
        addAnswerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor),
            new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel addAnswerLabel = new JLabel("Add your answer");
        addAnswerLabel.setForeground(primaryText);
        addAnswerLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        addAnswerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel rowPanel = new JPanel(new BorderLayout(12, 0));
        rowPanel.setOpaque(false);
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField usernameInput = new JTextField(usernamePlaceholder);
        usernameInput.setForeground(placeholderColor);
        usernameInput.setFont(new Font("SansSerif", Font.PLAIN, 14));
        usernameInput.setBorder(new EmptyBorder(10, 12, 10, 12));
        addPlaceholderBehavior(usernameInput, usernamePlaceholder, placeholderColor, inputText);

        JPanel usernameWrapper = new JPanel(new BorderLayout());
        usernameWrapper.setBackground(Color.WHITE);
        usernameWrapper.setBorder(BorderFactory.createLineBorder(borderColor));
        usernameWrapper.setPreferredSize(new Dimension(220, 42));
        usernameWrapper.add(usernameInput, BorderLayout.CENTER);

        JTextArea answerInput = new JTextArea(answerPlaceholder);
        answerInput.setForeground(placeholderColor);
        answerInput.setFont(new Font("SansSerif", Font.PLAIN, 14));
        answerInput.setLineWrap(true);
        answerInput.setWrapStyleWord(true);
        answerInput.setBorder(new EmptyBorder(10, 12, 10, 12));
        addPlaceholderBehavior(answerInput, answerPlaceholder, placeholderColor, inputText);

        JScrollPane answerInputScroll = new JScrollPane(answerInput);
        answerInputScroll.setBorder(BorderFactory.createLineBorder(borderColor));
        answerInputScroll.setPreferredSize(new Dimension(760, 82));

        rowPanel.add(usernameWrapper, BorderLayout.WEST);
        rowPanel.add(answerInputScroll, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton finalAdd = new JButton("Add Answer");
        stylePrimaryButton(finalAdd, accent);
        finalAdd.addActionListener(e -> {
            String username = cleanInput(usernameInput.getText(), usernamePlaceholder);
            String answer = cleanInput(answerInput.getText(), answerPlaceholder);

            if (answer.isEmpty()) {
                JOptionPane.showMessageDialog(
                    questionFrame,
                    "Please enter an answer before submitting.",
                    "Missing answer",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            man.callAddAnswer(postJson.get("id").toString(), username, answer);
            answerPanel.add(new AnswerItemPanel(username.isEmpty() ? "Unknown user" : username, answer));
            answerPanel.add(Box.createRigidArea(new Dimension(0, 14)));
            answerPanel.revalidate();
            answerPanel.repaint();

            answersCount[0]++;
            answerCountLabel.setText(answersCount[0] + " answers");

            usernameInput.setText(usernamePlaceholder);
            usernameInput.setForeground(placeholderColor);
            answerInput.setText(answerPlaceholder);
            answerInput.setForeground(placeholderColor);
        });
        buttonRow.add(finalAdd);

        addAnswerPanel.add(addAnswerLabel);
        addAnswerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addAnswerPanel.add(rowPanel);
        addAnswerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addAnswerPanel.add(buttonRow);

        rootPanel.add(addAnswerPanel, BorderLayout.SOUTH);

        questionFrame.setContentPane(rootPanel);
        questionFrame.setVisible(true);
    }

    private void addPlaceholderBehavior(JTextField field, String placeholder, Color placeholderColor, Color inputColor) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(inputColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(placeholderColor);
                }
            }
        });
    }

    private void addPlaceholderBehavior(JTextArea field, String placeholder, Color placeholderColor, Color inputColor) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(inputColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(placeholderColor);
                }
            }
        });
    }

    private void stylePrimaryButton(JButton button, Color background) {
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 38));
        addButtonInteraction(button, background, new Color(36, 96, 241), new Color(28, 84, 222));
    }

    private void styleSecondaryButton(JButton button) {
        Color defaultColor = new Color(235, 239, 246);
        button.setBackground(defaultColor);
        button.setForeground(new Color(52, 63, 79));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(92, 36));
        addButtonInteraction(button, defaultColor, new Color(223, 230, 242), new Color(209, 218, 234));
    }

    private void addButtonInteraction(JButton button, Color defaultColor, Color hoverColor, Color pressedColor) {
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    button.setBackground(pressedColor);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (button.contains(e.getPoint())) {
                    button.setBackground(hoverColor);
                } else {
                    button.setBackground(defaultColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultColor);
            }
        });
    }

    private String cleanInput(String value, String placeholder) {
        String text = value == null ? "" : value.trim();
        if (text.equals(placeholder)) {
            return "";
        }
        return text;
    }
}
