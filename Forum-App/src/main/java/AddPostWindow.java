import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.border.EmptyBorder;


public class AddPostWindow {
    private final SpringApi man = new SpringApi();

    public AddPostWindow() {
        JFrame addFrame = new JFrame("Create Post");
        addFrame.setSize(920, 620);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setResizable(false);
        addFrame.setLocationRelativeTo(null);

        Color pageBackground = new Color(243, 246, 250);
        Color cardBackground = Color.WHITE;
        Color primaryText = new Color(28, 31, 37);
        Color secondaryText = new Color(98, 107, 122);
        Color placeholderColor = new Color(144, 153, 168);
        Color inputTextColor = new Color(35, 39, 47);
        Color borderColor = new Color(220, 226, 235);
        Color actionButtonColor = new Color(46, 107, 255);
        Color actionButtonText = Color.WHITE;

        String postPlaceholder = "Write your question title...";
        String descriptionPlaceholder = "Add more context to help others answer (optional)";

        JPanel pagePanel = new JPanel(new BorderLayout());
        pagePanel.setBackground(pageBackground);
        pagePanel.setBorder(new EmptyBorder(36, 48, 36, 48));

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(cardBackground);
        cardPanel.setBorder(new EmptyBorder(28, 32, 28, 32));

        JLabel headerTitle = new JLabel("Create a new post");
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerTitle.setForeground(primaryText);
        headerTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel headerSubtitle = new JLabel("Ask clearly and include useful details.");
        headerSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        headerSubtitle.setForeground(secondaryText);
        headerSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("Question");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(primaryText);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField postInput = new JTextField(postPlaceholder);
        postInput.setForeground(placeholderColor);
        postInput.setFont(new Font("SansSerif", Font.PLAIN, 15));
        postInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        postInput.setPreferredSize(new Dimension(700, 44));
        postInput.setBorder(new EmptyBorder(10, 12, 10, 12));
        postInput.setBackground(Color.WHITE);
        postInput.setAlignmentX(Component.LEFT_ALIGNMENT);
        addPlaceholderBehavior(postInput, postPlaceholder, placeholderColor, inputTextColor);

        JPanel postInputWrapper = new JPanel(new BorderLayout());
        postInputWrapper.setBorder(BorderFactory.createLineBorder(borderColor));
        postInputWrapper.setBackground(Color.WHITE);
        postInputWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        postInputWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        postInputWrapper.add(postInput, BorderLayout.CENTER);

        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        descriptionLabel.setForeground(primaryText);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descriptionInput = new JTextArea(descriptionPlaceholder);
        descriptionInput.setForeground(placeholderColor);
        descriptionInput.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descriptionInput.setLineWrap(true);
        descriptionInput.setWrapStyleWord(true);
        descriptionInput.setBorder(new EmptyBorder(12, 12, 12, 12));
        descriptionInput.setBackground(Color.WHITE);
        addPlaceholderBehavior(descriptionInput, descriptionPlaceholder, placeholderColor, inputTextColor);

        JScrollPane descriptionScroll = new JScrollPane(descriptionInput);
        descriptionScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        descriptionScroll.setPreferredSize(new Dimension(700, 220));
        descriptionScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        descriptionScroll.setBorder(BorderFactory.createLineBorder(borderColor));
        descriptionScroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actions.setBackground(cardBackground);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backButton = new JButton("Back");
        styleSecondaryButton(backButton);
        backButton.addActionListener(e -> addFrame.dispose());

        JButton submitButton = new JButton("Submit Post");
        stylePrimaryButton(submitButton, actionButtonColor, actionButtonText);
        submitButton.addActionListener(e -> {
            String question = cleanInput(postInput.getText(), postPlaceholder);
            String description = cleanInput(descriptionInput.getText(), descriptionPlaceholder);

            if (question.isEmpty()) {
                JOptionPane.showMessageDialog(
                    addFrame,
                    "Please enter a question title before submitting.",
                    "Missing question",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            man.callCreatePost(question, description);
            addFrame.dispose();
        });

        actions.add(backButton);
        actions.add(submitButton);

        cardPanel.add(headerTitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        cardPanel.add(headerSubtitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(postInputWrapper);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(descriptionLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        cardPanel.add(descriptionScroll);
        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        cardPanel.add(actions);

        pagePanel.add(cardPanel, BorderLayout.CENTER);

        addFrame.setContentPane(pagePanel);
        addFrame.setVisible(true);
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

    private void stylePrimaryButton(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 40));
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(235, 239, 246));
        button.setForeground(new Color(55, 66, 84));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(100, 40));
    }

    private String cleanInput(String value, String placeholder) {
        String text = value == null ? "" : value.trim();
        if (text.equals(placeholder)) {
            return "";
        }
        return text;
    }
}