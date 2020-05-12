package dk.easv.ATForum.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.forum.R;

import java.util.List;

import dk.easv.ATForum.Interfaces.IDataAccess;
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
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.category_Cell, null);

        } else {
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        Category cat = categoryList.get(position);
        TextView txtCatName = view.findViewById(R.id.tvCategoryName);
        txtCatName.setText("Username: " + cat.getCategoryName());
        TextView txtCatDescription = view.findViewById(R.id.tvCatDescription);
        txtCatDescription.setText("Email: " + cat.getDescription());

        return view;
    }
}
