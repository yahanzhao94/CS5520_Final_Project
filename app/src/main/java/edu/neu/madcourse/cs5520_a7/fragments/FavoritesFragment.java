package edu.neu.madcourse.cs5520_a7.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import edu.neu.madcourse.cs5520_a7.postService.ItemClickListener;
import edu.neu.madcourse.cs5520_a7.postService.PostRviewAdapter;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Like;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String loginUserName;
    private String mParam2;

    public FavoritesFragment(String loginUserName) {
        // Required empty public constructor
        this.loginUserName = loginUserName;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param loginUserName Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String loginUserName, String param2) {
        FavoritesFragment fragment = new FavoritesFragment(loginUserName);
        Bundle args = new Bundle();
        args.putString(LOGIN_USER_NAME, loginUserName);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loginUserName = getArguments().getString(LOGIN_USER_NAME);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getLikedPostByUsername(loginUsername);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }
    private void createRecyclerView() {
        if(getActivity()==null)return;
        rLayoutManger = new LinearLayoutManager(getActivity());

        recyclerView = getActivity().findViewById(R.id.liked_post_recycler_view);
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