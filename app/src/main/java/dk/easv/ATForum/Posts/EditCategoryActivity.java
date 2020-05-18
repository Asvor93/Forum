package dk.easv.ATForum.Posts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.forum.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Category;
import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Users.ProfileActivity;

public class EditCategoryActivity extends AppCompatActivity {
    IDataAccess dataAccess;
    EditText txtCatName, txtCatDescription;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        dataAccess = DataAccessFactory.getInstance();

        txtCatName = findViewById(R.id.etEditCatName);
        txtCatDescription = findViewById(R.id.etEditCatDescription);

        setGUI();

        Button btnSubmit = findViewById(R.id.submitEditCat);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCategory();
            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void editCategory() {
        final String catNameString = txtCatName.getText().toString();
        final String catDescriptionString = txtCatDescription.getText().toString();

        Map<String, Object> mapCategory = new HashMap<>();
        mapCategory.put("categoryName", catNameString);
        mapCategory.put("description", catDescriptionString);

        final String id = category.getUid();

        dataAccess.editCategory(mapCategory, id, new IDataAccess.IOnResult<Category>() {
            @Override
            public void onResult(Category category) {
                Toast.makeText(EditCategoryActivity.this, "Category successfully updated", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }

    private void setGUI() {
        category = (Category) getIntent().getSerializableExtra("category");

        txtCatName.setText(category.getCategoryName());
        txtCatDescription.setText(category.getDescription());
    }
}
