package edu.neu.madcourse.cs5520_a7.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.postService.ItemClickListener;
import edu.neu.madcourse.cs5520_a7.postService.PostRviewAdapter;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private static final String POST_TABLE = "Posts";
    private static final String USER_NAME = "username";

    private DatabaseReference mDatabase;

    private List<Post> posts = new ArrayList<>();

    private RecyclerView recyclerView;
    private PostRviewAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private TextView tv_name;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String loginUserName;
    private String mParam2;

    public AccountFragment(String loginUserName) {
        // Required empty public constructor
        this.loginUserName = loginUserName;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param loginUserName Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(String loginUserName, String param2) {
        CreateFragment fragment = new CreateFragment(loginUserName);
        Bundle args = new Bundle();
        args.putString(LOGIN_USER_NAME, loginUserName);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
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
        getPostByUsername(loginUserName);
    }
    private void createRecyclerView() {
        tv_name = getActivity().findViewById(R.id.tv_name);
        tv_name.setText(loginUserName);
        rLayoutManger = new LinearLayoutManager(getActivity());

        recyclerView = getActivity().findViewById(R.id.self_post_recycler_view);
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