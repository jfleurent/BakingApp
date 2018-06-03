package com.example.jeffr.bakingapp.data;
import android.provider.BaseColumns;

public class RecipeDBContract {

    public static class RecipeEntry implements BaseColumns {

        public static final String TABLE_NAME = "recipeTable";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_NAME = "name";
    }

    public static class IngredientEntry implements  BaseColumns{

        public static final String TABLE_NAME = "ingredientTable";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_NAME = "ingredient";
    }

    public static class StepEntry implements BaseColumns{

        public static final String TABLE_NAME = "stepTable";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_VIDEO_URL = "videoUrl";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailUrl";
    }
}
