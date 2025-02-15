package com.example.testauth.ui.mailditails;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.testauth.Models.IngredientDtoView;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.R;
import com.example.testauth.Repository.RepositoryImpl;
import com.example.testauth.Repository.datasources.MealLocalDataSourceImpl;
import com.example.testauth.Repository.datasources.MealRemoteDataSourceImpl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MealDetails extends Fragment implements IMealDetailsView {
    public MealDetails() {
    }

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    TextView mealName;
    ImageView mealImage;
    TextView mealDescription;
    TextView location;
    ImageButton favoriteBtn;
    MealDetailsPresentor mealDetailsPresentor;
    MealDto GlobalMealDto;

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
    }

    List<MealDto> GloblaMealList = new ArrayList<>();

    {
        MealDto meal = new MealDto();
        meal.setIdMeal("52968");
        meal.setStrMeal("Mbuzi Choma (Roasted Goat)");
        meal.setStrCategory("Goat");
        meal.setStrArea("Kenyan");
        meal.setStrInstructions("Roast meat over medium heat for 50 minutes...");
        meal.setStrIngredient1("Goat Meat");
        meal.setStrIngredient2("Corn Flour");
        meal.setStrIngredient3("Tomatoes");
        meal.setStrIngredient4("Salt");
        meal.setStrIngredient5("Onion");
        meal.setStrIngredient6("Green Chilli");
        meal.setStrIngredient7("Coriander Leaves");
        meal.setStrMeasure1("1 kg");
        meal.setStrMeasure2("1 kg");
        meal.setStrMeasure3("2");
        meal.setStrMeasure4("Pinch");
        meal.setStrMeasure5("1");
        meal.setStrMeasure6("1");
        meal.setStrMeasure7("1 bunch");
        GloblaMealList.add(meal);

    }

    private static final String TAG = "MealDetails";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_details, container, false);
        myAdapter = new MyAdapter(getContext(), GloblaMealList.get(0).getIngredientDtos());
        recyclerView = view.findViewById(R.id.recyclerViewIngredients);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mealDescription = view.findViewById(R.id.descriptionEdite_text);
        mealImage = view.findViewById(R.id.MealImageDetails);
        mealName = view.findViewById(R.id.mealNameDetails);
        location = view.findViewById(R.id.varLocation);
        favoriteBtn = view.findViewById(R.id.FavoriteBtn);
        mealDetailsPresentor = new MealDetailsPresentor(this, RepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(getContext())));

        WebView webView = view.findViewById(R.id.videoView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

// Replace 'VIDEO_ID' with the actual YouTube video ID
        String videoId = "v=rp8Slv4INLk";
        String html = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"
                + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        webView.loadData(html, "text/html", "utf-8");


//        mealDetailsPresentor.getMealsFromFireBase().addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<MealDto> mealsList = new ArrayList<>();
//
//                for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
//                    MealDto meal = mealSnapshot.getValue(MealDto.class);
//                    if (meal != null) {
//                        mealsList.add(meal);
//                        mealDetailsPresentor.insertMeal(meal);
//                        Snackbar.make(view,"Added to meals ", Snackbar.LENGTH_LONG).show();
//                        Log.d(TAG, "Meal Found: " + meal.getIdMeal() );
//                    }
//                }
//
//                Log.d(TAG, "Total Calendar Entries Found: " + mealsList.size());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//            }
//        });


        MaterialButton addToCalenderBtn = view.findViewById(R.id.addToCalenderBtn);
        addToCalenderBtn.setOnClickListener(v -> {
            showCalender();
        });

        // Safe Args
        GlobalMealDto = MealDetailsArgs.fromBundle(getArguments()).getMealto();
        mealDescription.setText(GlobalMealDto.getStrInstructions());
        mealName.setText(GlobalMealDto.getStrMeal());
        location.setText(GlobalMealDto.getStrArea());
        myAdapter = new MyAdapter(getContext(), GlobalMealDto.getIngredientDtos());
        Glide.with(this).load(GlobalMealDto.getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(mealImage);

        favoriteBtn.setOnClickListener(v -> {
            GlobalMealDto.setFavorite(true);
            mealDetailsPresentor.insertMeal(GlobalMealDto);
            mealDetailsPresentor.insertMealToFireBase(GlobalMealDto);
        });
    }

    void showCalender() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
        datePicker.show(this.getParentFragmentManager(), "DATE_PICKER");
        // Slution for Just now you have to make it pro
        mealDetailsPresentor.insertMeal(GlobalMealDto);
        Log.i(TAG, "showCalender:  " + GlobalMealDto.getIdMeal());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            MealsCalenderDto mealCalenderDto = new MealsCalenderDto(GlobalMealDto.getIdMeal(), datePicker.getHeaderText());
            mealDetailsPresentor.insertToCalender(mealCalenderDto);
            mealDetailsPresentor.insertClendertoFireBase(mealCalenderDto);

        });
    }


    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private final Context context;
        private List<IngredientDtoView> ingredientDtoViews;

        public MyAdapter(Context _context, List<IngredientDtoView> ingredients) {
            super();
            context = _context;
            ingredientDtoViews = ingredients;
            Log.i(TAG, "MyAdapter: " + ingredients.size());
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.ingredient_card, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.ingrdientNameTxt.setText(ingredientDtoViews.get(position).getIngredient());
            holder.measureIngrdientTxt.setText(ingredientDtoViews.get(position).getMeasure());
            //Glide.with(context).load(ingredientDtos.get(position).getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
        }

        @Override
        public int getItemCount() {
            return ingredientDtoViews.size();
        }

        public void notifyItemChanged(List<IngredientDtoView> MealDtos) {
            this.ingredientDtoViews = MealDtos;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ConstraintLayout constraintLayout;
            View layout;
            TextView ingrdientNameTxt, measureIngrdientTxt;
            ImageView image;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView;
                ingrdientNameTxt = layout.findViewById(R.id.ingredientsNameEditeCard);
                image = layout.findViewById(R.id.ingredientsCardImage);
                measureIngrdientTxt = layout.findViewById(R.id.ingredientsMeasureCard);
            }
        }

    }


}