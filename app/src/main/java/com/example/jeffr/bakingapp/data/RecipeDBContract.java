package com.example.jeffr.bakingapp.data;
import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeDBContract {

    public static final String CONTENT_AUTHORITY = "com.example.jeffr.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECIPE = "recipe";

    public static final String PATH_INGREDIENTS = "ingredients";

    public static final String PATH_STEPS = "steps";

    public static class RecipeEntry implements BaseColumns {

        public static final Uri RECIPE_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_RECIPE)
                .build();
        public static final String TABLE_NAME = "recipeTable";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_SERVINGS = "servings";
        public static final String COLUMN_NAME = "name";
    }

    public static class IngredientEntry implements  BaseColumns{

        public static final Uri INGREDIENT_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_INGREDIENTS)
                .build();
        public static final String TABLE_NAME = "ingredientTable";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_NAME = "ingredient";
    }

    public static class StepEntry implements BaseColumns{

        public static final Uri STEP_CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_STEPS)
                .build();
        public static final String TABLE_NAME = "stepTable";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_VIDEO_URL = "videoUrl";
        public static final String COLUMN_RECIPE_ID = "recipeId";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnailUrl";
    }
}
