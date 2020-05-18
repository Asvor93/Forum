package dk.easv.ATForum.Posts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.forum.R;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Comment;

public class CreateCommentActivity extends MenuActivity {

    IDataAccess dataAccess;
    EditText txtComment;
    String topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);
        getExtras();
        dataAccess = DataAccessFactory.getInstance();

        txtComment = findViewById(R.id.etNewCommentMsg);

        Button btnSubmit = findViewById(R.id.submitNewComment);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComment();
            }
        });
    }

    private void getExtras() {
        topicId = getIntent().getStringExtra("topicId");
        Log.d("XYZ", "getExtras: " + topicId);
    }

    private void createComment() {
        final String commentString = txtComment.getText().toString();

        Map<String, Object> comment = new HashMap<>();
        comment.put("message", commentString);
        comment.put("author", currentUser);
        comment.put("topicId", topicId);
        comment.put("timestamp", FieldValue.serverTimestamp());

        dataAccess.createComment(comment, new IDataAccess.IOnResult<Comment>() {
            @Override
            public void onResult(Comment comment) {
                finish();
            }
        });
    }
}
