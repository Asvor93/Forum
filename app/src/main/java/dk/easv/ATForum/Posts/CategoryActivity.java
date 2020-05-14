package dk.easv.ATForum.Posts;

import androidx.appcompat.app.AppCompatActivity;

import com.example.forum.R;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import dk.easv.ATForum.Adapters.CategoryAdapter;
import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;

public class CategoryActivity extends AppCompatActivity {
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

        categoryListView = findViewById(R.id.catList);

        role = (Role) getIntent().getSerializableExtra("role");
        Log.d(TAG, "onCreateView: " + role);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        dataAccess = DataAccessFactory.getInstance();

        dataAccess.getAllCategories(new IDataAccess.IONCategoriesResult() {
            @Override
            public void onResult(List<Category> categories) {
                categoryList = categories;
                categoryAdapter = new CategoryAdapter(CategoryActivity.this, R.layout.category_cell, categoryList);
                categoryListView.setAdapter(categoryAdapter);
                Log.d(TAG, "dk.easv.ATForum.Users: " + categoryList.get(0));
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
}
