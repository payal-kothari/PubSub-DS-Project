package edu.rit.CSCI652.demo;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable{
	public int id;
	public List<String> keywords;
	public String name;

	public Topic(int id, List<String> keywords, String name){
	    this.id = id;
	    this.keywords = keywords;
	    this.name = name;
    }
}
