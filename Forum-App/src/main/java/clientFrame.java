import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

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
    private final Color primaryText = new Color(30, 34, 41);
    private final Color secondaryText = new Color(93, 101, 115);
    private final Color accent = new Color(46, 107, 255);
    private final Font titleFont = new Font("SansSerif", Font.BOLD, 26);
    private final Font subtitleFont = new Font("SansSerif", Font.PLAIN, 14);
    private final Font questionFont = new Font("SansSerif", Font.BOLD, 16);
    private final Font infoFont = new Font("SansSerif", Font.PLAIN, 13);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 14);

    public clientFrame() {

        JFrame frame = new JFrame("Forum");
        frame.setSize(1300, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
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

            int index = i;
            String id = temp.get("id").toString();
            postIds[i] = id;
            question.addActionListener(e -> new postFrame(postIds[index]));

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
    }

    private void styleSecondaryButton(JButton button) {
        button.setBackground(new Color(235, 239, 246));
        button.setForeground(new Color(52, 63, 79));
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(98, 36));
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
