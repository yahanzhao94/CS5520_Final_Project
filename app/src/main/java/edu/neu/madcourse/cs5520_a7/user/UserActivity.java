package edu.neu.madcourse.cs5520_a7.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import edu.neu.madcourse.cs5520_a7.R;
import edu.neu.madcourse.cs5520_a7.fragments.AccountFragment;
import edu.neu.madcourse.cs5520_a7.fragments.CreateFragment;
import edu.neu.madcourse.cs5520_a7.fragments.FavoritesFragment;
import edu.neu.madcourse.cs5520_a7.fragments.HomeFragment;

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
    BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
    bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

    // as soon as the application opens the first
    // fragment should be shown to the user
    // in this case it is algorithm fragment
    getSupportFragmentManager().beginTransaction().replace(R.id.fl_wrapper, new HomeFragment(loginUsername)).commit();
  }
  private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      // By using switch we can easily get
      // the selected fragment
      // by using there id.
      Fragment selectedFragment = null;
      switch (item.getItemId()) {
        case R.id.ic_home:
          selectedFragment = new HomeFragment(loginUsername);
          break;
        case R.id.ic_add:
          selectedFragment = new CreateFragment(loginUsername);
          break;
        case R.id.ic_favorites:
          selectedFragment = new FavoritesFragment(loginUsername);
          break;
        case R.id.ic_account:
          selectedFragment = new AccountFragment(loginUsername);
          break;
      }
      // It will help to replace the
      // one fragment to other.
      getSupportFragmentManager()
              .beginTransaction()
              .replace(R.id.fl_wrapper, selectedFragment)
              .commit();
      return true;
    }
  };

}
