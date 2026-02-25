import javax.swing.*;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


public class AddPostWindow {
    SpringApi man = new SpringApi();
    public AddPostWindow() {
        JFrame addFrame = new JFrame();
        addFrame.setSize(1300,800);
        addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addFrame.setResizable(false);

        JPanel generalPanel = new JPanel();
        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));

        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> addFrame.dispose());
        generalPanel.add(backButton);

        String postPlaceholder = "Add question";
        String descriptionPlaceholder = "Add description (optional)";
        Color placeholderColor = Color.GRAY;
        Color inputColor = Color.BLACK;
        
        JTextField postInput = new JTextField(postPlaceholder);
        postInput.setForeground(placeholderColor);
        postInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (postInput.getText().equals(postPlaceholder)){
                    postInput.setText("");
                    postInput.setForeground(inputColor);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (postInput.getText().trim().isEmpty()) {
                    postInput.setText(postPlaceholder);
                    postInput.setForeground(placeholderColor);
                }
            }
        });
        generalPanel.add(postInput);
        

        JTextField descriptionInput = new JTextField(descriptionPlaceholder);
        descriptionInput.setForeground(placeholderColor);
        descriptionInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (descriptionInput.getText().equals(descriptionPlaceholder)){
                    descriptionInput.setText("");
                    descriptionInput.setForeground(inputColor);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (descriptionInput.getText().trim().isEmpty()) {
                    descriptionInput.setText(descriptionPlaceholder);
                    descriptionInput.setForeground(placeholderColor);
                }
            }
        });
        generalPanel.add(descriptionInput);

        JButton post = new JButton("Submit");
        post.addActionListener(e -> {
            man.callCreatePost(postInput.getText(), descriptionInput.getText());
            addFrame.dispose();
        });
        generalPanel.add(post);


        addFrame.add(generalPanel);
        addFrame.setVisible(true);
    }
}