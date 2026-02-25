import javax.swing.*;
import java.awt.*;

import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class AnswerItemPanel extends JPanel {

    public AnswerItemPanel(String username, String answer) {
        setLayout(new BorderLayout(0, 8));
        setBorder(createEtchedBorder());

        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setBorder(createEmptyBorder(8, 10, 0, 10));
        usernameLabel.setFont(new Font("verdana", Font.BOLD, 14));

        JTextArea answerArea = new JTextArea(answer);
        answerArea.setEditable(false);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setBorder(createEmptyBorder(0, 10, 10, 10));
        answerArea.setFont(new Font("verdana", Font.PLAIN, 14));
        answerArea.setBackground(getBackground());

        add(usernameLabel, BorderLayout.NORTH);
        add(answerArea, BorderLayout.CENTER);
    }
}