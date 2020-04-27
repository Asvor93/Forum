package dk.easv.ATForum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forum.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.easv.ATForum.Models.User;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "XYZ";
    EditText loginEmail, loginPassword;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loginEmail = findViewById(R.id.txtEmail);
        loginPassword = findViewById(R.id.txtPassword);
        loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button loginSubmit = findViewById(R.id.btnLogin);

        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String emailString = loginEmail.getText().toString();
        String passwordString = loginPassword.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Login successful " + task.getResult().getUser().getEmail());
                            db.collection("users").document(task.getResult()
                                    .getUser().getUid()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        User user = null;
                                        if (document != null) {
                                            user = document.toObject(User.class);
                                            if (user != null) {
                                                Intent result = new Intent();
                                                result.putExtra("currentUser", user);
                                                setResult(RESULT_OK, result);
                                                Log.d(TAG, "Document: " + user.toString());
                                                finish();
                                            }
                                        } else {
                                            Log.d(TAG, "No such user");
                                        }
                                    } else {
                                        Log.d(TAG, "Failed with message: ", task.getException());
                                    }
                                }
                            });
                        }
                        else{
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }
