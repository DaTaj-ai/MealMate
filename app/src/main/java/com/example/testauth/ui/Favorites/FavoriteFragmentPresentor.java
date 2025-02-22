package com.example.testauth.ui.Favorites;

import com.example.testauth.Models.MealDto;
import com.example.testauth.Repository.Repository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteFragmentPresentor {
    com.example.testauth.ui.Favorites.IFavoriteFragment view;
    Repository repository;

    public FavoriteFragmentPresentor(com.example.testauth.ui.Favorites.IFavoriteFragment view, Repository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void getAllFavorites() {
        repository.getAllIsFavorites().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(m -> {
                    view.showdata(m);
                    if(m.size() == 0 ){
                        view.setEmptyAnimation(true);
                    }
                    else {
                        view.setEmptyAnimation(false);
                    }
                }).subscribe();
    }

    public void deleteMeal(MealDto mealDto) {
        repository.insertLocal(mealDto).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
