package edu.neu.madcourse.cs5520_a7.stickerService.models;

public class Post {
  public String username;
  public String postId;
  public String text;
  public String location;
  public String photoId;
  public long timestampInMillis;


  public Post() {}

  public Post(String username, String postId, String text, String photoId, String location, long timestampInMillis) {
    this.username = username;
    this.postId = postId;
    this.text = text;
    this.photoId = photoId;
    this.location = location;
    this.timestampInMillis = timestampInMillis;
  }

}
