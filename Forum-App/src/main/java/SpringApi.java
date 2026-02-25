import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.util.Objects;


public class SpringApi {

    String apiUrl = "http://localhost:8080";
    RestTemplate restTemplate = new RestTemplate();
    public String getViewable() {
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl + "/", String.class);

        JSONArray jsonArray = new JSONArray(Objects.requireNonNull(response.getBody()));
        return jsonArray.toString();
    }

    public String callViewPost(String id){
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl + "/post/" + id, String.class);

        JSONObject postJson = new JSONObject(Objects.requireNonNull(response.getBody()));

        return postJson.toString();
    }

    public void callAddAnswer(String questionID, String username, String answer){
        restTemplate.getForEntity(apiUrl + "/post/" + questionID + "/" + answer + "/"+ username, String.class);
    }
    public String callNextPage(int pageNumber){
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl + "/next/" + pageNumber, String.class);
        JSONArray jsonArray = new JSONArray(Objects.requireNonNull(response.getBody()));
        return jsonArray.toString();
    }
    public String callPreviousPage(int pageNumber){
        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl + "/previous/" + pageNumber, String.class);
        JSONArray jsonArray = new JSONArray(Objects.requireNonNull(response.getBody()));
        return jsonArray.toString();
    }

    public void callCreatePost(String question, String description){
        restTemplate.getForEntity(apiUrl + "/post/create/" + question + "/" + description, String.class);
    }

}
