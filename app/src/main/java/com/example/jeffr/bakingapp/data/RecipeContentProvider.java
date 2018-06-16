package com.example.jeffr.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.util.Util;

import butterknife.internal.Utils;
import timber.log.Timber;

public class RecipeContentProvider extends ContentProvider {

    private static final int CODE_RECIPE = 877;
    private static final int CODE_INGREDIENT = 286;
    private static final int CODE_STEPS = 678;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private RecipeDBHelper mOpenHelper1;

    public static UriMatcher buildUriMatcher() {
        Timber.d("Start UriMatcher");

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeDBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, RecipeDBContract.PATH_RECIPE, CODE_RECIPE);

        matcher.addURI(authority, RecipeDBContract.PATH_INGREDIENTS, CODE_INGREDIENT);

        matcher.addURI(authority, RecipeDBContract.PATH_STEPS, CODE_STEPS);

        Timber.d("End UriMatcher");
        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper1 = new RecipeDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Timber.d("Start query");
        Cursor cursor = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE: {
                Timber.d("Went to table: " + RecipeDBContract.RecipeEntry.TABLE_NAME);
                cursor = mOpenHelper1.getReadableDatabase().query(
                        RecipeDBContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_INGREDIENT: {
                Timber.d("Went to table: " + RecipeDBContract.IngredientEntry.TABLE_NAME);
                cursor = mOpenHelper1.getReadableDatabase().query(
                        RecipeDBContract.IngredientEntry.TABLE_NAME +
                                " , " + RecipeDBContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case CODE_STEPS: {
                Timber.d("Went to table: " + RecipeDBContract.StepEntry.TABLE_NAME);
                cursor = mOpenHelper1.getReadableDatabase().query(
                        RecipeDBContract.StepEntry.TABLE_NAME +
                                " , " + RecipeDBContract.RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                Timber.d("Unknown Uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        Timber.d("End query");
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Timber.d("Start insert");
        final SQLiteDatabase recipeDB = mOpenHelper1.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case CODE_RECIPE:
                recipeDB.beginTransaction();
                try {
                    Timber.d("Insert into table: " + RecipeDBContract.RecipeEntry.TABLE_NAME);
                    recipeDB.insertWithOnConflict(RecipeDBContract.RecipeEntry.TABLE_NAME,
                            null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    recipeDB.setTransactionSuccessful();
                } catch (Exception e) {

                } finally {
                    recipeDB.endTransaction();
                }
                break;
            case CODE_INGREDIENT:
                recipeDB.beginTransaction();
                try {
                    Timber.d("Insert into table: " + RecipeDBContract.IngredientEntry.TABLE_NAME);
                    recipeDB.insertWithOnConflict(RecipeDBContract.IngredientEntry.TABLE_NAME,
                            null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    recipeDB.setTransactionSuccessful();
                } catch (Exception e) {

                } finally {
                    recipeDB.endTransaction();
                }
                break;
            case CODE_STEPS:
                recipeDB.beginTransaction();
                try {
                    Timber.d("Insert into table: " + RecipeDBContract.StepEntry.TABLE_NAME);
                    recipeDB.insertWithOnConflict(RecipeDBContract.StepEntry.TABLE_NAME,
                            null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                    recipeDB.setTransactionSuccessful();
                } catch (Exception e) {

                } finally {
                    recipeDB.endTransaction();
                }
                break;
            default:
                Timber.d("Unknown Uri");
        }
        Timber.d("End insert");
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
