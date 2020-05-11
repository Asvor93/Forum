package dk.easv.ATForum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.forum.R;

import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "XYZ";
    User currentUser;
    Role role;
    IDataAccess dataAccess;
    MenuItem profileMenuItem, adminMenuItem, signUpMenuItem, loginMenuItem, logoutMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataAccess = DataAccessFactory.getInstance();
    }

    // Creates the options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu1, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (currentUser != null && role != null) {
            if (profileMenuItem != null) {
                profileMenuItem.setVisible(true);
                profileMenuItem.setEnabled(true);
            }

            if (role.getRoleName().equals("admin") || role.getRoleName().equals("superAdmin")) {
                if (adminMenuItem != null) {
                    adminMenuItem.setVisible(true);
                    adminMenuItem.setEnabled(true);
                }
            }

            if (signUpMenuItem != null) {
                signUpMenuItem.setVisible(false);
                signUpMenuItem.setEnabled(false);
            }

            if (logoutMenuItem != null) {
                logoutMenuItem.setVisible(true);
                logoutMenuItem.setEnabled(true);
            }

            if (loginMenuItem != null) {
                loginMenuItem.setVisible(false);
                loginMenuItem.setEnabled(false);
            }
        }
        if (currentUser == null) {
            if (profileMenuItem != null) {
                profileMenuItem.setVisible(false);
                profileMenuItem.setEnabled(false);
            }

            if (adminMenuItem != null) {
                adminMenuItem.setVisible(false);
                adminMenuItem.setEnabled(false);
            }

            if (signUpMenuItem != null) {
                signUpMenuItem.setVisible(true);
                signUpMenuItem.setEnabled(true);
            }

            if (logoutMenuItem != null) {
                logoutMenuItem.setVisible(false);
                logoutMenuItem.setEnabled(false);
            }

            if (loginMenuItem != null) {
                loginMenuItem.setVisible(true);
                loginMenuItem.setEnabled(true);
            }
        }
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
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
            case R.id.logoutMenu:
                logout();
                return true;
            case R.id.signUpMenu:
                Intent signUpIntent = new Intent(this, SignUpActivity.class);
                startActivityForResult(signUpIntent, 2);
                return true;
            case R.id.adminPageMenu:
                Intent adminIntent = new Intent(this, AdminActivity.class);
                adminIntent.putExtra("user", currentUser);
                adminIntent.putExtra("role", role);
                startActivity(adminIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private  void logout() {
        dataAccess.logout();
        currentUser = null;
        role = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            currentUser = (User) data.getExtras().getSerializable("currentUser");
            role = (Role) data.getExtras().getSerializable("role");

        } else if (resultCode == RESULT_CANCELED) {
            if (currentUser == null) {
                profileMenuItem.setVisible(false);
            }
            Log.d(TAG, "main activity on activity result canceled");
        }

    }
}
