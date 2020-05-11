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

public class MainActivity extends MenuActivity {
    private static final String TAG = "XYZ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       super.onPrepareOptionsMenu(menu);
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
}
