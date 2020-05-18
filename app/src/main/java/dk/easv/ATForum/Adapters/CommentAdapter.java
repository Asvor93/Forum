package dk.easv.ATForum.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.example.forum.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import dk.easv.ATForum.Models.Comment;
import dk.easv.ATForum.Models.User;


public class CommentAdapter extends ArrayAdapter<Comment> {
    private List<Comment> commentList;
    private Context context;
    private User commentUser;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> comments) {
        super(context, resource, comments);
        commentList = comments;
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
        commentUser = comment.getAuthor();
        TextView txtAuthorUsername = view.findViewById(R.id.tvUsername);
        ImageView userPicture = view.findViewById(R.id.ivUserPicture);
        TextView txtMessage = view.findViewById(R.id.tvCommentMessage);
        TextView txtTimestamp = view.findViewById(R.id.tvTimestamp);
        Button editButton = view.findViewById(R.id.btnSubmit);

        txtMessage.setText(comment.getMessage());
        Picasso.get().load(commentUser.getPhotoURL()).resize(150,150).into(userPicture);
        txtAuthorUsername.setText(commentUser.getUsername());
        if (comment.getTimestamp() != null) {
            txtTimestamp.setText(comment.getTimestamp().toString());
        }

        editButton.setFocusable(false);

        return view;
    }
}
