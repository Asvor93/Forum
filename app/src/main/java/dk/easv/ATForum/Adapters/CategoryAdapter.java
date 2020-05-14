package dk.easv.ATForum.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Models.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private List<Category> categoryList;

    public CategoryAdapter(Context context, int resource, List<Category> categoryList) {
        super(context, resource, categoryList);
        this.categoryList = categoryList;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());

            view = inflater.inflate(R.layout.category_cell, parent, false);

        } else {
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        final Category cat = categoryList.get(position);
        TextView txtCatName = view.findViewById(R.id.tvCategoryName);
        txtCatName.setText("Category Name: " + cat.getCategoryName());
        TextView txtCatDescription = view.findViewById(R.id.tvCatDescription);
        txtCatDescription.setText("Description: " + cat.getDescription());
        Button editButton = view.findViewById(R.id.submitEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("category", getPosition(cat));
            }
        });
        return view;
    }
}
