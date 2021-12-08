package edu.neu.madcourse.cs5520_a7.stickerService.models;

public class Post {
  public String username;
  public String postId;
  public String text;
  public String location;

  public Post(String username, String postId, String text, String location) {
    this.username = username;
    this.postId = postId;
    this.text = text;
    this.location = location;
  }

}
