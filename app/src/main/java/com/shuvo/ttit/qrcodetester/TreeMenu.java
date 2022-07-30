package com.shuvo.ttit.qrcodetester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.shuvo.ttit.qrcodetester.Helper.FragmentNavigationManager;
import com.shuvo.ttit.qrcodetester.Helper.NavigationManager;
import com.shuvo.ttit.qrcodetester.adapters.CustomExpandableListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TreeMenu extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle barDrawerToggle;
    private String ActivityTitle;

    ImageView tool;

    private String[] items;

    public static ExpandableListView expandableListView;
    private CustomExpandableListAdapter adapter;
    private List<String> fstTitle;
    public static boolean fromPicture = false;

    private Map<String, List<String>> fstChild;
    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_menu);
        tool = findViewById(R.id.nav_icon_main_menu);

        drawerLayout = findViewById(R.id.drawer_layout);
        expandableListView = findViewById(R.id.nav_list);
        //ActivityTitle = getTitle().toString();
        navigationManager = FragmentNavigationManager.getmInstance(this);

        initItems();

        View listHeaderView = getLayoutInflater().inflate(R.layout.header,null, false);
        expandableListView.addHeaderView(listHeaderView);
        
        getData();

        addDrawersItem();
        setupDrawer();

        if (savedInstanceState == null) {
            selectFirstItemAsDefault();
        }

        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        

    }

    private void selectFirstItemAsDefault() {

        if (navigationManager != null) {
            String firstItem = fstTitle.get(0);
            navigationManager.showFragment(firstItem);
        }
    }


    private void setupDrawer() {

        barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(barDrawerToggle);
        barDrawerToggle.syncState();

    }

    private void addDrawersItem() {
        adapter = new CustomExpandableListAdapter(this,fstTitle, fstChild);
        //expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(adapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                System.out.println(fstTitle.get(i).toString());

                if (expandableListView.isGroupExpanded(i)) {
                    expandableListView.collapseGroup(i);
                }
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                System.out.println(fstTitle.get(i).toString());
                if (!fromPicture)  {
                    expandableListView.collapseGroup(i);
                }
                fromPicture = false;
//                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                fromPicture = false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                String selectedItem =  (fstChild.get(fstTitle.get(i)).get(i1).toString());
                System.out.println(selectedItem);

//                if (items[0].equals(fstTitle.get(i))) {
//                    //navigationManager.showFragment(selectedItem);
//                }
//                else {
//                    throw new IllegalArgumentException("NOT SUPPORTED");
//                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void getData() {

        List<String> title = Arrays.asList("Chocolate","Chips","Nuts","Fresh Fruits","Vegetables");
        List<String> childItem = Arrays.asList("   White Chocolate", "   Milk Chocolate", "   Dark Chocolate", "   Weafer");

        fstChild = new TreeMap<>();
        fstChild.put(title.get(0),childItem);
        fstChild.put(title.get(1),childItem);
        fstChild.put(title.get(2),childItem);
        fstChild.put(title.get(3),childItem);

        fstTitle = new ArrayList<>(fstChild.keySet());

    }

    private void initItems() {

        items = new String[] {"Chocolate","Chips","Nuts","Fresh Fruits","Vegetables"};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}