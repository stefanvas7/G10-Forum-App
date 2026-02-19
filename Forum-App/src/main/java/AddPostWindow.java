import javax.swing.*;


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

        JTextField questionField = new JTextField("Add question");
        generalPanel.add(questionField);

        JTextField descriptionField = new JTextField("Add description (optional)");
        generalPanel.add(descriptionField);

        JButton post = new JButton("Submit");
        post.addActionListener(e -> {
            man.callCreatePost(questionField.getText(), descriptionField.getText());
            addFrame.dispose();
        });
        generalPanel.add(post);


        addFrame.add(generalPanel);
        addFrame.setVisible(true);
    }
}