import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

import java.awt.*;

import static javax.swing.BorderFactory.createEtchedBorder;

public class postFrame {
    SpringApi man = new SpringApi();
    public postFrame(String id) {

        JSONObject postJson = new JSONObject(man.callViewPost(id));

        JFrame questionFrame = new JFrame();
        questionFrame.setSize(1300,800);
        questionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        questionFrame.setResizable(false);
        questionFrame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;


        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> questionFrame.dispose());
        questionFrame.add(backButton);

        JPanel questionPanel = new JPanel();
        questionPanel.setBorder(createEtchedBorder());
        questionPanel.setLayout(new GridBagLayout());
        GridBagConstraints questionC = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.ipadx = 1000;
        c.weighty = 1;
        c.weightx = 1;
        questionPanel.setBackground(Color.red);
        //questionPanel.setSize(1000,400);
        //questionFrame.add(questionPanel,c);

        //code for the question itself
        questionC.gridx = 1;
        questionC.gridy = 0;
        questionC.ipadx = 200;
        questionC.weightx = 0.5;
        //questionC.weightx = 0;
        //questionC.gridwidth = 2;

        //ATTEMPT AT USING JTEXTAREA
        JTextArea questionTextArea = new JTextArea(postJson.get("question").toString(),100, 100);
        questionTextArea.setFont(new Font("verdana", Font.PLAIN, 20));
        questionTextArea.setEditable(false);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);

        //TEST
        JScrollPane questionTextScroll = new JScrollPane(questionTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        questionTextScroll.setSize(new Dimension(400,200));

        questionC.fill = GridBagConstraints.BOTH;
        questionC.weighty = 0.5;

        questionPanel.add(questionTextScroll, questionC);

        //code for any extra description to the question
        questionC.gridx = 1;
        questionC.gridy = 1;
        JTextArea descriptionTextArea = new JTextArea("[description");
        descriptionTextArea.setSize(800,200);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);

        JScrollPane descriptionTextScroll = new JScrollPane(descriptionTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        questionC.fill = GridBagConstraints.BOTH;
        questionC.weighty = 0.5;

        questionPanel.add(descriptionTextScroll, questionC);

        JSONArray answersArray = new JSONArray(postJson.get("answers").toString());

        int answersCount = answersArray.length();

        JLabel answerCountLabel = new JLabel("               " + answersCount + " answers");
        questionC.gridx = 0;
        questionC.gridy = 1;
        questionC.ipadx = 200;
        questionC.ipady = 100;
        questionC.weightx = 0;
        questionPanel.add(answerCountLabel, questionC);


        questionFrame.add(questionPanel,c);

        JScrollPane answerScrollPanel = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        answerScrollPanel.setSize(500,500);
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
        for(int i = 0; i < answersCount - 1; i++){
            answerPanel.add(formatAnswer(answersArray.getJSONObject(i).get("username").toString() + "     |     ", answersArray.getJSONObject(i).get("answer").toString()));
            answerPanel.add(Box.createRigidArea(new Dimension(0,50)));
        }
        answerScrollPanel.setViewportView(answerPanel);

        c.gridy = 2;
        c.weighty = 1;

        questionFrame.add(answerScrollPanel, c);

//      adding answers
        JScrollPane addAnswerScroll = new JScrollPane();
        JPanel addAnswerPanel = new JPanel();
        JTextField usernameInput = new JTextField("Username");
        JTextField answerInput = new JTextField("Answer                                     ");
        answerInput.setSize(300, 16);
        JButton finalAdd = new JButton("Add");
        finalAdd.addActionListener(e -> man.callAddAnswer(postJson.get("id").toString(), usernameInput.getText(), answerInput.getText()));
        addAnswerPanel.add(usernameInput);
        addAnswerPanel.add(answerInput);
        addAnswerPanel.add(finalAdd);

        addAnswerScroll.setViewportView(addAnswerPanel);

        c.gridy = 3;
        questionFrame.add(addAnswerPanel,c);




        questionFrame.setVisible(true);

    }

    public JPanel formatAnswer(String username, String answer){
        JPanel indAnswerPanel = new JPanel();
        JLabel usernameLabel = new JLabel(username);
        JLabel answerLabel = new JLabel(answer);
        indAnswerPanel.add(usernameLabel);
        indAnswerPanel.add(answerLabel);
        return indAnswerPanel;
    }
}
