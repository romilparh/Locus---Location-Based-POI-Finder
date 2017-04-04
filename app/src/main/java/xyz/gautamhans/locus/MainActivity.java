package xyz.gautamhans.locus;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //List to inflate RecyclerView with ViewHolder
    private List<CategoryDetails> categoryDetailsList;
    private List<PlaceCardDetails> placeCardDetails;

    //Category list Recycler View
    private RecyclerView rv_cat;
    private RecyclerView rv_places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,    drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rv_cat = (RecyclerView) findViewById(R.id.rv_cat);
        rv_places = (RecyclerView) findViewById(R.id.rv_places);

        LinearLayoutManager llm_cat = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        LinearLayoutManager llm_places = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        rv_cat.setLayoutManager(llm_cat);
        rv_places.setLayoutManager(llm_places);

        initializeDataCat();
        initializeDataPlaces();
        initializeAdapter();
    }

    public void initializeDataCat(){
        categoryDetailsList = new ArrayList<>();
        categoryDetailsList.add(new CategoryDetails(R.drawable.atm));
        categoryDetailsList.add(new CategoryDetails(R.drawable.bank));
        categoryDetailsList.add(new CategoryDetails(R.drawable.bar));
        categoryDetailsList.add(new CategoryDetails(R.drawable.hospital));
        categoryDetailsList.add(new CategoryDetails(R.drawable.parks));
        categoryDetailsList.add(new CategoryDetails(R.drawable.police));
        categoryDetailsList.add(new CategoryDetails(R.drawable.shopping));
        categoryDetailsList.add(new CategoryDetails(R.drawable.store));
        categoryDetailsList.add(new CategoryDetails(R.drawable.train));
        categoryDetailsList.add(new CategoryDetails(R.drawable.taxi));
        categoryDetailsList.add(new CategoryDetails(R.drawable.theater));
    }

    public void initializeDataPlaces(){
        placeCardDetails = new ArrayList<>();
        placeCardDetails.add(new PlaceCardDetails(R.drawable.brewmaster, 4.0, "The Brewmaster", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.curo_mall, 3.9, "Curo Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.pvr_mbdmall, 4.5, "PVR MBD Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.hotel_cabbana, 4.6, "Hotel Cabbana", "Phagwara-Jalandhar Highway"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.brewmaster, 4.0, "The Brewmaster", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.curo_mall, 3.9, "Curo Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.pvr_mbdmall, 4.5, "PVR MBD Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.hotel_cabbana, 4.6, "Hotel Cabbana", "Phagwara-Jalandhar Highway"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.brewmaster, 4.0, "The Brewmaster", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.curo_mall, 3.9, "Curo Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.pvr_mbdmall, 4.5, "PVR MBD Mall", "3rd Floor & Terrace, Chun Mun Mall"));
        placeCardDetails.add(new PlaceCardDetails(R.drawable.hotel_cabbana, 4.6, "Hotel Cabbana", "Phagwara-Jalandhar Highway"));
    }

    public void initializeAdapter(){
        RVCat_Adapter adapter = new RVCat_Adapter(categoryDetailsList);
        rv_cat.setAdapter(adapter);
        RVAdapter_PlaceCard adapter_placeCard = new RVAdapter_PlaceCard(placeCardDetails);
        rv_places.setAdapter(adapter_placeCard);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_saved_places) {

        } else if (id == R.id.nav_reminders) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_help) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
