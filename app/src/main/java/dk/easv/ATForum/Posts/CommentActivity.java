package dk.easv.ATForum.Posts;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import dk.easv.ATForum.Adapters.CommentAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Comment;


public class CommentActivity extends MenuActivity {
    // The id of the topic
    private String topicId;
    private IDataAccess dataAccess;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private ListView commentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Slidr.attach(this);

        commentListView = findViewById(R.id.comList);
        GetExtras();
        dataAccess = DataAccessFactory.getInstance();
        dataAccess.getComments(topicId, new IDataAccess.IOnResult<List<Comment>>() {
            @Override
            public void onResult(List<Comment> comments) {
                commentList = comments;
                commentAdapter = new CommentAdapter(CommentActivity.this, R.layout.comment_cell, comments, currentUser, role);
                commentListView.setAdapter(commentAdapter);
            }
        });

        commentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                commentListView.setLongClickable(true);
                Comment comment = commentList.get(position);
                String uid = comment.getId();

                dataAccess.deleteComment(uid);
                Toast.makeText(CommentActivity.this, "Deleted comment with id: " + uid, Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        Button btnGoToCreateComment = findViewById(R.id.createComment);
        btnGoToCreateComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCreateComment();
            }
        });

        if (currentUser == null) {
            btnGoToCreateComment.setVisibility(View.GONE);
        }
    }

    private void goToCreateComment() {
        Intent createCommentIntent = new Intent(CommentActivity.this, CreateCommentActivity.class);
        createCommentIntent.putExtra("topicId", topicId);
        startActivity(createCommentIntent);
    }

    private void GetExtras() {
        topicId = getIntent().getStringExtra("topicId");
    }
}
