package com.example.jeffr.bakingapp.dataobjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Recipe implements Parcelable {
    private int id;
    private List<Ingredient> ingredients;
    private List<Step> steps;
    private int servings;
    private  String name;

    public Recipe(){

    }
    public Recipe(int id, List<Ingredient> ingredients, List<Step> steps, int servings, String name) {
        this.id = id;
        this.ingredients = ingredients;
        this.servings = servings;
        this.steps = steps;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List<Step> getSteps() {

        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public List<Ingredient> getIngredients() {

        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.name);
    }

    protected Recipe(Parcel in) {
        this.id = in.readInt();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.steps = in.createTypedArrayList(Step.CREATOR);
        this.servings = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}
