package dk.easv.ATForum;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private static final String TAG = "XYZ";
    IDataAccess dataAccess;
    private FavoriteTopicAdapter favoriteTopicAdapter;
    private List<Topic> favTopicList;
    private ListView topicListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataAccess = DataAccessFactory.getInstance();

        topicListView = findViewById(R.id.list);


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
                    Toast.makeText(MainActivity.this, "Deleted Favorite topic with id: " + uid, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }

    protected void onResume() {
        super.onResume();
        if (currentUser != null) {
            Log.d(TAG, "currentUser: " + currentUser);
            dataAccess.getFavoriteTopics(currentUser.getUid(), new IDataAccess.IOnResult<List<Topic>>() {
                @Override
                public void onResult(List<Topic> favoriteTopics) {
                    favTopicList = favoriteTopics;
                    favoriteTopicAdapter = new FavoriteTopicAdapter(MainActivity.this, R.layout.topic_cell, favTopicList);
                    topicListView.setAdapter(favoriteTopicAdapter);
                    Log.d(TAG, "favTopicList: " + favTopicList);
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
