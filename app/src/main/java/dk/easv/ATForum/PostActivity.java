package dk.easv.ATForum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.forum.R;

public class PostActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentUser != null && role != null) {
            if (profileMenuItem != null) {
                profileMenuItem.setVisible(true);
                profileMenuItem.setEnabled(true);
            }

            if (adminMenuItem != null) {
                adminMenuItem.setVisible(true);
                adminMenuItem.setEnabled(true);
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

            if (categoryMenuItem != null) {
                categoryMenuItem.setVisible(false);
                categoryMenuItem.setEnabled(false);
            }
        }
        categoryMenuItem.setVisible(false);
        categoryMenuItem.setEnabled(false);
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }
}
