package dk.easv.ATForum.Posts;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forum.R;
import com.r0adkll.slidr.Slidr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import dk.easv.ATForum.Adapters.CategoryAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.MenuActivity;
import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.Topic;
import dk.easv.ATForum.Models.User;

public class CategoryActivity extends MenuActivity {
    private static final String TAG = "XYZ";
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private ListView categoryListView;
    private Role role;
    private User currentUser;

    private IDataAccess dataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Slidr.attach(this);

        categoryListView = findViewById(R.id.catList);

        role = (Role) getIntent().getSerializableExtra("role");
        Log.d(TAG, "onCreateView: " + role);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        dataAccess = DataAccessFactory.getInstance();

        dataAccess.getAllCategories(new IDataAccess.IOnResult<List<Category>>() {
            @Override
            public void onResult(List<Category> categories) {
                categoryList = categories;
                categoryAdapter = new CategoryAdapter(CategoryActivity.this, R.layout.category_cell, categoryList, currentUser);
                categoryListView.setAdapter(categoryAdapter);
            }
        });

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String catUid = categoryList.get(position).getUid();
                Intent topicIntent = new Intent(CategoryActivity.this, TopicActivity.class);
                topicIntent.putExtra("catId", catUid);
                startActivity(topicIntent);
            }
        });

        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                categoryListView.setLongClickable(true);
                Category category = categoryList.get(position);
                String uid = category.getUid();
                if (!role.getRoleName().equals("user")) {
                    dataAccess.deleteCategory(uid);
                    Toast.makeText(CategoryActivity.this, "Deleted category with id: " + uid, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        categoryMenuItem.setVisible(false);
        categoryMenuItem.setEnabled(false);
        return true;
    }
}
