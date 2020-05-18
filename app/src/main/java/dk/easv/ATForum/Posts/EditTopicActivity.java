package dk.easv.ATForum.Posts;

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
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Topic;

public class EditTopicActivity extends MenuActivity {
    private static final String TAG = "XYZ";
    IDataAccess dataAccess;
    EditText txtTopicName, txtTopicDescription;
    Topic topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);

        dataAccess = DataAccessFactory.getInstance();

        txtTopicName = findViewById(R.id.etEditTopicName);
        txtTopicDescription = findViewById(R.id.etEditTopicDescription);

        setGUI();

        Button btnSubmit = findViewById(R.id.submitEditTopic);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTopic();
            }
        });
    }

    private void editTopic() {
        final String topicNameString = txtTopicName.getText().toString();
        final String topicDescriptionString = txtTopicDescription.getText().toString();

        Map<String, Object> topicMap = new HashMap<>();
        topicMap.put("topicName", topicNameString);
        topicMap.put("description", topicDescriptionString);

        final String id = topic.getId();
        Log.d(TAG, "editTopic: " + id);

        dataAccess.updateTopic(topicMap, id, new IDataAccess.IOnResult<Topic>() {
            @Override
            public void onResult(Topic topic) {
                Toast.makeText(EditTopicActivity.this, "Topic successfully updated", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void setGUI() {
        topic = (Topic) getIntent().getSerializableExtra("topic");

        txtTopicName.setText(topic.getTopicName());
        txtTopicDescription.setText(topic.getDescription());
    }
}
