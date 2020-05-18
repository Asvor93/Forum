package dk.easv.ATForum.Posts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.forum.R;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Topic;

public class CreateTopicActivity extends MenuActivity {

    EditText etTopicName, etDescription;
    IDataAccess dataAccess;

    String catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);

        dataAccess = DataAccessFactory.getInstance();

        etTopicName = findViewById(R.id.etNewTopicName);
        etDescription = findViewById(R.id.etNewTopicDescription);
        getExtras();

        Button btnSubmit = findViewById(R.id.submitNewTopic);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTopic();
            }
        });
    }

    private void createTopic() {
        final String topicNameString = etTopicName.getText().toString();
        final String topicDescriptionString = etDescription.getText().toString();

        Map<String, Object> topic = new HashMap<>();
        topic.put("topicName", topicNameString);
        topic.put("description", topicDescriptionString);
        topic.put("author", currentUser);
        topic.put("categoryId", catId);

        dataAccess.createTopic(topic, new IDataAccess.IOnResult<Topic>() {
            @Override
            public void onResult(Topic topic) {
                finish();
            }
        });

    }

    private void getExtras() {
        catId = getIntent().getStringExtra("catId");
    }
}
