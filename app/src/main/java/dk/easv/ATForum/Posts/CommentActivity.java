package dk.easv.ATForum.Posts;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import dk.easv.ATForum.Adapters.CommentAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Comment;
import dk.easv.ATForum.Notifications;


public class CommentActivity extends MenuActivity {
    private String topicId;
    private IDataAccess dataAccess;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private ListView commentListView;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Slidr.attach(this);

        notificationManagerCompat = NotificationManagerCompat.from(this);

        commentListView = findViewById(R.id.comList);
        GetExtras();
        dataAccess = DataAccessFactory.getInstance();
        dataAccess.getComments(topicId, new IDataAccess.IOnResult<List<Comment>>() {
            @Override
            public void onResult(List<Comment> comments) {
                commentList = comments;
                commentAdapter = new CommentAdapter(CommentActivity.this, R.layout.comment_cell, comments);
                commentListView.setAdapter(commentAdapter);
                sendNotification(commentListView);
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
    }

    public void  sendNotification(View v) {

        Intent activityIntent = new Intent(this, CommentActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, Notifications.channelId1)
                .setSmallIcon(R.drawable.ic_comment)
                .setContentTitle("New Comment")
                // .setContentText()
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .build();
        notificationManagerCompat.notify(1, notification);
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
