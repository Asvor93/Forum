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
import dk.easv.ATForum.Models.Role;
import dk.easv.ATForum.Models.User;
import dk.easv.ATForum.Posts.EditCategoryActivity;

public class CategoryAdapter extends ArrayAdapter<Category> {
    // The list containing the categories
    private List<Category> categoryList;

    // The context which instantiated the adapter, used for starting the EditCategory activity
    private Context context;

    private User currentUser;

    private Role role;

    public CategoryAdapter(Context context, int resource, List<Category> categoryList, User currentUser, Role role) {
        super(context, resource, categoryList);
        this.categoryList = categoryList;
        this.context = context;
        this.currentUser = currentUser;
        this.role = role;
    }

    // Gets a view based on the layout that is inflated and displays the categories
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        CategoryViewHolder holder;
        if (view == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.category_cell, parent, false);
            holder = new CategoryViewHolder();
            holder.editButton = view.findViewById(R.id.goToEditCategory);
            holder.txtCatName = view.findViewById(R.id.tvCategoryName);
            holder.txtCatDescription = view.findViewById(R.id.tvCatDescription);
            view.setTag(holder);

        } else {
            holder = (CategoryViewHolder) view.getTag();
            Log.d("XYZ", "Position: " + position + " View recycled");
        }

        final Category cat = categoryList.get(position);
        holder.txtCatName.setText("Category Name: " + cat.getCategoryName());
        holder.txtCatDescription.setText("Description: " + cat.getDescription());

        // Click event that is placed on each view
        holder.editButton.setFocusable(false);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCategoryActivity.class);
                intent.putExtra("category", cat);
                context.startActivity(intent);
            }
        });

        holder.editButton.setVisibility(View.GONE);

        if (!role.getRoleName().equals("user")) {
            holder.editButton.setVisibility(View.VISIBLE);
        }

        return view;
    }

    // Used to store each view for recycling
    static class CategoryViewHolder {
        TextView txtCatName;
        TextView txtCatDescription;
        Button editButton;
    }
}
