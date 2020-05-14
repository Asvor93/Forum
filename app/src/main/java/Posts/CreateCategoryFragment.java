package Posts;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.forum.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import dk.easv.ATForum.DataAccessFactory;
import dk.easv.ATForum.Interfaces.IDataAccess;
import dk.easv.ATForum.Models.Category;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateCategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    IDataAccess dataAccess;
    EditText txtCatName, txtCatDescription;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateCategoryFragment newInstance(String param1, String param2) {
        CreateCategoryFragment fragment = new CreateCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dataAccess = DataAccessFactory.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        txtCatName = view.findViewById(R.id.etNewCatName);
        txtCatDescription = view.findViewById(R.id.etNewCatDescription);

        Button btnSubmit = view.findViewById(R.id.submitNewCat);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategory();
            }
        });
    }

    private void createCategory() {
        final String catNameString = txtCatName.getText().toString();
        final String catDescriptionString = txtCatDescription.getText().toString();

        Map<String, Object> category = new HashMap<>();
        category.put("categoryName", catNameString);
        category.put("description", catDescriptionString);

        dataAccess.createCategory(category, new IDataAccess.IONCategoryResult() {
            @Override
            public void onResult(Category category) {

            }

        });

    }
}
