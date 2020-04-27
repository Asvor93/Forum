package dk.easv.ATForum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.forum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.easv.ATForum.Models.User;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "XYZ";
    EditText nameSignUp, emailSignUp, usernameSignUp, passwordSignUp;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        nameSignUp = findViewById(R.id.txtNameSignUp);
        emailSignUp = findViewById(R.id.txtEmailSignUp);
        usernameSignUp = findViewById(R.id.txtUserNameSignUp);
        passwordSignUp = findViewById(R.id.txtPasswordSignUp);

        Button btnConfirm = findViewById(R.id.btnConfirmSignUp);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        final String emailString = emailSignUp.getText().toString();
        final String passwordString = passwordSignUp.getText().toString();
        final String nameString = nameSignUp.getText().toString();
        final String userNameString = usernameSignUp.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final User newUser = new User(userNameString, nameString, emailString, "user");
                    db.collection("users").add(newUser).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent();
                                intent.putExtra("currentUser", newUser);
                                finish();
                            } else {
                                Log.d(TAG, "failed adding user to database");
                            }
                        }
                    });
                } else {
                    Log.d(TAG, "failed to create auth user");
                }
            }
        });
    }
}
