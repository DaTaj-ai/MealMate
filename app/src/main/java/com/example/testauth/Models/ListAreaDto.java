package com.example.testauth.Models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ListAreaDto {
    private static final String TAG = "ListAreaDto";
    @SerializedName("meals")
    List<AreaDto> areaDtoList ;

    public List<AreaDto> getAreaDtoList() {
        return areaDtoList;
    }

    public void setAreaDtoList(List<AreaDto> areaDtoList) {
        this.areaDtoList = areaDtoList;
    }
    public List<String> toList(){
        List<String> locallist = new ArrayList<>() ;
        for(AreaDto area : areaDtoList){
            Log.i(TAG, "toList: " + area.getStrArea()  );
            locallist.add(area.getStrArea());
        }
        return locallist;
    }
}
