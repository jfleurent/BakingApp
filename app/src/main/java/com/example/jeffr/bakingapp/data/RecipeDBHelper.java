package com.example.jeffr.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    private static final String RECIPE_DATABASE_NAME = "recipes.db";

    public RecipeDBHelper(Context context) {
        super(context, RECIPE_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String ENABLE_FORIEGN_KEYS = "PRAGMA foreign_keys = ON;";

        final String CREATE_RECIPE_TABLE = " CREATE TABLE " + RecipeDBContract.RecipeEntry.TABLE_NAME +
                " (" + RecipeDBContract.RecipeEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "
                + RecipeDBContract.RecipeEntry.COLUMN_SERVINGS +" TEXT NOT NULL, "+
                RecipeDBContract.RecipeEntry.COLUMN_NAME +" TEXT NOT NULL );";

        final String CREATE_INGREDIENT_TABLE = "CREATE TABLE "+ RecipeDBContract.IngredientEntry.TABLE_NAME +
                " ("+ RecipeDBContract.IngredientEntry.COLUMN_MEASURE + " TEXT NOT NULL, "+
                RecipeDBContract.IngredientEntry.COLUMN_ID + " INTEGER PRIMARY KEY, "+
                RecipeDBContract.IngredientEntry.COLUMN_NAME + " TEXT NOT NULL, "+
                RecipeDBContract.IngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, "+
                RecipeDBContract.IngredientEntry.COLUMN_QUANTITY + " REAL NOT NULL, "+
                "FOREIGN KEY("+ RecipeDBContract.IngredientEntry.COLUMN_RECIPE_ID+ ") REFERENCES "+
                RecipeDBContract.RecipeEntry.TABLE_NAME + "("+ RecipeDBContract.RecipeEntry.COLUMN_ID +"));";

        final String CREATE_STEPS_TABLE = "CREATE TABLE "+ RecipeDBContract.StepEntry.TABLE_NAME +
                " (" + RecipeDBContract.StepEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "+
                RecipeDBContract.StepEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                RecipeDBContract.StepEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, "+
                RecipeDBContract.StepEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, "+
                RecipeDBContract.StepEntry.COLUMN_THUMBNAIL_URL + " TEXT, "+
                RecipeDBContract.StepEntry. COLUMN_VIDEO_URL + " TEXT, " +
                "FOREIGN KEY("+ RecipeDBContract.StepEntry.COLUMN_RECIPE_ID+ ") REFERENCES "+
                RecipeDBContract.RecipeEntry.TABLE_NAME + "("+ RecipeDBContract.RecipeEntry.COLUMN_ID +"));";

        sqLiteDatabase.execSQL(ENABLE_FORIEGN_KEYS);
        sqLiteDatabase.execSQL(CREATE_RECIPE_TABLE);
        sqLiteDatabase.execSQL(CREATE_INGREDIENT_TABLE);
        sqLiteDatabase.execSQL(CREATE_STEPS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeDBContract.RecipeEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeDBContract.IngredientEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeDBContract.StepEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
