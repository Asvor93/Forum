package dk.easv.ATForum.Posts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Adapters.TopicAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Topic;

public class TopicActivity extends MenuActivity {
    private IDataAccess dataAccess;
    private TopicAdapter topicAdapter;
    private List<Topic> topicList;
    private ListView topicListView;
    private String catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        GetExtras();
        topicListView = findViewById(R.id.list);

        dataAccess = DataAccessFactory.getInstance();
        dataAccess.getTopics(catId, new IDataAccess.IONTopicsResult() {
            @Override
            public void onResult(List<Topic> topics) {
                topicList = topics;
                topicAdapter = new TopicAdapter(TopicActivity.this, R.layout.topic_cell, topicList);
                topicListView.setAdapter(topicAdapter);
            }
        });

        Button btnCreateTopic = findViewById(R.id.createTopic);
        btnCreateTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateTopic();
            }
        });

    }

    private void goToCreateTopic() {
        Intent createTopicIntent = new Intent(TopicActivity.this, CreateTopicActivity.class);
        createTopicIntent.putExtra("catId", catId);
        startActivity(createTopicIntent);
    }

    private void GetExtras() {
        catId = getIntent().getStringExtra("catId");
    }
}
