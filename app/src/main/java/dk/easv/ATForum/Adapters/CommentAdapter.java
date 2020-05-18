package dk.easv.ATForum.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Models.Comment;
import dk.easv.ATForum.Posts.EditCommentActivity;
import dk.easv.ATForum.Posts.EditTopicActivity;


public class CommentAdapter extends ArrayAdapter<Comment> {
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> comments) {
        super(context, resource, comments);
        commentList = comments;
        this.context = context;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());

            view = inflater.inflate(R.layout.comment_cell, parent, false);

        } else {
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        final Comment comment = commentList.get(position);
        TextView txtAuthorUsername = view.findViewById(R.id.tvUsername);
        
        TextView txtMessage = view.findViewById(R.id.tvCommentMessage);
        txtMessage.setText(comment.getMessage());
        TextView txtTimestamp = view.findViewById(R.id.tvTimestamp);
        if (comment.getTimestamp() != null) {
            txtTimestamp.setText(comment.getTimestamp().toString());
        }
        Button editButton = view.findViewById(R.id.btnSubmit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCommentActivity.class);
                intent.putExtra("comment", comment);
                context.startActivity(intent);
            }
        });
        editButton.setFocusable(false);

        return view;
    }
}
