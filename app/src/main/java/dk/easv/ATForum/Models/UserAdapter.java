package dk.easv.ATForum.Models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forum.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;


public class UserAdapter extends ArrayAdapter<User> {

    private List<User> userList;
    private List<Role> roleList;
    private IDataAccess dataAccess;

    public UserAdapter(Context context, int resource, List<User> userList, List<Role> roleList) {
        super(context, resource, userList);
        this.userList = userList;
        this.roleList = roleList;
        dataAccess = DataAccessFactory.getInstance();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.cell, null);

        } else {
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        User user = userList.get(position);
        Role role = roleList.get(position);
        final String uid = user.getUid();
        Button btnEditRole = view.findViewById(R.id.btnEditRole);
        btnEditRole.setFocusable(false);
        if (role.getRoleName().equals("admin")) {
            btnEditRole.setEnabled(true);
            btnEditRole.setVisibility(View.VISIBLE);
            btnEditRole.setText(R.string.demote);
            btnEditRole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> role = new HashMap<>();
                    role.put("roleName", "user");
                    dataAccess.createRole(role, uid, new IDataAccess.IONRoleResult() {
                        @Override
                        public void onResult(Role role) {
                            role.setRoleName(role.getRoleName());
                        }
                    });
                }
            });
        } else if (role.getRoleName().equals("user")) {
            btnEditRole.setEnabled(true);
            btnEditRole.setVisibility(View.VISIBLE);
            btnEditRole.setText(R.string.promote);
            btnEditRole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> role = new HashMap<>();
                    role.put("roleName", "admin");
                    dataAccess.createRole(role, uid, new IDataAccess.IONRoleResult() {
                        @Override
                        public void onResult(Role role) {
                            role.setRoleName(role.getRoleName());
                        }
                    });
                }
            });
        }
        TextView txtUsername = view.findViewById(R.id.tvUsername);
        txtUsername.setText("Username: " + user.getUsername());
        TextView txtEmail = view.findViewById(R.id.tvUserEmail);
        txtEmail.setText("Email: " + user.getEmail());
        ImageView imgUserPic = view.findViewById(R.id.imgUserPicture);
        Picasso.get()
                .load(user.getPhotoURL())
                .resize(150, 180)
                .into(imgUserPic);

        return view;
    }
}
