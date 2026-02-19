import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BorderFactory.*;

public class clientFrame {
    SpringApi control = new SpringApi();
     JPanel[] individualPostPanel;
    int page = 1;

    public clientFrame() {

        //frame
        JFrame frame = new JFrame();
        frame.setSize(1300,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

//Create posts
        JPanel generalPanel = new JPanel();
        generalPanel.setSize(40,40);
        generalPanel.setBorder(createEmptyBorder(30,30,10,10));
        JButton button = new JButton("Post");
        button.addActionListener(e ->{
            AddPostWindow createPost = new AddPostWindow();
        });
        button.setSize(30,30);

        generalPanel.add(button);
        frame.add(generalPanel, BorderLayout.NORTH);

//Question posts Section
        JPanel postPanel = new JPanel();
        frame.add(postPanel, BorderLayout.CENTER);
        postPanel.setSize(1300,654);
        postPanel.setBorder(createEtchedBorder());
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));

        //Individual posts
        individualPostPanel = new JPanel[5];
        JSONArray jsonArray;
//      informs the user if the server side of the application isn't up
        try {
            String stringified = control.getViewable();
            System.out.println(stringified);
            jsonArray = new JSONArray(stringified);
        }catch (RuntimeException e){
            JFrame exceptionFrame = new JFrame();
            exceptionFrame.setSize(500,300);
            exceptionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JLabel label = new JLabel("Server is not up");
            exceptionFrame.add(label);
            exceptionFrame.pack();
            exceptionFrame.setSize(500,300);
            exceptionFrame.setVisible(true);

            throw new RuntimeException(e);

        }
        JSONObject temp;
        for (int i = 0; i<5; i++){

            temp = jsonArray.getJSONObject(i);

            individualPostPanel[i] = new JPanel();
            individualPostPanel[i].setSize(1000,120);
            individualPostPanel[i].setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.VERTICAL;



            JSONArray answersArray = new JSONArray(temp.get("answers").toString());
            JLabel answers = new JLabel(answersArray.length() + " answers");
            c.gridx = 0;
            c.gridy = 1;
            individualPostPanel[i].add(answers, c);


            String questionText = temp.get("question").toString();
            JButton question = new JButton(questionText);


            c.fill = GridBagConstraints.VERTICAL;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipadx = 1000;
            c.ipady = 90;
            c.gridheight = 2;
            c.gridx = 1;
            c.gridy = 0;
            question.setHorizontalTextPosition(JLabel.LEFT);
            individualPostPanel[i].add(question, c);

            String id = temp.get("id").toString();
            question.addActionListener(e -> {

                postFrame viewingPosts = new postFrame(id);


            });
            postPanel.add(individualPostPanel[i]);
        }
        //Viewing more posts
        JPanel arrowPanel = new JPanel();
        JButton backButton = new JButton();
        backButton.setText("Back");

        backButton.addActionListener(e -> {
            if (page <= 1) {
                page = page;
            }else {
                page = page - 1;
            }
            reloadContentPrevious(page);
        });
        JButton forwardButton = new JButton();
        forwardButton.setText("Next");
        forwardButton.addActionListener(e -> {
            page = page + 1;
            reloadContentNext(page);

        });
        arrowPanel.add(backButton);
        arrowPanel.add(forwardButton);

        frame.add(arrowPanel, BorderLayout.SOUTH);


        frame.setVisible(true);




    }
    public void reloadContentNext(int page){

        JButton tempButton;
        JLabel tempLabel;
        JSONObject tempObject;
        JSONArray tempArray = new JSONArray(control.callNextPage(page));

        for (int i = 0; i < 5; i++){
            tempObject = tempArray.getJSONObject(i);
            tempButton = (JButton) individualPostPanel[i].getComponent(1);
            tempButton.setText(tempObject.get("question").toString());

            tempLabel = (JLabel) individualPostPanel[i].getComponent(0);
            JSONArray tempAnswerArray = new JSONArray(tempObject.get("answers").toString());
            tempLabel.setText(tempAnswerArray.length() + " answers");
        }
    }
    public void reloadContentPrevious(int page){
        JButton tempButton;
        JLabel tempLabel;
        JSONObject tempObject;
        JSONArray tempArray = new JSONArray(control.callPreviousPage(page));
        for (int i = 0; i < 5; i++){
            tempObject = tempArray.getJSONObject(i);
            tempButton = (JButton) individualPostPanel[i].getComponent(1);
            tempButton.setText(tempObject.get("question").toString());

            tempLabel = (JLabel) individualPostPanel[i].getComponent(0);
            JSONArray tempAnswerArray = new JSONArray(tempObject.get("answers").toString());
            tempLabel.setText(tempAnswerArray.length() + " answers");
        }
    }


}
