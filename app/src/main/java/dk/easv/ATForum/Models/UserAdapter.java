package dk.easv.ATForum.Models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forum.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserAdapter extends ArrayAdapter<User> {

    private List<User> userList;

    public UserAdapter(Context context, int resource, List<User> userList) {
        super(context, resource, userList);
        this.userList = userList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.cell,null);
        }else {
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        User user = userList.get(position);
        TextView txtUser = view.findViewById(R.id.tvUser);
        txtUser.setText("Username: " + user.getUsername() + "Email: " + user.getEmail());
        ImageView imgUserPic = view.findViewById(R.id.imgUserPicture);
        Picasso.get().load(user.getPhotoURL()).into(imgUserPic);

        return view;
    }
}
