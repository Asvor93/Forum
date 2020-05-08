package dk.easv.ATForum;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forum.R;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Models.UserAdapter;

public class AdminActivity extends AppCompatActivity {

    UserAdapter userAdapter;

    List<User> userList;

    ListView userListView;

    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        userListView = findViewById(R.id.listUsers);
        //get All users
        dataAccess = DataAccessFactory.getInstance();
        dataAccess.getAllUsers(new IDataAccess.IONUsersResult() {
            @Override
            public void onResult(List<User> users) {
                userList = users;
                userAdapter = new UserAdapter(AdminActivity.this, R.layout.cell, userList);
                userListView.setAdapter(userAdapter);
            }
        });
    }
}
