package com.example.testauth.ui.Favorites;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.MotionButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.testauth.Models.helper.CountryFlag;
import com.example.testauth.Models.MealDto;
import com.example.testauth.R;
import com.example.testauth.Repository.RepositoryImpl;
import com.example.testauth.Repository.datasources.MealLocalDataSourceImpl;
import com.example.testauth.Repository.datasources.MealRemoteDataSourceImpl;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment implements IFavoriteFragment {

    List<MealDto> GloblaMealList = new ArrayList<>();
    RecyclerView recyclerView;
    MyFavoriteAdapter myFavoriteAdapter;
    LottieAnimationView emptyAnimation;
    FavoriteFragmentPresentor favoriteFragmentPresentor;
    private static final String TAG = "HomeContentFragment";

    @Override
    public void showdata(List<MealDto> meals) {
        GloblaMealList = meals;
        myFavoriteAdapter.notifyItemChanged(GloblaMealList);
        myFavoriteAdapter.notifyDataSetChanged();

    }

    public FavoriteFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favoriteFragmentPresentor = new FavoriteFragmentPresentor(this, RepositoryImpl.getInstance(MealRemoteDataSourceImpl.getInstance(), MealLocalDataSourceImpl.getInstance(getContext())));
        favoriteFragmentPresentor.getAllFavorites();
        emptyAnimation = view.findViewById(R.id.empty);
        myFavoriteAdapter = new MyFavoriteAdapter(getContext(), GloblaMealList);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewFavorite);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myFavoriteAdapter);
    }

    @Override
    public void setEmptyAnimation(Boolean state) {
        if (state) {
            emptyAnimation.setVisibility(View.VISIBLE);
        } else {
            new Handler().postDelayed(() -> {
                emptyAnimation.setVisibility(View.GONE);
            }, 2000);
        }
    }

    public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.ViewHolder> {
        private final Context context;
        private List<MealDto> MealDtoList;

        public MyFavoriteAdapter(Context _context, List<MealDto> users) {
            super();
            context = _context;
            MealDtoList = users;
        }

        @NonNull
        @Override
        public MyFavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.fragment_inspiration_card, parent, false);
            MyFavoriteAdapter.ViewHolder vh = new MyFavoriteAdapter.ViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(@NonNull MyFavoriteAdapter.ViewHolder holder, int position) {
            holder.nameTxt.setText(MealDtoList.get(position).getStrMeal());
            Glide.with(context).load(MealDtoList.get(position).getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
            holder.countryTxt.setText(MealDtoList.get(position).getStrArea());
            holder.categoryTxt.setText(MealDtoList.get(position).getStrCategory());
            Glide.with(context).load(CountryFlag.getFlagUrl(MealDtoList.get(position).getStrArea())). into(holder.flagFavorite);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavoriteFragmentDirections.ActionFavoriteFragmentToMealDetails action = FavoriteFragmentDirections.actionFavoriteFragmentToMealDetails(MealDtoList.get(position));
                    Log.i(TAG, "onClick: " + MealDtoList.get(position).getStrMeal());
                    Navigation.findNavController(view).navigate(action);
                }
            });

            holder.deleteFromFavoriteBtn.setVisibility(View.VISIBLE);
            holder.deleteFromFavoriteBtn.setBackgroundResource(R.drawable.delete);
            holder.deleteFromFavoriteBtn.setOnClickListener(v -> {
//            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationX", 0f, 100f);
//            animator.setDuration(500);
//            animator.setInterpolator(new AccelerateDecelerateInterpolator());
//            animator.start();
            Snackbar.make(v, "Meal Deleted From Favorites", Snackbar.LENGTH_LONG).show();

            MealDtoList.get(position).setFavorite(false);
            favoriteFragmentPresentor.deleteMeal(MealDtoList.get(position));
            notifyItemChanged(MealDtoList);
            notifyDataSetChanged();
        });
            if (MealDtoList.size() == 0) {
                setEmptyAnimation(true);
            }
            else{
                setEmptyAnimation(false);
            }
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
            ImageView image, flagFavorite;
            TextView countryTxt, categoryTxt;
            MotionButton deleteFromFavoriteBtn ;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                layout = itemView;
                nameTxt = layout.findViewById(R.id.mealNameInspirationCard);
                image = layout.findViewById(R.id.inspirationCardImage);
                flagFavorite = layout.findViewById(R.id.flagImageMealDetails);
                countryTxt = layout.findViewById(R.id.varLocation) ;
                categoryTxt = layout.findViewById(R.id.categoryCard);
                deleteFromFavoriteBtn = layout.findViewById(R.id.add_removeImageFavorites);


            }
        }

    }


}