package com.project.QAserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
public class QAserverApplication {

	PostManager man = new PostManager();
	int page = 1;
	public static void main(String[] args) {
		SpringApplication.run(QAserverApplication.class, args);
	}
	@GetMapping("/")
	public String getListable(){

//		setup test cases
		man.setupTest();

		return man.listablePost(page);
	}
	int lastViewablePostPage = 1;
	@GetMapping("/next/{page}")
	public String next(@PathVariable(value="page")String page){
		String temp;
		try	{
			temp = man.listablePost(Integer.parseInt(page));
			lastViewablePostPage = lastViewablePostPage + 1;
		}catch (NullPointerException e){
			temp = man.listablePost(lastViewablePostPage);
			throw new RuntimeException(e);
		}
		return temp;
	}
	@GetMapping("/previous/{page}")
	public String previous(@PathVariable(value="page")String page){
		String temp;

		try {
			temp = man.listablePost(Integer.parseInt(page));
		}catch (NullPointerException e){
			temp = man.listablePost(lastViewablePostPage);
			throw new RuntimeException(e);
		}
		return temp;
	}

	@GetMapping("/post/{id}")
	public String viewPost(@PathVariable(value="id") String postID){
		return man.viewPost(postID);
	}
	@GetMapping("/post/{postid}/{answer}/{username}")
	public void addAnswer(@PathVariable(value="postid") String postID, @PathVariable(value = "answer") String answer, @PathVariable(value = "username") String username){
		man.addAnswer(postID,username,answer);
	}
	@GetMapping("/post/create/{question}/{description}")
	public void addPost(@PathVariable(value="question") String question, @PathVariable(value = "description") String description){
		man.addPost(question,description);
	}


}
