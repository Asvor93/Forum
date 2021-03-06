package dk.easv.ATForum.Posts;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Topic;

public class EditTopicActivity extends MenuActivity {
    // Tag used for logging
    private static final String TAG = "XYZ";

    // Interface used to accessing the database
    IDataAccess dataAccess;

    // The views used for input when changing the topic
    EditText txtTopicName, txtTopicDescription;

    // The topic that is being edited
    Topic topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_topic);

        Slidr.attach(this);

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

    /**
     * Finalizes the edit by getting the relevant data
     * and using it to call the updateTopic method of the dataAccess interface
     */
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

    /**
     * Gets the extra from the activity that started the intent uses it to set the gui
     */
    private void setGUI() {
        topic = (Topic) getIntent().getSerializableExtra("topic");

        txtTopicName.setText(topic.getTopicName());
        txtTopicDescription.setText(topic.getDescription());
    }
}
