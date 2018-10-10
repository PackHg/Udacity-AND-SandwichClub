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

package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    // Key for saving and restoring mPosition to and from the Bundle
    private static final String POSITION_KEY = "position";

    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(POSITION_KEY, DEFAULT_POSITION);
        } else {
            Intent intent = getIntent();
            if (intent == null) {
                closeOnError();
            } else {
                mPosition = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
            }
        }

        if (mPosition == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[mPosition];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        setTitle(sandwich.getMainName());
        populateUI(sandwich);

        final ImageView mIngredientsIv = findViewById(R.id.image_iv);
        mIngredientsIv.setVisibility(View.VISIBLE);
        final ProgressBar mProgressBar = findViewById(R.id.loading_spinner_pb);
        mProgressBar.setVisibility(View.VISIBLE);

        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mIngredientsIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        // Hide image view and loading indicator, and display an error message
                        mProgressBar.setVisibility(View.GONE);
                        mIngredientsIv.setVisibility(View.GONE);
                        Toast.makeText(DetailActivity.this, getText(R.string.image_download_error_message),
                                Toast.LENGTH_LONG).show();
                    }
                });
        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(POSITION_KEY, mPosition);
        super.onSaveInstanceState(outState);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Populates the UI of {@link DetailActivity} with the given {@param sandwich} data.
     * Does nothing if {@param sandwich} is null.
     *
     * @param sandwich a {@link Sandwich} object.
     */
    private void populateUI(Sandwich sandwich) {

        if (sandwich == null) {
            // Do nothing
            return;
        }

        TextView alsoKnownAsLabelTv = findViewById(R.id.also_known_label_tv);
        TextView alsoKnownAsTv = findViewById(R.id.also_known_tv);
        ArrayList<String> alsoKnownAs = (ArrayList<String>) sandwich.getAlsoKnownAs();
        String s = toStringWithAnd(alsoKnownAs);
        if (s != null && !s.isEmpty()) {
            alsoKnownAsLabelTv.setVisibility(View.VISIBLE);
            alsoKnownAsTv.setVisibility(View.VISIBLE);
            alsoKnownAsTv.setText(s);
        } else {
            alsoKnownAsLabelTv.setVisibility(View.GONE);
            alsoKnownAsTv.setVisibility(View.GONE);
        }

        TextView placeOfOriginLabelTv = findViewById(R.id.origin_label_tv);
        TextView placeOfOriginTv = findViewById(R.id.origin_tv);
        String origin = sandwich.getPlaceOfOrigin();
        if (!origin.isEmpty()) {
            placeOfOriginLabelTv.setVisibility(View.VISIBLE);
            placeOfOriginTv.setVisibility(View.VISIBLE);
            placeOfOriginTv.setText(sandwich.getPlaceOfOrigin());
        } else {
            placeOfOriginLabelTv.setVisibility(View.GONE);
            placeOfOriginTv.setVisibility(View.GONE);
        }

        TextView descriptionTv = findViewById(R.id.description_tv);
        descriptionTv.setText(sandwich.getDescription());

        TextView ingredientsTv = findViewById(R.id.ingredients_tv);
        ArrayList<String> ingredients = (ArrayList<String>) sandwich.getIngredients();
        s = toStringWithAnd(ingredients);
        if (s != null) {
            ingredientsTv.setText(s);
        }
    }

    /**
     * Returns a {@link String} in the form of "s1, s2, ... and sn" where s1, s2, ...
     * and sn are the {@link String} items of the {@param stringArrayList}.
     * Returns null if {@param stringArrayList} is null.
     *
     * @param stringArrayList a {@link ArrayList<String>}.
     * @return {@link String}
     */
    private String toStringWithAnd(ArrayList<String> stringArrayList) {

        if (stringArrayList == null) {
            return null;
        }

        final String COMMA = ", ";
        final String AND = " and ";
        final String PERIOD = ".";

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringArrayList.size(); i++) {
            if (i == 0) {
                stringBuilder.append(stringArrayList.get(i));
            } else if (i < stringArrayList.size() - 1) {
                stringBuilder.append(COMMA).append(stringArrayList.get(i));
            } else {
                stringBuilder.append(AND).append(stringArrayList.get(i)).append(PERIOD);
            }
        }

        return stringBuilder.toString();
    }
}
