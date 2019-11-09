package sandhya.prabhu.in.ndbakingapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndbakingapp.R;
import sandhya.prabhu.in.ndbakingapp.activities.RecipeListActivity;
import sandhya.prabhu.in.ndbakingapp.model.Recipe;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private FragmentActivity activity;
    private List<Recipe> recipes;

    public RecipeAdapter(FragmentActivity activity, List<Recipe> recipes) {

        this.activity = activity;
        this.recipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View v = layoutInflater.inflate(R.layout.recipe_row, parent, false);
        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {

        holder.recipe.setText(recipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipe_text_view)
        TextView recipe;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Recipe item = recipes.get(pos);
                        Intent intent = new Intent(activity, RecipeListActivity.class);
                        intent.putExtra(RecipeListActivity.RECIPE_NAME, item.getName());
                        intent.putExtra(RecipeListActivity.RECIPE_ID, item.getId());
                        intent.putExtra(RecipeListActivity.RECIPE_SERVINGS, item.getServings());
                        intent.putExtra(RecipeListActivity.RECIPE_IMAGE, item.getImage());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(RecipeListActivity.RECIPE_INGREDIENTS, (Serializable) item.getIngredientsList());
                        bundle.putSerializable(RecipeListActivity.RECIPE_STEPS, (Serializable) item.getStepsList());
                        intent.putExtra(RecipeListActivity.BUNDLE, bundle);
                        activity.startActivity(intent);
                    }
                }
            });
        }
    }
}
