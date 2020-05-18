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
import androidx.annotation.Nullable;

import com.example.forum.R;

import java.util.List;
;
import dk.easv.ATForum.Models.Topic;

public class TopicAdapter extends ArrayAdapter<Topic> {
    private List<Topic> topicList;

    public TopicAdapter(@NonNull Context context, int resource, @NonNull List<Topic> topics) {
        super(context, resource, topics);
        topicList = topics;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ViewHolder holder;;
        if (view == null) {
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.topic_cell, null);
            holder = new ViewHolder();
            holder.txtAuthorName = view.findViewById(R.id.tvAuthorName);
            holder.txtTopicDesc = view.findViewById(R.id.tvTopicDescription);
            holder.txtTopicName = view.findViewById(R.id.tvTopicName);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        Topic topic = topicList.get(position);
        holder.txtAuthorName.setText(topic.getAuthor().getUsername());
        holder.txtTopicDesc.setText(topic.getDescription());
        holder.txtTopicName.setText(topic.getTopicName());
        return view;
    }

    static class ViewHolder {
        TextView txtAuthorName;
        TextView txtTopicDesc;
        TextView txtTopicName;
    }
}
