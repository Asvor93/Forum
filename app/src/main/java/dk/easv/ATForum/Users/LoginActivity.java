package dk.easv.ATForum.Users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.forum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.r0adkll.slidr.Slidr;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class LoginActivity extends AppCompatActivity {
    // Tag for logging
    private static final String TAG = "XYZ";

    // Views for login inputs
    EditText loginEmail, loginPassword;

    // Interface to interact with the database
    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Slidr.attach(this);

        dataAccess = DataAccessFactory.getInstance();

        loginEmail = findViewById(R.id.txtEmail);
        loginEmail.setHint("Email");
        loginPassword = findViewById(R.id.txtPassword);
        loginPassword.setHint("Password");
        loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button loginSubmit = findViewById(R.id.btnLogin);
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    /**
     * Handles logging in and getting the role of the logged in user by calling the dataAccess interface
     */
    private void login() {
        String emailString = loginEmail.getText().toString();
        String passwordString = loginPassword.getText().toString();

        dataAccess.login(emailString, passwordString, new IDataAccess.IOnResult<User>() {
            @Override
            public void onResult(User user) {
                final Intent result = new Intent();
                result.putExtra("currentUser", user);

                dataAccess.getRole(user.getUid(), new IDataAccess.IOnResult<Role>() {
                    @Override
                    public void onResult(Role role) {
                        result.putExtra("role", role);
                        setResult(RESULT_OK, result);
                        finish();
                    }
                });
            }
        });
    }
}
