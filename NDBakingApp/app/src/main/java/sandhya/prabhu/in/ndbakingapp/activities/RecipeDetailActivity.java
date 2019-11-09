package sandhya.prabhu.in.ndbakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndbakingapp.R;
import sandhya.prabhu.in.ndbakingapp.fragments.RecipeDetailFragment;
import sandhya.prabhu.in.ndbakingapp.model.Recipe;
import sandhya.prabhu.in.ndbakingapp.model.Steps;

public class RecipeDetailActivity extends AppCompatActivity {

    private List<Steps> stepsList;
    private int step_position;
    private RecipeDetailFragment recipeDetailFragment;

    @BindView(R.id.toolbarTextView)
    TextView toolbarText;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            recipeDetailFragment = (RecipeDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "fragment");
        } else {
            fetchData();
            toolbarText.setText(R.string.steps_detail);
            Bundle args = new Bundle();
            args.putSerializable(RecipeListActivity.RECIPE_STEPS, (Serializable) stepsList);
            args.putInt(RecipeListActivity.STEP_POSITION, step_position);
            recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.container_detail, recipeDetailFragment).commit();
        }

    }

    @SuppressWarnings("unchecked")
    private void fetchData() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra(RecipeListActivity.BUNDLE);
        stepsList = (List<Steps>) args.getSerializable(RecipeListActivity.RECIPE_STEPS);
        step_position = intent.getIntExtra(RecipeListActivity.STEP_POSITION, 0);
        Toast.makeText(this, "" + step_position, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(RecipeListActivity.RECIPE_STEPS, (Serializable) stepsList);
        outState.putInt(RecipeListActivity.STEP_POSITION, step_position);
        getSupportFragmentManager().putFragment(outState, "fragment", recipeDetailFragment);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stepsList = (List<Steps>) savedInstanceState.getSerializable(RecipeListActivity.RECIPE_STEPS);
        step_position = savedInstanceState.getInt(RecipeListActivity.STEP_POSITION, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Recipe recipe = RecipeListActivity.getActivityInstance().getData();
            Intent intent = new Intent(this, RecipeListActivity.class);
            intent.putExtra(RecipeListActivity.RECIPE_NAME, recipe.getName());
            intent.putExtra(RecipeListActivity.RECIPE_ID, recipe.getId());
            intent.putExtra(RecipeListActivity.RECIPE_SERVINGS, recipe.getServings());
            intent.putExtra(RecipeListActivity.RECIPE_IMAGE, recipe.getImage());
            Bundle bundle = new Bundle();
            bundle.putSerializable(RecipeListActivity.RECIPE_INGREDIENTS, (Serializable) recipe.getIngredientsList());
            bundle.putSerializable(RecipeListActivity.RECIPE_STEPS, (Serializable) stepsList);
            intent.putExtra(RecipeListActivity.BUNDLE, bundle);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}