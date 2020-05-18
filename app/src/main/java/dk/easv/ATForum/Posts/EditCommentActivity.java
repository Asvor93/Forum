package dk.easv.ATForum.Posts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forum.R;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Comment;
import dk.easv.ATForum.Models.Topic;

public class EditCommentActivity extends AppCompatActivity {

    IDataAccess dataAccess;
    EditText txtMessage;
    Comment comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);

        dataAccess = DataAccessFactory.getInstance();

        txtMessage = findViewById(R.id.etEditMessage);

        setGUI();

        Button btnSubmit = findViewById(R.id.submitEditComment);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editComment();
            }
        });
    }

    private void editComment() {
        final String messageString = txtMessage.getText().toString();

        Map<String, Object> topicMap = new HashMap<>();
        topicMap.put("message", messageString);

        final String id = comment.getId();
        Log.d("XYZ", "editComment: " + id);

        dataAccess.updateTopic(topicMap, id, new IDataAccess.IOnResult<Topic>() {
            @Override
            public void onResult(Topic topic) {
                Toast.makeText(EditCommentActivity.this, "Comment successfully updated", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void setGUI() {
        comment = (Comment) getIntent().getSerializableExtra("comment");

        txtMessage.setText(comment.getMessage());
    }
}
