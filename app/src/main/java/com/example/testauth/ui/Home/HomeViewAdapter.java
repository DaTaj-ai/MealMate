package com.example.testauth.ui.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.testauth.Models.MealDto;
import com.example.testauth.Models.helper.CountryFlag;
import com.example.testauth.R;
import java.util.List;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.ViewHolder> {
    private final Context context;
    private List<MealDto> MealDtoList;

    public HomeViewAdapter(Context _context, List<MealDto> users) {
        super();
        context = _context;
        MealDtoList = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.fragment_inspiration_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (MealDtoList.get(position).getStrMealThumb() == null || MealDtoList.get(position).getStrMealThumb().isEmpty() ) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0)); // Hide the item completely
            return;
        }
        holder.nameTxt.setText(MealDtoList.get(position).getStrMeal());
        Glide.with(context).load(MealDtoList.get(position).getStrMealThumb()).placeholder(R.drawable.ic_launcher_foreground).into(holder.image);
        holder.location.setText(MealDtoList.get(position).getStrArea());
        holder.category.setText(MealDtoList.get(position).getStrCategory());
        Glide.with(context).load(CountryFlag.getFlagUrl(MealDtoList.get(position).getStrArea())).into(holder.flagImage);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeContentFragmentDirections.ActionHomeContentFragmentToMealDetails action = HomeContentFragmentDirections.actionHomeContentFragmentToMealDetails(MealDtoList.get(position));
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
        TextView nameTxt, location, category;
        ImageView image;
        ImageView flagImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            nameTxt = layout.findViewById(R.id.mealNameInspirationCard);
            image = layout.findViewById(R.id.inspirationCardImage);
            location = layout.findViewById(R.id.varLocation);
            category = layout.findViewById(R.id.categoryCard);
            flagImage = layout.findViewById(R.id.flagImageMealDetails);


        }
    }

}
