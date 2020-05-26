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
import androidx.annotation.Nullable;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Topic;
import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Posts.EditTopicActivity;

public class TopicAdapter extends ArrayAdapter<Topic> {
    private static final String TAG = "XYZ";
    private List<Topic> topicList;
    private Context context;
    private IDataAccess dataAccess;
    private User currentUser;

    public TopicAdapter(@NonNull Context context, int resource, @NonNull List<Topic> topics, User currentUser) {
        super(context, resource, topics);
        topicList = topics;
        this.context = context;
        dataAccess = DataAccessFactory.getInstance();
        this.currentUser = currentUser;
    }

    // Gets a view based on the layout that is inflated and displays the topics
    @NonNull
    @Override
    public View getView(final int position, @Nullable View view, @NonNull ViewGroup parent) {
        TopicViewHolder holder;
        if (view == null) {
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.topic_cell, null);
            holder = new TopicViewHolder();
            holder.txtAuthorName = view.findViewById(R.id.tvAuthorName);
            holder.txtTopicDesc = view.findViewById(R.id.tvTopicDescription);
            holder.txtTopicName = view.findViewById(R.id.tvTopicName);
            view.setTag(holder);
        } else {
            holder = (TopicViewHolder) view.getTag();
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        final Topic topic = topicList.get(position);
        holder.txtAuthorName.setText(topic.getAuthor().getUsername());
        holder.txtTopicDesc.setText(topic.getDescription());
        holder.txtTopicName.setText(topic.getTopicName());

        holder.editButton = view.findViewById(R.id.goToEditTopic);
        holder.editButton.setFocusable(false);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditTopicActivity.class);
                intent.putExtra("topic", topic);
                context.startActivity(intent);
            }
        });
        holder.btnAddToFavorites = view.findViewById(R.id.addFavoriteTopic);
        holder.btnAddToFavorites.setFocusable(false);
        holder.btnAddToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataAccess.addFavoriteTopic(currentUser.getUid(), topic);
            }
        });

        return view;
    }

    // Used to store each view for recycling
    static class TopicViewHolder {
        TextView txtAuthorName;
        TextView txtTopicDesc;
        TextView txtTopicName;
        Button editButton;
        Button btnAddToFavorites;
    }
}
