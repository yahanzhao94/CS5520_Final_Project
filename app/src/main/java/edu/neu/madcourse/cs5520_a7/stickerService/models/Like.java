package edu.neu.madcourse.cs5520_a7.stickerService.models;

public class Like {


  public String likeId;
  public String username;
  public String postId;

  public Like() {}

  public Like(String likeId, String username, String postId) {
    this.likeId = likeId;
    this.username = username;
    this.postId = postId;
  }
}
