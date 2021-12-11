package edu.neu.madcourse.cs5520_a7.postService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.util.UUID;

import edu.neu.madcourse.cs5520_a7.MainActivity;
import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.stickerService.models.Post;

public class CreateNewPost extends AppCompatActivity {

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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fragment_create_fragments);

    //Get location
    System.out.println("Before get location");
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    String loginUsername = getIntent().getStringExtra("login_username");
    createButton = findViewById(R.id.create_post);
    postText = findViewById(R.id.postText);

    //Get image
    ImageView ivHeart = (ImageView) findViewById(R.id.heart2);
    ImageView ivLove = (ImageView) findViewById(R.id.love);
    ImageView ivTeamWork = (ImageView) findViewById(R.id.team_work2);
    ImageView ivHappyNewYear = (ImageView) findViewById(R.id.happy_new_year2);
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
        Log.d("logInfo=:", loginUsername);
        createPost(loginUsername, stickerId, text, currentLocation);
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
    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    Location location = locationManager.getLastKnownLocation("gps");
    locationListener = new LocationListener() {
      @Override
      public void onLocationChanged(@NonNull Location location) {
        currentLocation = (location.getLatitude() + ", " + location.getLongitude());
      }
    };
    if (location != null) {
      currentLocation = (location.getLatitude() + ", " + location.getLongitude());
    } else {
      locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
    }
  }

  // create a new post
  private void createPost(String username, String photoId, String text, String location) {
    String postId = UUID.randomUUID().toString();
    Post post = new Post(username, postId, text, photoId, currentLocation, Instant.now().toEpochMilli());
    mDatabase.child(POST_TABLE).child(postId).setValue(post);
    Toast.makeText(CreateNewPost.this, "Post successfully!", Toast.LENGTH_SHORT).show();
  }
}