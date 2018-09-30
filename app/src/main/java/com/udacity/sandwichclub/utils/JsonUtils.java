/*
 * Copyright (C) 2018 Pack Heng
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use these files except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.udacity.sandwichclub.utils;

import android.util.Log;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.udacity.sandwichclub.model.Sandwich;

/**
 * Helper class with JSON utilities
 */
public class JsonUtils {
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    /**
     * Returns an {@link Sandwich} from a JSON String, null if the JSON String is null
     * or empty.
     *
     * @param json a JSON String.
     * @return a {@link Sandwich} or null.
     */
    public static Sandwich parseSandwichJson(String json) {

        if (json == null || json.isEmpty()) {
            return null;
        }

        final String EMPTY_STRING = "";

        // Keys for parsing JSON String
        final String NAME = "name";
        final String MAIN_NAME = "mainName";
        final String ALSO_KNOWN_AS = "alsoKnownAs";
        final String PLACE_OF_ORIGIN = "placeOfOrigin";
        final String DESCRIPTION = "description";
        final String IMAGE = "image";
        final String INGREDIENTS = "ingredients";

        Sandwich sandwich = null;

        try {
            JSONObject joBase = new JSONObject(json);

            JSONObject joName = joBase.optJSONObject(NAME);
            if (joName == null) {
                return null;
            }

            String mainName = joName.optString(MAIN_NAME, EMPTY_STRING);

            ArrayList<String> alsoKnownAs = new ArrayList<String>();
            JSONArray jaAlsoKnownAs = joName.optJSONArray(ALSO_KNOWN_AS);
            if (jaAlsoKnownAs != null) {
                for (int i = 0; i < jaAlsoKnownAs.length(); i++) {
                    String item = jaAlsoKnownAs.optString(i);
                    if (item != null) {
                        alsoKnownAs.add(item);
                    }
                }
            }

            String placeOfOrigin = joBase.optString(PLACE_OF_ORIGIN, EMPTY_STRING);
            String description = joBase.optString(DESCRIPTION, EMPTY_STRING);
            String image = joBase.optString(IMAGE, EMPTY_STRING);

            ArrayList<String> ingredients = new ArrayList<String>();
            JSONArray jaIngredients = joBase.optJSONArray(INGREDIENTS);
            if (jaIngredients != null) {
                for (int i = 0; i < jaIngredients.length(); i++) {
                    String item = jaIngredients.optString(i);
                    if (item != null) {
                        ingredients.add(item);
                    }
                }
            }

            sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description,
                    image, ingredients);
            } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem with parsing the JSON string", e);
        }

        return sandwich;
    }
}
