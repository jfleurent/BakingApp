package com.example.jeffr.bakingapp.utilities;

import com.example.jeffr.bakingapp.dataobjects.Recipe;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

public class JsonUtils {

    public static List<Recipe> getRecipeList(String jsonResponse){
        Timber.d("Start getRecipeList");
        List<Recipe> recipes = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            JSONArray jsonArray = new JSONArray(jsonResponse);
            recipes = objectMapper.readValue(jsonArray
                    .toString(),typeFactory.constructCollectionType(List.class, Recipe.class));
        } catch (JSONException e) {
            Timber.e(e,e.getMessage());
        } catch (JsonParseException e) {
            Timber.e(e,"JsonParseException in getRecipeList");
        } catch (JsonMappingException e) {
            Timber.e(e,"JsonMappingException in getRecipeList");
        } catch (IOException e) {
            Timber.e(e,"IOException in getRecipeList");
        }
        Timber.d("End getRecipeList");
        return  recipes;
    }
}
