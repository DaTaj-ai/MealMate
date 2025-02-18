package com.example.testauth.ui.mailditails;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testauth.helper.CountryFlag;
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

import java.util.List;

import androidx.activity.OnBackPressedCallback;

public class MealDetails extends Fragment implements IMealDetailsView {
    public MealDetails() {
    }

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    TextView mealName;
    ImageView mealImage , flagImage;
    TextView mealDescription;
    TextView location;
    ImageButton favoriteBtn;
    MealDetailsPresentor mealDetailsPresentor;
    MealDto GlobalMealDto;

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private static final String TAG = "MealDetails";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(requireView());
                Navigation.findNavController(requireView()).popBackStack();
                //navController.navigate(R.id.homeContentFragment);
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_details, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mealDescription = view.findViewById(R.id.descriptionEdite_text);
        mealImage = view.findViewById(R.id.MealImageDetails);
        mealName = view.findViewById(R.id.mealNameDetails);
        location = view.findViewById(R.id.varLocation);
        flagImage = view.findViewById(R.id.flagImageMealDetails);
        favoriteBtn = view.findViewById(R.id.FavoriteBtn);
        mealDetailsPresentor = new MealDetailsPresentor(this, RepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(getContext())));



        MaterialButton addToCalenderBtn = view.findViewById(R.id.addToCalenderBtn);
        addToCalenderBtn.setOnClickListener(v -> {
            showCalender();
        });

        // Safe Args
        GlobalMealDto = MealDetailsArgs.fromBundle(getArguments()).getMealto();
        mealDescription.setText(GlobalMealDto.getStrInstructions());
        mealName.setText(GlobalMealDto.getStrMeal());
        location.setText(GlobalMealDto.getStrArea());

        myAdapter = new MyAdapter(getContext(), MealDetailsArgs.fromBundle(getArguments()).getMealto().getIngredientDtos());
        recyclerView = view.findViewById(R.id.recyclerViewIngredients);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);


        Glide.with(this).load(GlobalMealDto.getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(mealImage);
        Glide.with(this).load(CountryFlag.getFlagUrl(GlobalMealDto.getStrArea())).placeholder(R.drawable.ic_launcher_foreground).into(flagImage);

        myAdapter.notifyItemChanged(MealDetailsArgs.fromBundle(getArguments()).getMealto().getIngredientDtos());
        myAdapter.notifyDataSetChanged();

        WebView webView = view.findViewById(R.id.videoView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String videoId;

        if(GlobalMealDto.getStrYoutube() != null){
            videoId = getYouTubeVideoId(GlobalMealDto.getStrYoutube());
        }
        else {
            videoId = "";
        }

        String html = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"
                + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        webView.loadData(html, "text/html", "utf-8");

        favoriteBtn.setOnClickListener(v -> {
            GlobalMealDto.setFavorite(true);
            mealDetailsPresentor.insertMeal(GlobalMealDto).doOnComplete(()->showSnackBar("Meal Added to Favorite Successfully")).subscribe();
            mealDetailsPresentor.insertMealToFireBase(GlobalMealDto);
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        Boolean isQuest = sharedPreferences.getBoolean("isQuest", false);

        if (isQuest)  {
            favoriteBtn.setEnabled(false);
            Snackbar.make(view, "welcome to Guest Mode \n Login to get full access", Snackbar.LENGTH_LONG).show();
        }
    }
    String getYouTubeVideoId(String url) {
        String videoId = "";
        videoId = url.substring(url.indexOf("=") + 1);
        return videoId;
    };

    void showCalender() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();
        datePicker.show(this.getParentFragmentManager(), "DATE_PICKER");



        Log.i(TAG, "showCalender:  " + GlobalMealDto.getIdMeal());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            MealsCalenderDto mealCalenderDto = new MealsCalenderDto(GlobalMealDto.getIdMeal(), datePicker.getHeaderText());
            mealDetailsPresentor.insertMeal(GlobalMealDto).doOnError(e->showSnackBar(e.getMessage())).doOnComplete(()->{mealDetailsPresentor.insertToCalender(mealCalenderDto);}).subscribe();
            mealDetailsPresentor.insertMealToFireBase(GlobalMealDto);
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
            Glide.with(context).load("https://www.themealdb.com/images/ingredients/"+ ingredientDtoViews.get(position).getIngredient()+"-Small.png").placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
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