package sandhya.prabhu.in.ndbakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndbakingapp.BakingWidget;
import sandhya.prabhu.in.ndbakingapp.R;
import sandhya.prabhu.in.ndbakingapp.adapter.IngredientsAdapter;
import sandhya.prabhu.in.ndbakingapp.adapter.StepsAdapter;
import sandhya.prabhu.in.ndbakingapp.model.Ingredients;
import sandhya.prabhu.in.ndbakingapp.model.Recipe;
import sandhya.prabhu.in.ndbakingapp.model.Steps;

public class RecipeListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private SharedPreferences sharedPreferences;

    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_SERVINGS = "rec_servings";
    public static final String RECIPE_IMAGE = "recipe_image";
    public static final String RECIPE_STEPS = "recipe_steps";
    public static final String RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String BUNDLE = "BUNDLE";
    public static final String STEP_POSITION = "step_pos";
    static RecipeListActivity INSTANCE;
    private String recipe_name;
    private String recipe_image;
    private int recipe_id;
    private int recipe_servings;
    Recipe recipe;
    private List<Ingredients> ingredientsList;
    private List<Steps> stepsList;

    @BindView(R.id.ingredients_recycler)
    RecyclerView ing_recyclerView;

    @BindView(R.id.steps_recycler)
    RecyclerView stepsRecyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        INSTANCE = this;
        sharedPreferences = getSharedPreferences(getString(R.string.pref_file_name), MODE_PRIVATE);
        ButterKnife.bind(this);

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }
        fetchNetworkSourceData();
        assert stepsRecyclerView != null;
        setupIngRecyclerView(ing_recyclerView);
        setupRecyclerView(stepsRecyclerView);
    }


    @SuppressWarnings("unchecked")
    private void fetchNetworkSourceData() {
        Intent intent = getIntent();
        recipe_name = intent.getExtras().getString(RecipeListActivity.RECIPE_NAME, "");
        recipe_image = intent.getExtras().getString(RecipeListActivity.RECIPE_IMAGE, "");
        recipe_id = intent.getExtras().getInt(RecipeListActivity.RECIPE_ID, 0);
        recipe_servings = intent.getExtras().getInt(RecipeListActivity.RECIPE_SERVINGS, 0);
        Bundle bundle = intent.getExtras().getBundle(RecipeListActivity.BUNDLE);
        ingredientsList = (List<Ingredients>) bundle.getSerializable(RecipeListActivity.RECIPE_INGREDIENTS);
        stepsList = (List<Steps>) bundle.getSerializable(RecipeListActivity.RECIPE_STEPS);
        recipe = new Recipe(recipe_id, recipe_name, ingredientsList, stepsList, recipe_servings, recipe_image);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (int i = 0; i < ingredientsList.size(); i++) {
            stringBuilder.append(counter + " " + ingredientsList.get(i).getIngredient() + "\n");
            counter++;
        }
        String line = recipe_name + "\n" + stringBuilder.toString();

        editor.putString(getString(R.string.ing_list_pref), line);
        editor.apply();

        Intent inte = new Intent(this, BakingWidget.class);
        inte.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), BakingWidget.class));
        inte.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(inte);

        if (recipe_name != null && !recipe_name.equals("")) {
            toolbar.setTitle(recipe_name);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new StepsAdapter(this, stepsList, mTwoPane));
    }

    private void setupIngRecyclerView(RecyclerView ing_recyclerView) {
        ing_recyclerView.setAdapter(new IngredientsAdapter(this, ingredientsList, mTwoPane));
    }

    public static RecipeListActivity getActivityInstance() {
        return INSTANCE;
    }

    public Recipe getData() {
        return recipe;
    }
}

