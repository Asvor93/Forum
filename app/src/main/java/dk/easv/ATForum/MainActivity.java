package dk.easv.ATForum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.forum.R;

import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class MainActivity extends AppCompatActivity {
    User currentUser;
    Role role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }

    private void startProfileActivity() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", currentUser);
        startActivityForResult(intent, 3);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            currentUser = (User) data.getExtras().getSerializable("currentUser");
            role = (Role) data.getExtras().getSerializable("role");
        }
    }

    private void startSignUpActivity() {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivityForResult(intent, 2);
    }
}
