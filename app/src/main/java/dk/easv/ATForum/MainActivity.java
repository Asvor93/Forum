package dk.easv.ATForum;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Adapters.TopicAdapter;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Topic;

public class MainActivity extends MenuActivity {
    private static final String TAG = "XYZ";
    IDataAccess dataAccess;
    private TopicAdapter topicAdapter;
    private List<Topic> favTopicList;
    private ListView topicListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataAccess = DataAccessFactory.getInstance();

        Log.d(TAG, "currentUser: " + currentUser);

        topicListView = findViewById(R.id.list);

        topicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                topicListView.setLongClickable(true);
                Topic topic = favTopicList.get(position);
                String uid = currentUser.getUid();
                if (currentUser != null) {

                }
                dataAccess.deleteFavoriteTopic(uid, topic);
                    Toast.makeText(MainActivity.this, "Deleted Favorite topic with id: " + uid, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currentUser != null) {
            Log.d(TAG, "currentUser: " + currentUser);
            dataAccess.getFavoriteTopics(currentUser.getUid(), new IDataAccess.IOnResult<List<Topic>>() {
                @Override
                public void onResult(List<Topic> favoriteTopics) {
                    favTopicList = favoriteTopics;
                    topicAdapter = new TopicAdapter(MainActivity.this, R.layout.topic_cell, favTopicList, currentUser);
                    topicListView.setAdapter(topicAdapter);
                    Log.d(TAG, "favTopicList: " + favTopicList);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }
}
