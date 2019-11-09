package sandhya.prabhu.in.ndbakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import sandhya.prabhu.in.ndbakingapp.activities.RecipeDetailActivity;
import sandhya.prabhu.in.ndbakingapp.activities.RecipeListActivity;
import sandhya.prabhu.in.ndbakingapp.fragments.RecipeDetailFragment;
import sandhya.prabhu.in.ndbakingapp.model.Steps;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.RecipeDetailViewHolder> {

    private RecipeListActivity recipeListActivity;
    private List<Steps> stepsList;
    private boolean mTwoPane;

    public StepsAdapter(RecipeListActivity recipeListActivity, List<Steps> stepsList, boolean mTwoPane) {
        this.recipeListActivity = recipeListActivity;
        this.stepsList = stepsList;
        this.mTwoPane = mTwoPane;
    }

    @Override
    public RecipeDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(recipeListActivity);
        View v = layoutInflater.inflate(R.layout.recipe_list_content, parent, false);
        return new RecipeDetailViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecipeDetailViewHolder holder, int position) {
        holder.stepDesc.setText(stepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_desc)
        TextView stepDesc;

        public RecipeDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mTwoPane) {
                        Bundle args = new Bundle();
                        args.putSerializable(RecipeListActivity.RECIPE_STEPS, (Serializable) stepsList);
                        args.putInt(RecipeListActivity.STEP_POSITION, getAdapterPosition());
                        RecipeDetailFragment fragment = new RecipeDetailFragment();
                        fragment.setArguments(args);
                        recipeListActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipe_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, RecipeDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(RecipeListActivity.RECIPE_STEPS, (Serializable) stepsList);
                        intent.putExtra(RecipeListActivity.BUNDLE, bundle);
                        intent.putExtra(RecipeListActivity.STEP_POSITION, getAdapterPosition());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
