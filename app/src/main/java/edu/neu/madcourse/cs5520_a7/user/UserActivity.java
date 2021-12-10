package edu.neu.madcourse.cs5520_a7.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import com.google.firebase.database.DatabaseReference;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.postService.CheckAllPosts;
import edu.neu.madcourse.cs5520_a7.postService.CheckLikedPosts;
import edu.neu.madcourse.cs5520_a7.postService.CheckSelfPosts;
import edu.neu.madcourse.cs5520_a7.postService.CreateNewPost;

public class UserActivity extends AppCompatActivity {

  private static final String TAG = UserActivity.class.getSimpleName();
  private String loginUsername = "";
  private DatabaseReference mDatabase;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user);
    loginUsername = getIntent().getStringExtra("login_username");
    System.out.println("Login user name: " + loginUsername);
  }

  public void goToCreateNewPost(View view) {
    Intent intent = new Intent(getBaseContext(), CreateNewPost.class);
    intent.putExtra("login_username", loginUsername);
    startActivity(intent);
  }

  public void goToCheckAllPosts(View view) {
    Intent intent = new Intent(getBaseContext(), CheckAllPosts.class);
    intent.putExtra("login_username", loginUsername);
    startActivity(intent);
  }

  public void goToCheckSelfPosts(View view) {
    Intent intent = new Intent(getBaseContext(), CheckSelfPosts.class);
    intent.putExtra("login_username", loginUsername);
    startActivity(intent);
  }

  public void goToCheckLikedPosts(View view) {
    Intent intent = new Intent(getBaseContext(), CheckLikedPosts.class);
    intent.putExtra("login_username", loginUsername);
    startActivity(intent);
  }

}
