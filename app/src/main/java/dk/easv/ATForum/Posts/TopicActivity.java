package dk.easv.ATForum.Posts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
        topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String topicUid = topicList.get(position).getId();
                Intent commentIntent = new Intent( TopicActivity.this, CommentActivity.class);
                commentIntent.putExtra("topicUid", topicUid);
                startActivity(commentIntent);
            }
        });
    }

    private void GetExtras() {
        catId = getIntent().getStringExtra("catId");
    }
}
