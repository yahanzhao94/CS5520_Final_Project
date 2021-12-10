package edu.neu.madcourse.cs5520_a7.postService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Like;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

public class CheckSelfPosts extends AppCompatActivity {

  private static final String POST_TABLE = "Posts";
  private static final String USER_NAME = "username";

  private DatabaseReference mDatabase;
  private String loginUsername;

  private List<Post> posts = new ArrayList<>();

  private RecyclerView recyclerView;
  private PostRviewAdapter rviewAdapter;
  private RecyclerView.LayoutManager rLayoutManger;
  private TextView tv_name;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_check_self_posts);
    mDatabase = FirebaseDatabase.getInstance().getReference();
    loginUsername = getIntent().getStringExtra("login_username");
    tv_name = findViewById(R.id.tv_name);
    tv_name.setText(loginUsername);
    getPostByUsername(loginUsername);
  }

  private void createRecyclerView() {

    rLayoutManger = new LinearLayoutManager(this);

    recyclerView = findViewById(R.id.self_post_recycler_view);
    recyclerView.setHasFixedSize(true);

    rviewAdapter = new PostRviewAdapter(posts);
    ItemClickListener itemClickListener = new ItemClickListener() {
      @Override
      public void onItemClick(int position) {
      }
    };
    rviewAdapter.setOnItemClickListener(itemClickListener);

    recyclerView.setAdapter(rviewAdapter);
    recyclerView.setLayoutManager(rLayoutManger);


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
    this.posts = posts;
    createRecyclerView();
  }
}