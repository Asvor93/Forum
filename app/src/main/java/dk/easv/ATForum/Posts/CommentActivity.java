package dk.easv.ATForum.Posts;


import android.os.Bundle;
import android.widget.ListView;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Adapters.CommentAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Comment;


public class CommentActivity extends MenuActivity {
    private String topicId;
    private IDataAccess dataAccess;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private ListView commentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentListView = findViewById(R.id.comList);
        GetExtras();
        dataAccess = DataAccessFactory.getInstance();
        dataAccess.getComments(topicId, new IDataAccess.IOnResult<List<Comment>>() {
            @Override
            public void onResult(List<Comment> comments) {
                commentList = comments;
                commentAdapter = new CommentAdapter(CommentActivity.this, R.layout.comment_cell, comments);
                commentListView.setAdapter(commentAdapter);
            }
        });

    }

    private void GetExtras() {
        topicId = getIntent().getStringExtra("topicUid");
    }
}
