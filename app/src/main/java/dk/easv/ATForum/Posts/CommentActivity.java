package dk.easv.ATForum.Posts;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import dk.easv.ATForum.Adapters.CommentAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Comment;


public class CommentActivity extends MenuActivity {
    // Request code for creating a comment
    private static final int COMMENT_CREATE_REQUEST_CODE = 7;

    // The id of the topic
    private String topicId;

    // The interface that handles data access
    private IDataAccess dataAccess;

    // The adapter for the comments
    private CommentAdapter commentAdapter;

    // The list that the adapter uses
    private List<Comment> commentList;

    // The listView that holds all comments
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
                commentAdapter = new CommentAdapter(CommentActivity.this, R.layout.comment_cell, comments, currentUser);
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

    /**
     * Starts the CreateComment activity and puts the topic id as an extra
     */
    private void goToCreateComment() {
        Intent createCommentIntent = new Intent(CommentActivity.this, CreateCommentActivity.class);
        createCommentIntent.putExtra("topicId", topicId);
        startActivityForResult(createCommentIntent, COMMENT_CREATE_REQUEST_CODE);
    }

    /**
     * Gets the topic id from the intent that started the activity
     */
    private void GetExtras() {
        topicId = getIntent().getStringExtra("topicId");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Comment c = (Comment) data.getExtras().getSerializable("newComment");
            if (c != null) {
                if (requestCode == COMMENT_CREATE_REQUEST_CODE) {
                    commentList.add(c);
                    commentAdapter.notifyDataSetChanged();
                }
            }


        }
    }
}
