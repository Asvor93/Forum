package dk.easv.ATForum.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import dk.easv.ATForum.Adapters.TopicAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Topic;

public class TopicActivity extends MenuActivity {
    // Tag used for logging
    private static final String TAG = "XYZ";

    // The interface that handles data access
    private IDataAccess dataAccess;

    // The adapter that manages the views of the list view
    private TopicAdapter topicAdapter;

    // The list that populates the adapter
    private List<Topic> topicList;

    // The list view that shows all our
    private ListView topicListView;

    // The id of the category that opened this activity
    private String catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Slidr.attach(this);
        GetExtras();
        topicListView = findViewById(R.id.list);
        Log.d(TAG, "Role topic activity: " + role.getRoleName());
        dataAccess = DataAccessFactory.getInstance();

        // Gets the topics from the database and sets them in the onResult callback
        dataAccess.getTopics(catId, new IDataAccess.IOnResult<List<Topic>>() {
            @Override
            public void onResult(List<Topic> topics) {
                topicList = topics;
                topicAdapter = new TopicAdapter(TopicActivity.this, R.layout.topic_cell, topicList, currentUser);
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

        if (currentUser == null) {
            btnCreateTopic.setVisibility(View.GONE);
        }

        // Opens up the comment activity based on which topic is clicked on
		topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String topicUid = topicList.get(position).getId();
                Intent commentIntent = new Intent( TopicActivity.this, CommentActivity.class);
                commentIntent.putExtra("topicId", topicUid);
                startActivity(commentIntent);
            }
        });

        // Handles deleting topic based on position. Only admins and super admins can delete topics
		topicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                topicListView.setLongClickable(true);
                Topic topic = topicList.get(position);
                String uid = topic.getId();
                if (!role.getRoleName().equals("user")) {
                    dataAccess.deleteTopic(uid);
                    Toast.makeText(TopicActivity.this, "Deleted topic with id: " + topic.getTopicName(), Toast.LENGTH_SHORT).show();
                }
                topicList.remove(position);
                topicAdapter.notifyDataSetChanged();
                return true;
            }
        });

    }

    // Starts create activity
    private void goToCreateTopic() {
        Intent createTopicIntent = new Intent(TopicActivity.this, CreateTopicActivity.class);
        createTopicIntent.putExtra("catId", catId);
        startActivity(createTopicIntent);
	}

	// Gets the id from the intent
    private void GetExtras() {
        catId = getIntent().getStringExtra("catId");
    }

}
