package dk.easv.ATForum.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Models.Topic;

public class FavoriteTopicAdapter extends ArrayAdapter<Topic> {
    // Tag used for logging
    private static final String TAG = "XYZ";

    // List containing the favorite topics
    private List<Topic> favoriteTopicList;

    public FavoriteTopicAdapter(@NonNull Context context, int resource, @NonNull List<Topic> favoriteTopics) {
        super(context, resource, favoriteTopics);
        favoriteTopicList = favoriteTopics;
    }

    // Gets a view based on the layout that is inflated and displays the favorite topics
    @NonNull
    @Override
    public View getView(final int position, @Nullable View view, @NonNull ViewGroup parent) {
        FavoritesViewHolder holder;
        if (view == null) {
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.favorite_topic_cell, null);
            holder = new FavoritesViewHolder();
            holder.txtAuthorName = view.findViewById(R.id.tvAuthorName);
            holder.txtTopicDesc = view.findViewById(R.id.tvTopicDescription);
            holder.txtTopicName = view.findViewById(R.id.tvTopicName);
            view.setTag(holder);
        } else {
            holder = (FavoritesViewHolder) view.getTag();
            Log.d(TAG, "Position: " + position + " View recycled");
        }

        final Topic topic = favoriteTopicList.get(position);
        holder.txtAuthorName.setText(topic.getAuthor().getUsername());
        holder.txtTopicDesc.setText(topic.getDescription());
        holder.txtTopicName.setText(topic.getTopicName());


        return view;
    }

    // Used to store each view for recycling
    static class FavoritesViewHolder {
        TextView txtAuthorName;
        TextView txtTopicDesc;
        TextView txtTopicName;
    }
}
