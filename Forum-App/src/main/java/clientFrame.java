import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.BorderFactory.*;

public class clientFrame {
    private final SpringApi control = new SpringApi();
    private final JPanel[] individualPostPanel = new JPanel[5];
    private final JButton[] questionButtons = new JButton[5];
    private final JLabel[] answerLabels = new JLabel[5];
    private final String[] postIds = new String[5];
    private int page = 1;

    private final Color pageBackground = new Color(243, 246, 250);
    private final Color cardBackground = Color.WHITE;
    private final Color cardBorder = new Color(223, 229, 238);
    private final Color cardBorderHover = new Color(184, 198, 220);
    private final Color primaryText = new Color(30, 34, 41);
    private final Color secondaryText = new Color(93, 101, 115);
    private final Color accent = new Color(46, 107, 255);
    private final Color accentHover = new Color(36, 96, 241);
    private final Color accentPressed = new Color(28, 84, 222);
    private final Color secondaryButton = new Color(235, 239, 246);
    private final Color secondaryButtonHover = new Color(223, 230, 242);
    private final Color secondaryButtonPressed = new Color(209, 218, 234);
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 26);
    private final Font subtitleFont = new Font("SansSerif", Font.PLAIN, 14);
    private final Font questionFont = new Font("SansSerif", Font.BOLD, 16);
    private final Font infoFont = new Font("SansSerif", Font.PLAIN, 13);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

    public clientFrame() {

        JFrame frame = new JFrame("Forum");
        frame.setSize(1300, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setMinimumSize(new Dimension(980, 640));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(pageBackground);
        frame.setLayout(new BorderLayout(0, 0));

        JPanel rootPanel = new JPanel(new BorderLayout(0, 0));
        rootPanel.setBackground(pageBackground);
        rootPanel.setBorder(createEmptyBorder(22, 28, 24, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(createEmptyBorder(0, 0, 16, 0));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Community Forum");
        title.setFont(titleFont);
        title.setForeground(primaryText);

        JLabel subtitle = new JLabel("Browse questions, open discussions, or create a new post.");
        subtitle.setFont(subtitleFont);
        subtitle.setForeground(secondaryText);

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 6)));
        titlePanel.add(subtitle);

        JButton createPostButton = new JButton("+ New Post");
        stylePrimaryButton(createPostButton);
        createPostButton.addActionListener(e -> {
            new AddPostWindow();
        });
        JPanel createButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        createButtonPanel.setOpaque(false);
        createButtonPanel.add(createPostButton);

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(createButtonPanel, BorderLayout.EAST);
        rootPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel postPanel = new JPanel();
        postPanel.setOpaque(false);
        postPanel.setBorder(createEmptyBorder(4, 0, 6, 0));
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        rootPanel.add(postPanel, BorderLayout.CENTER);

        JSONArray jsonArray;
        try {
            String stringified = control.getViewable();
            jsonArray = new JSONArray(stringified);
        } catch (RuntimeException e) {
            showServerError(frame);
            throw new RuntimeException(e);
        }

        for (int i = 0; i < 5; i++) {
            JSONObject temp = jsonArray.getJSONObject(i);

            JPanel card = new JPanel(new BorderLayout(16, 0));
            card.setBackground(cardBackground);
            card.setBorder(createCompoundBorder(
                createLineBorder(cardBorder),
                createEmptyBorder(14, 16, 14, 16)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 102));

            JPanel leftMetaPanel = new JPanel();
            leftMetaPanel.setLayout(new BoxLayout(leftMetaPanel, BoxLayout.Y_AXIS));
            leftMetaPanel.setOpaque(false);
            leftMetaPanel.setPreferredSize(new Dimension(110, 68));

            JSONArray answersArray = new JSONArray(temp.get("answers").toString());
            JLabel answers = new JLabel(answersArray.length() + " answers", SwingConstants.CENTER);
            answers.setForeground(secondaryText);
            answers.setFont(infoFont);
            answers.setAlignmentX(Component.CENTER_ALIGNMENT);
            answers.setBorder(createCompoundBorder(createLineBorder(new Color(226, 232, 242)), createEmptyBorder(8, 8, 8, 8)));
            answers.setBackground(new Color(248, 250, 254));
            answers.setOpaque(true);
            leftMetaPanel.add(answers);

            JButton question = new JButton(temp.get("question").toString());
            question.setHorizontalAlignment(SwingConstants.LEFT);
            question.setFocusPainted(false);
            question.setBorderPainted(false);
            question.setOpaque(false);
            question.setContentAreaFilled(false);
            question.setForeground(primaryText);
            question.setFont(questionFont);
            question.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            int index = i;
            String id = temp.get("id").toString();
            postIds[i] = id;
            question.addActionListener(e -> new postFrame(postIds[index]));

            addPostCardHover(card, question, leftMetaPanel, answers);

            individualPostPanel[i] = card;
            answerLabels[i] = answers;
            questionButtons[i] = question;

            card.add(leftMetaPanel, BorderLayout.WEST);
            card.add(question, BorderLayout.CENTER);
            postPanel.add(card);
            postPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(createEmptyBorder(8, 0, 0, 0));

        JLabel pageLabel = new JLabel("Page: " + page);
        pageLabel.setForeground(secondaryText);
        pageLabel.setFont(infoFont);

        JPanel arrowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        arrowPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        styleSecondaryButton(backButton);
        backButton.addActionListener(e -> {
            if (page > 1) {
                page = page - 1;
                pageLabel.setText("Page: " + page);
                reloadContentPrevious(page);
            }
        });

        JButton forwardButton = new JButton("Next");
        styleSecondaryButton(forwardButton);
        forwardButton.addActionListener(e -> {
            page = page + 1;
            pageLabel.setText("Page: " + page);
            reloadContentNext(page);
        });

        arrowPanel.add(backButton);
        arrowPanel.add(forwardButton);

        bottomPanel.add(pageLabel, BorderLayout.WEST);
        bottomPanel.add(arrowPanel, BorderLayout.EAST);
        rootPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(rootPanel);
        frame.setVisible(true);
    }

    public void reloadContentNext(int page) {
        JSONArray tempArray = new JSONArray(control.callNextPage(page));
        refreshPosts(tempArray);
    }

    public void reloadContentPrevious(int page) {
        JSONArray tempArray = new JSONArray(control.callPreviousPage(page));
        refreshPosts(tempArray);
    }

    private void refreshPosts(JSONArray tempArray) {
        for (int i = 0; i < 5; i++) {
            JSONObject tempObject = tempArray.getJSONObject(i);
            questionButtons[i].setText(tempObject.get("question").toString());
            JSONArray tempAnswerArray = new JSONArray(tempObject.get("answers").toString());
            answerLabels[i].setText(tempAnswerArray.length() + " answers");
            postIds[i] = tempObject.get("id").toString();
        }
    }

    private void stylePrimaryButton(JButton button) {
        button.setBackground(accent);
        button.setForeground(Color.WHITE);
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(132, 40));
        addButtonInteraction(button, accent, accentHover, accentPressed);
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(secondaryButton);
        button.setForeground(new Color(52, 63, 79));
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(98, 36));
        addButtonInteraction(button, secondaryButton, secondaryButtonHover, secondaryButtonPressed);
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

    private void addPostCardHover(JPanel card, JButton question, JComponent... otherComponents) {
        MouseAdapter hoverHandler = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCardHoverState(card, question, true, false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setCardHoverState(card, question, true, true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point pointer = MouseInfo.getPointerInfo() != null ? MouseInfo.getPointerInfo().getLocation() : null;
                if (pointer == null) {
                    setCardHoverState(card, question, false, false);
                    return;
                }

                Point cardLocation = card.getLocationOnScreen();
                Rectangle cardBounds = new Rectangle(cardLocation, card.getSize());
                setCardHoverState(card, question, cardBounds.contains(pointer), false);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Point pointer = MouseInfo.getPointerInfo() != null ? MouseInfo.getPointerInfo().getLocation() : null;
                if (pointer == null) {
                    setCardHoverState(card, question, false, false);
                    return;
                }

                Point cardLocation = card.getLocationOnScreen();
                Rectangle cardBounds = new Rectangle(cardLocation, card.getSize());
                if (!cardBounds.contains(pointer)) {
                    setCardHoverState(card, question, false, false);
                }
            }
        };

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(hoverHandler);
        question.addMouseListener(hoverHandler);

        for (JComponent component : otherComponents) {
            component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            component.addMouseListener(hoverHandler);
        }
    }

    private void setCardHoverState(JPanel card, JButton question, boolean hovered, boolean pressed) {
        Color borderColor = hovered ? cardBorderHover : cardBorder;
        Color background;

        if (pressed) {
            background = new Color(236, 244, 255);
        } else if (hovered) {
            background = new Color(248, 251, 255);
        } else {
            background = cardBackground;
        }

        card.setBackground(background);
        card.setBorder(createCompoundBorder(
            createLineBorder(borderColor),
            createEmptyBorder(14, 16, 14, 16)
        ));
        question.setForeground(hovered || pressed ? accent : primaryText);
    }

    private void showServerError(JFrame parent) {
        JOptionPane.showMessageDialog(
            parent,
            "The forum server is not available right now. Please start the server and try again.",
            "Server unavailable",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
