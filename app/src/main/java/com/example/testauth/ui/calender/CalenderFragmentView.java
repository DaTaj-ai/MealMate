package com.example.testauth.ui.calender;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.testauth.helper.CountryFlag;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.MealsCalenderDto;
import com.example.testauth.R;
import com.example.testauth.Repository.RepositoryImpl;
import com.example.testauth.Repository.datasources.MealLocalDataSourceImpl;
import com.example.testauth.Repository.datasources.MealRemoteDataSourceImpl;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CalenderFragmentView extends Fragment implements ICalenderFragmentView {

    TextView DateMealsText;
    private static final String TAG = "CalenderFragmentView";
    List<MealDto> GloblaMealList = new ArrayList<>();
    RecyclerView recyclerView;
    View BackUpBtn;
    private CompositeDisposable disposables = new CompositeDisposable();

    MyCalenderAdapter myCalenderAdapter;

    public CalenderFragmentView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calender, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DateMealsText = view.findViewById(R.id.mealsCalenderText);

        // Presenter
        CalenderFragmentPresenter calenderFragmentPresenter = new CalenderFragmentPresenter(this, RepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(getContext())));


        // RecyclerView
        myCalenderAdapter = new MyCalenderAdapter(getContext(), GloblaMealList);
        BackUpBtn = view.findViewById(R.id.backupBtn);
        recyclerView = (RecyclerView) view.findViewById(R.id.CalenderRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myCalenderAdapter);



        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // CalendarView month is 0-based, so add 1
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);

                // Format the date as "MMM dd, yyyy"
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                String formattedDate = dateFormat.format(calendar.getTime());
                DateMealsText.setText("Meals for" + formattedDate);

                calenderFragmentPresenter.getMealsOfData(formattedDate)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(ignored -> calenderFragmentPresenter.getMealsByIdes(ignored))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                meals -> {
                                    myCalenderAdapter.notifyItemChanged(meals);
                                    myCalenderAdapter.notifyDataSetChanged();
                                },
                                throwable -> {
                                    Log.e(TAG, "Error fetching meals: " + throwable.getMessage());
                                }
                        );

            }
        });



        BackUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calenderFragmentPresenter.getMealsFromFireBase().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<MealDto> mealsList = new ArrayList<>();

                        for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
                            MealDto meal = mealSnapshot.getValue(MealDto.class);
                            if (meal != null) {
                                mealsList.add(meal);
                                calenderFragmentPresenter.insertMeal(meal);
                                Snackbar.make(view, "Added to meals ", Snackbar.LENGTH_SHORT).show();
                                Log.d(TAG, "Meal Found: " + meal.getIdMeal());
                            }
                        }

                        Log.d(TAG, "Total Meals Found: " + mealsList.size());

                        calenderFragmentPresenter.getCalenderFromFireBase().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                List<MealsCalenderDto> calendarList = new ArrayList<>();

                                for (DataSnapshot mealSnapshot : snapshot.getChildren()) {
                                    MealsCalenderDto meal = mealSnapshot.getValue(MealsCalenderDto.class);
                                    if (meal != null) {
                                        calendarList.add(meal);
                                        calenderFragmentPresenter.insertToCalender(meal);
                                        Log.d(TAG, "Calendar Meal Found: " + meal.getMealId() + " " + meal.getDate());
                                    }
                                }

                                Snackbar.make(view, "Total Calendar Entries Found: " + calendarList.size(), Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "Error: " + error.getMessage());
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, "Error: " + error.getMessage());
                    }
                });

            }
        });

    }


    public class MyCalenderAdapter extends RecyclerView.Adapter<MyCalenderAdapter.ViewHolder> {
        private final Context context;
        private List<MealDto> MealDtoList;

        public MyCalenderAdapter(Context _context, List<MealDto> users) {
            super();
            context = _context;
            MealDtoList = users;
        }

        @NonNull
        @Override
        public MyCalenderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.fragment_inspiration_card, parent, false);
            MyCalenderAdapter.ViewHolder vh = new MyCalenderAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(@NonNull MyCalenderAdapter.ViewHolder holder, int position) {
            holder.nameTxt.setText(MealDtoList.get(position).getStrMeal());
            Glide.with(context).load(MealDtoList.get(position).getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
            holder.locationTxt.setText(MealDtoList.get(position).getStrArea());
            holder.categoryTxt.setText(MealDtoList.get(position).getStrCategory());
            Glide.with(context).load(CountryFlag.getFlagUrl(MealDtoList.get(position).getStrArea())). into(holder.flagImage);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CalenderFragmentViewDirections.ActionCalenderFragmentToMealDetails action = CalenderFragmentViewDirections.actionCalenderFragmentToMealDetails(MealDtoList.get(position));
                    Log.i(TAG, "onClick: " + MealDtoList.get(position).getStrMeal());
                    Navigation.findNavController(view).navigate(action);
                }
            });

         }

        @Override
        public int getItemCount() {
            return MealDtoList.size();
        }

        public void notifyItemChanged(List<MealDto> MealDtos) {
            this.MealDtoList = MealDtos;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            ConstraintLayout constraintLayout;
            View layout;
            TextView nameTxt;
            ImageView image;
            ImageView flagImage;
            TextView categoryTxt;
            TextView locationTxt;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView;
                nameTxt = layout.findViewById(R.id.mealNameInspirationCard);
                image = layout.findViewById(R.id.inspirationCardImage);
                flagImage = layout.findViewById(R.id.flagImageMealDetails);
                categoryTxt = layout.findViewById(R.id.categoryCard);
                locationTxt = layout.findViewById(R.id.varLocation);

            }
        }

    }


}







