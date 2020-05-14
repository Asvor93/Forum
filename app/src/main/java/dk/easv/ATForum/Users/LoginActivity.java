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

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "XYZ";
    EditText loginEmail, loginPassword;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void login() {
        String emailString = loginEmail.getText().toString();
        String passwordString = loginPassword.getText().toString();

        dataAccess.login(emailString, passwordString, new IDataAccess.IONUserResult() {
            @Override
            public void onResult(User user) {
                final Intent result = new Intent();
                result.putExtra("currentUser", user);

                dataAccess.getRole(user.getUid(), new IDataAccess.IONRoleResult() {
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
