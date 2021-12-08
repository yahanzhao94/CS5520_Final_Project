package edu.neu.madcourse.cs5520_a7.postService;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import edu.neu.madcourse.cs5520_a7.MainActivity;
import edu.neu.madcourse.cs5520_a7.R;

public class CreateNewPost extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();
  private static final String USER_TABLE = "Users";
  private DatabaseReference mDatabase;
  private String postUsername;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_new_post);
    mDatabase = FirebaseDatabase.getInstance().getReference();

  }
}