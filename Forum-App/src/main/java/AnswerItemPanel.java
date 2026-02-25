import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.BorderFactory.createEtchedBorder;
import static javax.swing.BorderFactory.createEmptyBorder;

public class AnswerItemPanel extends JPanel {

    public AnswerItemPanel(String username, String answer) {
        Color baseBackground = Color.WHITE;
        Color hoverBackground = new Color(248, 251, 255);

        setLayout(new BorderLayout(0, 8));
        setBorder(createEtchedBorder());
        setBackground(baseBackground);

        JLabel usernameLabel = new JLabel(username);
        usernameLabel.setBorder(createEmptyBorder(8, 10, 0, 10));
        usernameLabel.setFont(new Font("verdana", Font.BOLD, 14));
        usernameLabel.setOpaque(false);

        JTextArea answerArea = new JTextArea(answer);
        answerArea.setEditable(false);
        answerArea.setLineWrap(true);
        answerArea.setWrapStyleWord(true);
        answerArea.setBorder(createEmptyBorder(0, 10, 10, 10));
        answerArea.setFont(new Font("verdana", Font.PLAIN, 14));
        answerArea.setBackground(baseBackground);

        add(usernameLabel, BorderLayout.NORTH);
        add(answerArea, BorderLayout.CENTER);

        MouseAdapter hoverHandler = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackground);
                answerArea.setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Point pointer = MouseInfo.getPointerInfo() != null ? MouseInfo.getPointerInfo().getLocation() : null;
                if (pointer == null) {
                    setBackground(baseBackground);
                    answerArea.setBackground(baseBackground);
                    return;
                }

                Point location = getLocationOnScreen();
                Rectangle bounds = new Rectangle(location, getSize());
                if (!bounds.contains(pointer)) {
                    setBackground(baseBackground);
                    answerArea.setBackground(baseBackground);
                }
            }
        };

        addMouseListener(hoverHandler);
        usernameLabel.addMouseListener(hoverHandler);
        answerArea.addMouseListener(hoverHandler);
    }
}