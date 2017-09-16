package edu.rit.CSCI652.demo;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable {
	private int id;
	private Topic topic;
	private String title;
	private String content;

	public int getId() {
		return id;
	}

	public Topic getTopic() {
		return topic;
	}

	public String getTitle() {
		return title;
	}






	public Event(int id, Topic topic, String title, String content){
		this.id = id;
		this.topic = topic;
		this.title = title;
		this.content = content;
	}
}
