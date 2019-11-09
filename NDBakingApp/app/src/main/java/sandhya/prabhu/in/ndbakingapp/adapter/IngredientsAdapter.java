package sandhya.prabhu.in.ndbakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.ndbakingapp.R;
import sandhya.prabhu.in.ndbakingapp.activities.RecipeListActivity;
import sandhya.prabhu.in.ndbakingapp.model.Ingredients;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {


    private RecipeListActivity recipeListActivity;
    private List<Ingredients> ingredientsList;
    private boolean mTwoPane;

    public IngredientsAdapter(RecipeListActivity recipeListActivity, List<Ingredients> ingredientsList, boolean mTwoPane) {
        this.recipeListActivity = recipeListActivity;
        this.ingredientsList = ingredientsList;
        this.mTwoPane = mTwoPane;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(recipeListActivity);
        View v = layoutInflater.inflate(R.layout.ing_row, parent, false);
        return new IngredientsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {

        holder.ingName.setText(ingredientsList.get(position).getIngredient());
        holder.ingMeasure.setText(ingredientsList.get(position).getMeasure());
        holder.ingQuantity.setText(String.valueOf(ingredientsList.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ing_name)
        TextView ingName;

        @BindView(R.id.ing_quantity)
        TextView ingQuantity;

        @BindView(R.id.ing_measure)
        TextView ingMeasure;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
