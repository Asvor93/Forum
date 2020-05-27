package dk.easv.ATForum;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Adapters.FavoriteTopicAdapter;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Topic;
import dk.easv.ATForum.Posts.CommentActivity;

public class MainActivity extends MenuActivity {
    // Tag used for logging
    private static final String TAG = "XYZ";

    // The interface that facilitates data access
    IDataAccess dataAccess;

    // The adapter for the favorite topics
    private FavoriteTopicAdapter favoriteTopicAdapter;

    // The list of favorite topics for the current user
    private List<Topic> favTopicList;

    // Shows the current user's favorite topics
    private ListView topicListView;


    // Sets the contentView for when there is no current user by default
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (currentUser == null) {
            setContentView(R.layout.home_page);
        }

    }

    // Switches between the contentViews depending on whether there is any favorite topics or not
    protected void onResume() {
        super.onResume();

        if (currentUser != null) {
            setContentView(R.layout.activity_main);

            dataAccess = DataAccessFactory.getInstance();

            topicListView = findViewById(R.id.list);

            dataAccess.getFavoriteTopics(currentUser.getUid(), new IDataAccess.IOnResult<List<Topic>>() {
                @Override
                public void onResult(List<Topic> favoriteTopics) {
                    if (!favoriteTopics.isEmpty()) {
                        favTopicList = favoriteTopics;
                        favoriteTopicAdapter = new FavoriteTopicAdapter(MainActivity.this, R.layout.topic_cell, favTopicList);
                        topicListView.setAdapter(favoriteTopicAdapter);
                    } else {
                        setContentView(R.layout.home_page);
                    }
                }
            });

            topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String topicUid = favTopicList.get(position).getId();
                    Intent commentIntent = new Intent(MainActivity.this, CommentActivity.class);
                    commentIntent.putExtra("topicId", topicUid);
                    startActivity(commentIntent);
                }
            });

            topicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    topicListView.setLongClickable(true);
                    Topic topic = favTopicList.get(position);
                    String uid = currentUser.getUid();
                    dataAccess.deleteFavoriteTopic(uid, topic);
                    favTopicList.remove(position);
                    favoriteTopicAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Deleted Favorite topic with id: " + uid, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        mainPageMenuItem.setVisible(false);
        mainPageMenuItem.setEnabled(false);
        return true;
    }
}
