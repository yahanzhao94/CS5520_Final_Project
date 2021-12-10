package edu.neu.madcourse.cs5520_a7.postService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Like;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

public class CheckLikedPosts extends AppCompatActivity {

  private static final String POST_TABLE = "Posts";
  private static final String LIKE_TABLE = "Likes";
  private static final String USER_NAME = "username";

  private static final String TIMESTAMP = "timestampInMillis";

  private DatabaseReference mDatabase;

  private List<Post> posts = new ArrayList<>();
  private Map<String, Like> likeByPostId = new HashMap<>();
  private Set<String> likedPostIds = new HashSet<>();

  private RecyclerView recyclerView;
  private PostRviewAdapter rviewAdapter;
  private RecyclerView.LayoutManager rLayoutManger;
  private String loginUsername;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_check_liked_posts);
    mDatabase = FirebaseDatabase.getInstance().getReference();
    loginUsername = getIntent().getStringExtra("login_username");
    getLikedPostByUsername(loginUsername);
  }


  private void createRecyclerView() {

    rLayoutManger = new LinearLayoutManager(this);

    recyclerView = findViewById(R.id.liked_post_recycler_view);
    recyclerView.setHasFixedSize(true);

    rviewAdapter = new PostRviewAdapter(posts, likedPostIds);
    ItemClickListener itemClickListener = new ItemClickListener() {
      @Override
      public void onItemClick(int position) {
        Post post = posts.get(position);
        if (!likedPostIds.contains(post.postId)) {
          String likeId = UUID.randomUUID().toString();
          Like like = new Like(likeId, loginUsername, post.postId);
          mDatabase.child(LIKE_TABLE).child(likeId).setValue(like).
            addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                getLikedPostByUsername(loginUsername);
              }
            });
        } else {
          mDatabase.child(LIKE_TABLE).child(likeByPostId.get(post.postId).likeId).removeValue()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
              @Override
              public void onComplete(@NonNull Task<Void> task) {
                getLikedPostByUsername(loginUsername);
              }
            });
        }

      }
    };
    rviewAdapter.setOnItemClickListener(itemClickListener);

    recyclerView.setAdapter(rviewAdapter);
    recyclerView.setLayoutManager(rLayoutManger);


  }


  private void getLikedPostByUsername(String userName) {

    mDatabase.child(LIKE_TABLE).orderByChild(USER_NAME).equalTo(loginUsername).addValueEventListener(
      new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          List<Like> likes = new ArrayList<>();
          List<Post> likedPost = new ArrayList<>();
          if (snapshot.hasChildren()) {
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
              Like like = dataSnapshot.getValue(Like.class);
              likes.add(like);
              mDatabase.child(POST_TABLE).child(like.postId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                  Post post = snapshot.getValue(Post.class);
                  likedPost.add(post);
                  updatePostsView(likedPost, likes);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
              });
            }
          }
          updatePostsView(likedPost, likes);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
      });
  }

  private void updatePostsView(List<Post> posts, List<Like> likes) {
    posts.sort((o1, o2) -> Long.compare(o2.timestampInMillis, o1.timestampInMillis));
    this.posts = posts;
    Set<String> likePostIds = new HashSet<>();
    for (Like like : likes) {
      likePostIds.add(like.postId);
      likeByPostId.put(like.postId, like);
    }
    this.likedPostIds = likePostIds;
    createRecyclerView();
  }
}