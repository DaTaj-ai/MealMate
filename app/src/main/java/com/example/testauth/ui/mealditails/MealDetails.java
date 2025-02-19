package com.example.testauth.ui.mealditails;

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
import com.example.testauth.Models.helper.CountryFlag;
import com.example.testauth.Models.IngredientDtoView;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.R;
import com.example.testauth.Repository.RepositoryImpl;
import com.example.testauth.Repository.datasources.MealLocalDataSourceImpl;
import com.example.testauth.Repository.datasources.MealRemoteDataSourceImpl;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.activity.OnBackPressedCallback;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetails extends Fragment implements IMealDetailsView {
    public MealDetails() {
    }

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    TextView mealName;
    ImageView mealImage, flagImage;
    TextView mealDescription;
    TextView location;
    ImageButton favoriteBtn;
    MealDetailsPresentor mealDetailsPresentor;
    MealDto globalMealDto;

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(this.getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMeal(MealDto mealDto) {
        mealDescription.setText(mealDto.getStrInstructions());
        mealName.setText(mealDto.getStrMeal());
        location.setText(mealDto.getStrArea());
        Glide.with(this).load(mealDto.getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(mealImage);
        Glide.with(this).load(CountryFlag.getFlagUrl(mealDto.getStrArea())).placeholder(R.drawable.ic_launcher_foreground).into(flagImage);
        myAdapter.notifyItemChanged(mealDto.getIngredientDtos());
        myAdapter.notifyDataSetChanged();
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
        // Adapter
        myAdapter = new MyAdapter(getContext(), MealDetailsArgs.fromBundle(getArguments()).getMealto().getIngredientDtos());
        recyclerView = view.findViewById(R.id.recyclerViewIngredients);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);

        myAdapter.notifyItemChanged(MealDetailsArgs.fromBundle(getArguments()).getMealto().getIngredientDtos());
        myAdapter.notifyDataSetChanged();


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        Boolean isQuest = sharedPreferences.getBoolean("isQuest", false);

        MaterialButton addToCalenderBtn = view.findViewById(R.id.addToCalenderBtn);
        if (isQuest) {
            favoriteBtn.setEnabled(false);
            addToCalenderBtn.setEnabled(false);
            Snackbar.make(view, "welcome to Guest Mode \n Login to get full access", Snackbar.LENGTH_LONG).show();
        }

        addToCalenderBtn.setOnClickListener(v -> {
            showCalender();
        });

        // Safe Args
        globalMealDto = MealDetailsArgs.fromBundle(getArguments()).getMealto();
        showMeal(globalMealDto);
        mealDetailsPresentor.getMealByIdRemote(globalMealDto.getIdMeal()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).doOnNext(mealDto-> {
                    if(mealDto.getMeals().size()>0){
                        mealDto.getMeals().get(0);
                        Log.i(TAG, "onViewCreated: " + mealDto.getMeals().get(0).getStrArea() );
                        showMeal(mealDto.getMeals().get(0));
                    }
                }).subscribe(mealDto ->{if(mealDto.getMeals().size()>0){
                    mealDto.getMeals().get(0);
                    Log.i(TAG, "onViewCreated: this" + mealDto.getMeals().get(0).getStrArea() );
                    showMeal(mealDto.getMeals().get(0));}});



        WebView webView = view.findViewById(R.id.videoView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String videoId;

        if (globalMealDto.getStrYoutube() != null) {
            videoId = getYouTubeVideoId(globalMealDto.getStrYoutube());
        } else {
            videoId = "";
        }

        String html = "<html><body><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"
                + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";

        webView.loadData(html, "text/html", "utf-8");

        favoriteBtn.setOnClickListener(v -> {
            globalMealDto.setFavorite(true);
            mealDetailsPresentor.insertMeal(globalMealDto).doOnComplete(() -> showSnackBar("Meal Added to Favorite Successfully")).subscribe();
            mealDetailsPresentor.insertMealToFireBase(globalMealDto);
        });

    }


    String getYouTubeVideoId(String url) {
        String videoId = "";
        videoId = url.substring(url.indexOf("=") + 1);
        return videoId;
    }

    ;


    void showCalender() {

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .build();

        datePicker.show(this.getParentFragmentManager(), "DATE_PICKER");

        datePicker.addOnPositiveButtonClickListener(selection -> {
            MealsCalenderDto mealCalenderDto = new MealsCalenderDto(globalMealDto.getIdMeal(), datePicker.getHeaderText());
            mealDetailsPresentor.insertMeal(globalMealDto)
                    .doOnError(e -> showSnackBar(e.getMessage()))
                    .doOnComplete(() -> mealDetailsPresentor.insertToCalender(mealCalenderDto))
                    .subscribe();
            mealDetailsPresentor.insertMealToFireBase(globalMealDto);
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
            Glide.with(context).load("https://www.themealdb.com/images/ingredients/" + ingredientDtoViews.get(position).getIngredient() + "-Small.png").placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
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