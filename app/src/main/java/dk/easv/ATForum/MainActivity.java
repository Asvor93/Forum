package dk.easv.ATForum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forum.R;

import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "XYZ";
    User currentUser;
    Role role;
    MenuItem profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Button btnLoginMain = findViewById(R.id.btnMainLogin);
        btnLoginMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });

        Button btnSignUp = findViewById(R.id.btnMainSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity();
            }
        });

        Button btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfileActivity();
            }
        });
        */

    }

    // Creates the options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu1, menu);
        profile = menu.findItem(R.id.profileMenu);
        if (currentUser == null) {
            if (profile != null) {
                profile.setVisible(false);
            }
        }
        return true;
    }

    // Handles menu selection.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileMenu:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("user", currentUser);
                startActivityForResult(intent, 3);
                return true;
            case R.id.loginMenu:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivityForResult(loginIntent, 1);
                return true;
            case R.id.signUpMenu:
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivityForResult(signUpIntent, 2);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", currentUser);
        startActivityForResult(intent, 3);
    }

     */

    /*
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            currentUser = (User) data.getExtras().getSerializable("currentUser");
            role = (Role) data.getExtras().getSerializable("role");
            profile.setVisible(true);
        } else if (resultCode == RESULT_CANCELED) {
            profile.setVisible(false);
            Log.d(TAG, "main activity on activity result canceled");
        }

    }

    /*
    private void startSignUpActivity() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivityForResult(intent, 2);
    }
     */
}
