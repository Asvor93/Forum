package dk.easv.ATForum.Posts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.forum.R;
import com.google.firebase.firestore.FieldValue;
import com.r0adkll.slidr.Slidr;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Comment;

public class CreateCommentActivity extends MenuActivity {
    // The interface to facilitate data access
    IDataAccess dataAccess;

    // Contains the text to edit
    EditText txtComment;

    // The id of the topic that the comment is linked to
    String topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_comment);

        Slidr.attach(this);
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

    // Gets the topic id related to the comment
    private void getExtras() {
        topicId = getIntent().getStringExtra("topicId");
        Log.d("XYZ", "getExtras: " + topicId);
    }

    // Calls the database to create a comment and then finishes when it is done
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
