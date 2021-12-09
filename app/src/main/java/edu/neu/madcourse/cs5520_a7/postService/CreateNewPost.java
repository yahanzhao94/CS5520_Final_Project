package edu.neu.madcourse.cs5520_a7.postService;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.UUID;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

public class CreateNewPost extends AppCompatActivity {

  private static final String TAG = CreateNewPost.class.getSimpleName();
  private static final String POST_TABLE = "Posts";
  private DatabaseReference mDatabase;
  private Button createButton;
  private EditText postText;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_new_post);
    mDatabase = FirebaseDatabase.getInstance().getReference();
    String loginUsername = getIntent().getStringExtra("login_username");
    createButton = findViewById(R.id.create_post);
    postText = findViewById(R.id.postText);
    createButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String text = postText.getText().toString();
        Log.d("logInfo=:", loginUsername);
        createPost(loginUsername, String.valueOf(R.drawable.heart), text, "fake location");
      }
    });

  }


  // Sends sticker to another user
  private void createPost(String username, String photoId, String text, String location) {
    String postId = UUID.randomUUID().toString();
    Post post = new Post(username, postId, text, photoId, location, Instant.now().toEpochMilli());
    mDatabase.child(POST_TABLE).child(postId).setValue(post);
  }
}