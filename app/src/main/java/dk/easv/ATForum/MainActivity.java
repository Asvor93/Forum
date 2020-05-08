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
    MenuItem profile, admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // Creates the options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu1, menu);
        profile = menu.findItem(R.id.profileMenu);
        admin = menu.findItem(R.id.adminPageMenu);
        if (currentUser == null) {
            if (profile != null) {
                profile.setVisible(false);
            }

            if (admin != null) {
                admin.setVisible(false);
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
            case R.id.adminPageMenu:
                Intent adminIntent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(adminIntent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            currentUser = (User) data.getExtras().getSerializable("currentUser");
            role = (Role) data.getExtras().getSerializable("role");
            profile.setVisible(true);

            if (role != null) {
                if (role.getRoleName().equals("admin") || role.getRoleName().equals("superAdmin")) {
                    admin.setVisible(true);
                }
            }

        } else if (resultCode == RESULT_CANCELED) {
            profile.setVisible(false);
            Log.d(TAG, "main activity on activity result canceled");
        }

    }
}
