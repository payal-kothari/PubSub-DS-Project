package edu.rit.CSCI652.demo;

import java.io.Serializable;
import java.util.List;

public class Event implements Serializable {
	public int id;
	public Topic topic;
	public String title;
	private String content;

	public Event(int id, Topic topic, String title, String content){
		this.id = id;
		this.topic = topic;
		this.title = title;
		this.content = content;
	}
}
