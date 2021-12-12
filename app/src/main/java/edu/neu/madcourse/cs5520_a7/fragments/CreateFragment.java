package edu.neu.madcourse.cs5520_a7.fragments;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.UUID;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.postService.CreateNewPost;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {
    private static final String TAG = CreateNewPost.class.getSimpleName();
    private static final String POST_TABLE = "Posts";
    private DatabaseReference mDatabase;
    private Button createButton;
    private EditText postText;

    private String stickerId;
    private ImageView selectedImage;
    private static final int REQUEST_CODE = 1;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String currentLocation;
    private TextView txtLat;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LOGIN_USER_NAME = "LOGIN_USER_NAME";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String loginUserName;
    private String mParam2;

    public CreateFragment(String loginUserName) {
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
        return inflater.inflate(R.layout.fragment_create_fragments, container, false);
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
        //Get location
        System.out.println("Before get location");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Come in the permission");
            requestPermissions(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, REQUEST_CODE);

            currentLocation = "";
        } else {
            getLocation();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        createButton = getActivity().findViewById(R.id.create_post);
        postText = getActivity().findViewById(R.id.postText);

        //Get image
        ImageView ivHeart = (ImageView) getActivity().findViewById(R.id.heart2);
        ImageView ivLove = (ImageView) getActivity().findViewById(R.id.love);
        ImageView ivTeamWork = (ImageView) getActivity().findViewById(R.id.team_work2);
        ImageView ivHappyNewYear = (ImageView) getActivity().findViewById(R.id.happy_new_year2);
        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerId = String.valueOf(R.drawable.heart);
                ivHeart.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);

                selectedImage = ivHeart;
                Log.d("logInfo=:",stickerId);
            }
        });
        ivLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerId = String.valueOf(R.drawable.love);
                ivLove.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                selectedImage = ivLove;
                Log.d("logInfo=:",stickerId);
            }
        });
        ivTeamWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerId = String.valueOf(R.drawable.team_work);
                ivTeamWork.getDrawable().setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
                selectedImage = ivTeamWork;
                Log.d("logInfo=:",stickerId);
            }
        });
        ivHappyNewYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerId = String.valueOf(R.drawable.happy_new_year);
                ivHappyNewYear.getDrawable().setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
                selectedImage = ivHappyNewYear;
                Log.d("logInfo=:",stickerId);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = postText.getText().toString();
                selectedImage.getDrawable().clearColorFilter();
                Log.d("logInfo=:", loginUserName);
                createPost(loginUserName, stickerId, text, currentLocation);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
        }
    }
    private void getLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation("gps");
        txtLat = getActivity().findViewById(R.id.textView);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = (location.getLatitude() + ", " + location.getLongitude());
                txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
            }
        };
        if (location != null) {
            currentLocation = (location.getLatitude() + ", " + location.getLongitude());
            txtLat.setText("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }
    }

    // create a new post
    private void createPost(String username, String photoId, String text, String location) {
        String postId = UUID.randomUUID().toString();
        Post post = new Post(username, postId, text, photoId, currentLocation, Instant.now().toEpochMilli());
        mDatabase.child(POST_TABLE).child(postId).setValue(post);
        Toast.makeText(getActivity(), "Post successfully!", Toast.LENGTH_SHORT).show();
    }
}