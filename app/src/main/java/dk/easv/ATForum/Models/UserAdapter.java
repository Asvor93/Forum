package dk.easv.ATForum.Models;

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


public class UserAdapter extends ArrayAdapter<User> {

    private List<User> userList;
    private List<Role> roleList;
    private IDataAccess dataAccess;
    private Role userRole;

    public UserAdapter(Context context, int resource, List<User> userList, List<Role> roleList,
                       Role role) {
        super(context, resource, userList);
        this.userList = userList;
        this.roleList = roleList;
        this.userRole = role;
        dataAccess = DataAccessFactory.getInstance();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.cell, null);
            holder = new ViewHolder();
            holder.btnEditRole = view.findViewById(R.id.btnEditRole);
            holder.imgUserPic = view.findViewById(R.id.imgUserPicture);
            holder.txtEmail = view.findViewById(R.id.tvUserEmail);
            holder.txtUsername = view.findViewById(R.id.tvUsername);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        final User user = userList.get(position);
        final Role role = roleList.get(position);
        final String uid = user.getUid();

        holder.btnEditRole.setFocusable(false);

        if (userRole.getRoleName().equals("superAdmin")) {
            if (role.getRoleName().equals("admin")) {
                holder.btnEditRole.setEnabled(true);
                holder.btnEditRole.setVisibility(View.VISIBLE);
                holder.btnEditRole.setText(R.string.demote);
                holder.btnEditRole.setOnClickListener(new View.OnClickListener() {
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
                holder.btnEditRole.setEnabled(true);
                holder.btnEditRole.setVisibility(View.VISIBLE);
                holder.btnEditRole.setText(R.string.promote);
                holder.btnEditRole.setOnClickListener(new View.OnClickListener() {
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
        }


        holder.txtUsername.setText("Username: " + user.getUsername());

        holder.txtEmail.setText("Email: " + user.getEmail());

        Picasso.get()
                .load(user.getPhotoURL())
                .resize(150, 180)
                .into(holder.imgUserPic);

        return view;
    }

    static class ViewHolder {
        TextView txtUsername;
        TextView txtEmail;
        ImageView imgUserPic;
        Button btnEditRole;
    }
}
