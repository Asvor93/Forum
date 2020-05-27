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

import com.example.forum.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;


public class UserAdapter extends ArrayAdapter<User> {
    // A list of users
    private List<User> userList;

    // A list of roles
    private List<Role> roleList;

    // The data access interface that is used to carry out actions on the database
    private IDataAccess dataAccess;

    // The role of the current user
    private Role userRole;

    public UserAdapter(Context context, int resource, List<User> userList, List<Role> roleList,
                       Role role) {
        super(context, resource, userList);
        this.userList = userList;
        this.roleList = roleList;
        this.userRole = role;
        dataAccess = DataAccessFactory.getInstance();
    }

    // Gets a view based on the layout that is inflated and displays the users
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        UserViewHolder holder;
        if (view == null) {
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.cell, null);
            holder = new UserViewHolder();
            holder.btnEditRole = view.findViewById(R.id.btnEditRole);
            holder.imgUserPic = view.findViewById(R.id.imgUserPicture);
            holder.txtEmail = view.findViewById(R.id.tvUserEmail);
            holder.txtUsername = view.findViewById(R.id.tvUsername);
            view.setTag(holder);
        } else {
            holder = (UserViewHolder) view.getTag();
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        final User user = userList.get(position);
        final Role role = roleList.get(position);
        final String uid = user.getUid();

        holder.btnEditRole.setFocusable(false);
        // Conditions for the buttons related to admin actions
        if (userRole.getRoleName().equals("superAdmin")) {
            if (role.getRoleName().equals("admin")) {
                holder.btnEditRole.setEnabled(true);
                holder.btnEditRole.setVisibility(View.VISIBLE);
                holder.btnEditRole.setText(R.string.demote);
                holder.btnEditRole.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> newRole = new HashMap<>();
                        newRole.put("roleName", "user");
                        dataAccess.createRole(newRole, uid, new IDataAccess.IOnResult<Role>() {
                            @Override
                            public void onResult(Role result) {
                                role.setRoleName(result.getRoleName());
                            }
                        });
                    }
                });
            } else if (role.getRoleName().equals("user")) {
                holder.btnEditRole.setEnabled(true);
                holder.btnEditRole.setVisibility(View.VISIBLE);
                holder.btnEditRole.setText(R.string.promote);
                holder.btnEditRole.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> newRole = new HashMap<>();
                        newRole.put("roleName", "admin");
                        dataAccess.createRole(newRole, uid, new IDataAccess.IOnResult<Role>() {
                            @Override
                            public void onResult(Role result) {
                                role.setRoleName(result.getRoleName());
                            }
                        });
                    }
                });
            }
        }


        holder.txtUsername.setText("Username: " + user.getUsername());

        holder.txtEmail.setText("Email: " + user.getEmail());

        Picasso.get()
                .load(user.getPhotoURL())
                .resize(150, 180)
                .into(holder.imgUserPic);

        return view;
    }

    // Used to store each view for recycling
    static class UserViewHolder {
        TextView txtUsername;
        TextView txtEmail;
        ImageView imgUserPic;
        Button btnEditRole;
    }
}
