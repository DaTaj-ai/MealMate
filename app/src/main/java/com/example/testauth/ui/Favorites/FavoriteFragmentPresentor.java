package com.example.testauth.ui.Favorites;

import static io.reactivex.rxjava3.internal.jdk8.FlowableFlatMapStream.subscribe;

import com.example.testauth.Models.MealDto;
import com.example.testauth.Repository.Repository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoriteFragmentPresentor {
    IFavoriteFragment view;
    Repository repository;

    public FavoriteFragmentPresentor(IFavoriteFragment view, Repository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void getAllFavorites() {
        repository.getAllIsFavorites().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(m -> view.showdata(m)).subscribe();
    }

    public void deleteMeal(MealDto mealDto) {
        repository.insertLocal(mealDto).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
