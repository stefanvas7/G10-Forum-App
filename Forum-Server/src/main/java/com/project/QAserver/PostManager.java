package com.project.QAserver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;
/*
Format for JSON:

{"id":"[ID],"question":"[question","answers":[{"username":"[username]","answer":"[answer]"}]}

 */

public class PostManager {

    public String listablePost(int page){

        File dir = new File("src/main/resources/postJSON/");
        File[] directoryListing = dir.listFiles((di, name) -> !name.equals(".DS_Store"));

        JSONArray jsonArray = new JSONArray();

        int firstPostIndex = (page - 1) * 5;
        assert directoryListing != null;
        int lastPostIndex;
        if (directoryListing.length < firstPostIndex + 5) {
            lastPostIndex = directoryListing.length;
        }else {
            lastPostIndex = firstPostIndex + 5;
        }
        BufferedReader reader;
        JSONObject temp;
        String line;
        for (int i = firstPostIndex; i < lastPostIndex; i++) {



            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(directoryListing[i]), StandardCharsets.UTF_8));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            try {
                line = reader.readLine();
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            temp = new JSONObject(line);
            jsonArray.put(temp);
        }




        return jsonArray.toString();
        }



    public String viewPost(String questionID) {
        File file = new File("src/main/resources/postJSON/" + questionID + ".json");
        Scanner reader;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String str = "";
        while (reader.hasNextLine()) {
            String temp = reader.nextLine();
            str = str + temp;
        }
        JSONObject tempJson = new JSONObject(str);
        return tempJson.toString();


    }
    public void addPost(String question, String description){
        JSONObject tempPostJson = new JSONObject();
        JSONObject[] nullAnswersJson = new JSONObject[0];
        String id;
        id = UUID.randomUUID().toString();
        tempPostJson.put("id", id);
        tempPostJson.put("question", question);
        tempPostJson.put("description",description);
        tempPostJson.put("answers",nullAnswersJson);


        File file = new File("src/main/resources/postJSON/" + id + ".json");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(tempPostJson.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void addAnswer(String questionID, String username, String answer){
        File file = new File("src/main/resources/postJSON/" + questionID + ".json");
        Scanner reader = null;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String str = "";
        while (reader.hasNextLine()){
            String temp = reader.nextLine();
            str = str + temp;
        }
        JSONObject tempJson = new JSONObject(str);
        JSONArray previousList = new JSONArray(tempJson.get("answers").toString());
        JSONObject newAnswerJson = new JSONObject();
        newAnswerJson.put("username", username);
        newAnswerJson.put("answer", answer);
        previousList.put(newAnswerJson);
        tempJson.put("answers", previousList);

        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(tempJson.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//  USE THE FOLLOWING CODE TO CREATE TEST JSON FILES WITH THE PROPER FORMAT
    public void setupTest(){
        JSONObject testObject = new JSONObject();
        JSONArray answers = new JSONArray();
        String id;
        FileWriter writer = null;
        for (int i = 0; i < 10; i++) {
            id = UUID.randomUUID().toString();
            File file = new File("src/main/resources/postJSON/" + id + ".json");

            testObject.put("id", id);
            testObject.put("question", "question number " + i);
            JSONObject tempAnswerObject = new JSONObject();
            for (int n = 0; n < 4; n++) {
                tempAnswerObject.put("username", n + "username");
                tempAnswerObject.put("answer", "answer number " + n);
            }
            testObject.put("answers", answers);

            try {
                writer = new FileWriter(file);
                writer.write(testObject.toString());
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}
