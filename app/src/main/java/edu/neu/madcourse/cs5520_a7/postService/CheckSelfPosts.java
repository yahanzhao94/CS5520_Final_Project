package edu.neu.madcourse.cs5520_a7.postService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

public class CheckSelfPosts extends AppCompatActivity {

  private static final String POST_TABLE = "Posts";
  private static final String USER_NAME = "username";

  private DatabaseReference mDatabase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_check_self_posts);
    mDatabase = FirebaseDatabase.getInstance().getReference();
  }


  private void getPostByUsername(String userName) {

    mDatabase.child(POST_TABLE).orderByChild(USER_NAME).equalTo(
      userName).addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        List<Post> posts = new ArrayList<>();
        if (snapshot.hasChildren()) {
          for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            Post post = dataSnapshot.getValue(Post.class);
            posts.add(post);
          }
        }
        posts.sort((o1, o2) -> Long.compare(o2.timestampInMillis, o1.timestampInMillis));
        updatePostsView(posts);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        // ...
      }
    });
  }

  private void updatePostsView(List<Post> posts) {
    for (Post post : posts) {
      System.out.println("Self post: " + post.postId);
    }
  }
}