package dk.easv.ATForum.Adapters;

import android.content.Context;
import android.content.Intent;
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
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Posts.EditCommentActivity;
import dk.easv.ATForum.Posts.EditTopicActivity;


public class CommentAdapter extends ArrayAdapter<Comment> {
    // The list containing the comments that is sent with the constructor
    private List<Comment> commentList;

    // The context which instantiated the adapter, used for starting the EditComment activity
    private Context context;

    private User currentUser;

    private Role role;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> comments, User currentUser, Role role) {
        super(context, resource, comments);
        commentList = comments;
        this.context = context;
        this.currentUser = currentUser;
        this.role = role;
    }

    // Gets a view based on the layout that is inflated and displays the relevant comments
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        CommentViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.comment_cell, parent, false);
            holder = new CommentViewHolder();
            holder.editButton = view.findViewById(R.id.btnSubmit);
            holder.txtTimestamp = view.findViewById(R.id.tvTimestamp);
            holder.txtMessage = view.findViewById(R.id.tvCommentMessage);
            holder.userPicture = view.findViewById(R.id.ivUserPicture);
            holder.txtAuthorUsername = view.findViewById(R.id.tvUsername);
            view.setTag(holder);
        } else {
            holder = (CommentViewHolder) view.getTag();
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        final Comment comment = commentList.get(position);
        User commentUser = comment.getAuthor();

        holder.txtMessage.setText(comment.getMessage());
        Picasso.get().load(commentUser.getPhotoURL()).resize(150, 150).into(holder.userPicture);
        holder.txtAuthorUsername.setText(commentUser.getUsername());
        if (comment.getTimestamp() != null) {
            holder.txtTimestamp.setText(comment.getTimestamp().toString());
        }

        // Click event that is placed on each view
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCommentActivity.class);
                intent.putExtra("comment", comment);
                context.startActivity(intent);
            }
        });
        holder.editButton.setFocusable(false);

        if (currentUser == null) {
            holder.editButton.setVisibility(View.GONE);
        }
        return view;
    }

    // Used to store each view for recycling
    static class CommentViewHolder {
        TextView txtMessage;
        TextView txtAuthorUsername;
        TextView txtTimestamp;
        Button editButton;
        ImageView userPicture;
    }
}
