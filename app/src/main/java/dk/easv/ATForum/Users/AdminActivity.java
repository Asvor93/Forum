package dk.easv.ATForum.Users;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import java.util.List;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Adapters.UserAdapter;

public class AdminActivity extends MenuActivity {

    private static final String TAG = "XYZ";
    UserAdapter userAdapter;
    List<Role> roleList;
    List<User> userList;
    ListView userListView;

    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Slidr.attach(this);

        userListView = findViewById(R.id.listUsers);

        //get All users
        dataAccess = DataAccessFactory.getInstance();
        dataAccess.getAllUsers(currentUser.getUid(), new IDataAccess.IOnResult<List<User>>() {
            @Override
            public void onResult(List<User> users) {
                userList = users;

                //get All roles
                dataAccess.getAllRoles(new IDataAccess.IOnResult<List<Role>>() {
                    @Override
                    public void onResult(List<Role> roles) {
                        roleList = roles;
                        userAdapter = new UserAdapter(AdminActivity.this, R.layout.cell, userList, roleList, role);
                        userListView.setAdapter(userAdapter);
                    }
                });
            }
        });

        userListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                userListView.setLongClickable(true);
                User user = userList.get(position);
                String uid = user.getUid();
                if (!role.getRoleName().equals("user")) {
                    if (roleList.get(position).getRoleName().equals("user") ||
                            role.getRoleName().equals("superAdmin")) {
                        dataAccess.deleteUser(uid);
                        Toast.makeText(AdminActivity.this, "Deleted user with id: " + uid, Toast.LENGTH_SHORT).show();
                    }
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        adminMenuItem.setEnabled(false);
        adminMenuItem.setVisible(false);

        return true;
    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }
}