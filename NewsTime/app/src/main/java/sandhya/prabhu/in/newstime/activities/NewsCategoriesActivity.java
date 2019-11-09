package sandhya.prabhu.in.newstime.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import sandhya.prabhu.in.newstime.R;
import sandhya.prabhu.in.newstime.fragments.AboutFragment;
import sandhya.prabhu.in.newstime.fragments.ArticlesFragment;
import sandhya.prabhu.in.newstime.fragments.FavFragment;
import sandhya.prabhu.in.newstime.fragments.FeedbackFragment;
import sandhya.prabhu.in.newstime.fragments.SearchArticlesFragment;

public class NewsCategoriesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.searchEditText)
    EditText searchValue;

    @BindView(R.id.searchButton)
    Button button_search;

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    private String country_code = "";
    private Bundle args;

    public static final String CATEGORY = "category";
    public static final String BUSINESS_CT = "business";
    public static final String HEALTH_CT = "health";
    public static final String SCIENCE_CT = "science";
    public static final String SPORTS_CT = "sports";
    public static final String TECHNOLOGY_CT = "technology";
    public static final String ENTERTAINMENT_CT = "entertainment";
    public static final String SEARCH_STRING = "search_string";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_categories);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra(CountrySelectionActivity.COUNTRY_CODE)) {
            country_code = intent.getExtras().getString(CountrySelectionActivity.COUNTRY_CODE, "");
        }
        args = new Bundle();
        args.putString(CountrySelectionActivity.COUNTRY_CODE, country_code);
        if (savedInstanceState == null) {
            callFragment();
        }
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userSearchString = searchValue.getText().toString();
                Toast.makeText(NewsCategoriesActivity.this, userSearchString, Toast.LENGTH_SHORT).show();
                if (!userSearchString.equalsIgnoreCase("")) {
                    args.putString(SEARCH_STRING, userSearchString);
                    callSearchFragment();
                    searchValue.setText("");
                    linearLayout.setVisibility(View.GONE);
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    searchValue.setError(getString(R.string.enter_text_to_search_error));
                }
            }
        });
    }

    private void callSearchFragment() {
        SearchArticlesFragment searchArticlesFragment = new SearchArticlesFragment();
        searchArticlesFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.articleContainer, searchArticlesFragment).commit();
    }

    private void callFragment() {
        ArticlesFragment articlesFragment = new ArticlesFragment();
        articlesFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.articleContainer, articlesFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            linearLayout.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_business) {
            next(BUSINESS_CT);
        } else if (id == R.id.nav_entertainment) {
            next(ENTERTAINMENT_CT);
        } else if (id == R.id.nav_fav) {
            FavFragment favFragment = new FavFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.articleContainer, favFragment).commit();
        } else if (id == R.id.nav_health) {
            next(HEALTH_CT);
        } else if (id == R.id.nav_science) {
            next(SCIENCE_CT);
        } else if (id == R.id.nav_sports) {
            next(SPORTS_CT);
        } else if (id == R.id.nav_about) {
            AboutFragment aboutFragment = new AboutFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.articleContainer, aboutFragment).commit();
        } else if (id == R.id.nav_tech) {
            next(TECHNOLOGY_CT);
        } else {
            callFeedbackFragment();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callFeedbackFragment() {
        Intent i = getIntent();
        if (i.hasExtra(MainActivity.USER_NAME)) {
            String username = i.getExtras().getString(MainActivity.USER_NAME);
            args.putString(MainActivity.USER_NAME, username);
            FeedbackFragment feedbackFragment = new FeedbackFragment();
            feedbackFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.articleContainer, feedbackFragment).commit();
        }
    }

    private void next(String ct) {
        args.putString(CATEGORY, ct);
        args.putString(CountrySelectionActivity.COUNTRY_CODE, country_code);
        callFragment();
    }
}
