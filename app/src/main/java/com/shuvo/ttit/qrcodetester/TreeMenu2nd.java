package com.shuvo.ttit.qrcodetester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.shuvo.ttit.qrcodetester.adapters.CustomExpandAdapter2nd;
import com.shuvo.ttit.qrcodetester.adapters.CustomExpandableListAdapter;
import com.shuvo.ttit.qrcodetester.adapters.UserAdapter;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import static com.shuvo.ttit.qrcodetester.OracleConnection.createConnection;

public class TreeMenu2nd extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserAdapter.ClickedItem {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView tool;
    public static TextView cartBadge;

    ArrayList<CategoryList> categoryLists;

    private Boolean conn = false;
    private Boolean connected = false;

    final Random mRandom = new Random(-2067702723);

    public static ExpandableListView expandableListView;
    private CustomExpandAdapter2nd adapter;
    public static  boolean fromPicture = false;

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<NavigationUserList> navigationUserLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_menu2nd);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);
        tool = findViewById(R.id.nav_icon_main_menu);
        expandableListView = findViewById(R.id.nav_list_second);

        cartBadge = findViewById(R.id.cart_badge);
        cartBadge.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.user_info_list_view);

        categoryLists = new ArrayList<>();
        navigationUserLists = new ArrayList<>();

        navigationUserLists.add(new NavigationUserList("PROFILE", "user_profile"));
        navigationUserLists.add(new NavigationUserList("MY ORDERS", "my_orders"));
        navigationUserLists.add(new NavigationUserList("FAVORITES","my_favorites"));
        navigationUserLists.add(new NavigationUserList("VOUCHERS","my_vouchers"));
        navigationUserLists.add(new NavigationUserList("LOG OUT", "nav_log_out"));
        navigationUserLists.add(new NavigationUserList("LOGIN", "nav_login"));

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        userAdapter = new UserAdapter(navigationUserLists,this,TreeMenu2nd.this);
        recyclerView.setAdapter(userAdapter);



        // navigation bar implementation
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
//        View listHeaderView = getLayoutInflater().inflate(R.layout.header,null, false);
//        expandableListView.addHeaderView(listHeaderView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout , R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                System.out.println(categoryLists.get(i).getCategoryName());

                if (expandableListView.isGroupExpanded(i)) {
                    expandableListView.collapseGroup(i);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                System.out.println(categoryLists.get(i).toString());
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

                String selectedItem =  (categoryLists.get(i).getSubCategoryLists().get(i1).getIem_name());
                System.out.println(selectedItem);

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        new Check().execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        System.out.println("ITEM ID: "+id);
        item.setChecked(true);

        if (id == R.id.nav_logIn) {
//            Intent intent = new Intent(HomePage.this, Login.class);
//            startActivity(intent);
            Toast.makeText(getApplicationContext(),item.getTitle().toString(), Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.nav_logOut) {
//            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(HomePage.this)
//                    .setTitle("LOG OUT!")
//                    .setMessage("Do you want to log out?")
//                    .setIcon(R.drawable.terrain_shop_icon)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            userInfoLists.clear();
//                            userInfoLists = new ArrayList<>();
//
//                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
//                            editor1.remove(AD_ID);
//                            editor1.remove(AD_CODE);
//                            editor1.remove(AD_NAME);
//                            editor1.remove(AD_ADDRESS);
//                            editor1.remove(AD_PHONE);
//                            editor1.remove(AD_EMAIL);
//                            editor1.remove(AD_DIV_ID);
//                            editor1.remove(LOGIN_TF);
//                            editor1.apply();
//                            editor1.commit();
//                            loginDone = false;
//                            userCheck();
//                        }
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //Do nothing
//                        }
//                    });
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();

            Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.my_orders) {
//            Intent intent = new Intent(HomePage.this, MyOrders.class);
//            startActivity(intent);
            Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.user_profile) {
//            Intent intent = new Intent(HomePage.this, UserProfile.class);
//            startActivity(intent);
            Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_SHORT).show();
        }
        else {
//            for (int i = 0 ; i < categoryLists.size() ; i++) {
//                if (id == Integer.parseInt(categoryLists.get(i).getIem_id())) {
//                    if (categoryLists.get(i).getBelowCat() != null) {
//                        if (categoryLists.get(i).getBelowCat().equals("2")) {
//                            Intent intent = new Intent(HomePage.this, SubCategory.class);
//                            intent.putExtra("IEM ID", categoryLists.get(i).getIem_id());
//                            intent.putExtra("IEM NAME", categoryLists.get(i).getCategoryName());
//                            startActivity(intent);
//                        }
//                        else if (categoryLists.get(i).getBelowCat().equals("3")) {
//                            Intent intent = new Intent(HomePage.this, Products.class);
//                            intent.putExtra("IEM ID", categoryLists.get(i).getIem_id());
//                            intent.putExtra("IEM NAME", categoryLists.get(i).getCategoryName());
//                            startActivity(intent);
//                        }
//                    }
//                    else {
//                        Intent intent = new Intent(HomePage.this, Products.class);
//                        intent.putExtra("IEM ID", categoryLists.get(i).getIem_id());
//                        intent.putExtra("IEM NAME", categoryLists.get(i).getCategoryName());
//                        startActivity(intent);
//                    }
//                    break;
//                }
//            }
//            System.out.println("ID: " + id);
            Toast.makeText(getApplicationContext(),item.getTitle().toString(),Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean isConnected () {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline () {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {

    }

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                CheckAllData();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            if (conn) {



                adapter = new CustomExpandAdapter2nd(TreeMenu2nd.this,categoryLists);
                expandableListView.setAdapter(adapter);


//                Menu menu = navigationView.getMenu();
//                SubMenu subMenu1 = menu.findItem(R.id.all_categories).getSubMenu();
//
//                for (int i = 0 ; i < categoryLists.size(); i++) {
//                    System.out.println("Menu Size: " + menu.size());
//                    subMenu1.add(R.id.categories_id_off_all, Integer.parseInt(categoryLists.get(i).getIem_id()), menu.size()+i+1, "  "+categoryLists.get(i).getCategoryName());
////                    SubMenu subMenu = subMenu1.findItem(Integer.parseInt(categoryLists.get(i).getIem_id())).getSubMenu();
////                    subMenu.add(123455566, 1012,menu.size()+i+1,"NEW SSSUUBBB");
//                }


                conn = false;
                connected = false;

            } else {

                //Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(TreeMenu2nd.this)
                        .setMessage("Slow Internet or Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new Check().execute();
                        dialog.dismiss();
                    }
                });
            }
        }
    }

    public void CheckAllData () {

        try {
            Connection connection = createConnection();
            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();

            categoryLists = new ArrayList<>();
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select IEM_ID, IEM_TYPE, IEM_NAME,\n" +
                    "(Select AVG(iem_type) from ITEM_ECOM_MST A where A.iem_iem_id = B.IEM_ID) as below\n" +
                    "from ITEM_ECOM_MST B where iem_type = 1 \n" +
                    "                    and IEM_ID NOT IN (1,2,3)\n" +
                    "                    order by IEM_ID");

            while (rs.next()) {

                categoryLists.add(new CategoryList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),generateRandomColor(),new ArrayList<>()));
            }
            rs.close();

            for (int i = 0 ; i < categoryLists.size(); i++) {
                String iem_id = categoryLists.get(i).getIem_id();
                ArrayList<SubCategoryList> subCategoryLists = new ArrayList<>();
                ResultSet resultSet = stmt.executeQuery("select IEM_ID, IEM_IEM_ID, IEM_TYPE, IEM_NAME, IEM_THUMB,\n" +
                        "                    (Select AVG(iem_type) from ITEM_ECOM_MST A where A.iem_iem_id = B.IEM_ID) as below\n" +
                        "                    from ITEM_ECOM_MST B where iem_iem_id = "+iem_id+"\n" +
                        "                                        order by IEM_ID");

                while (resultSet.next()) {
                    Blob b=resultSet.getBlob(5);
                    Bitmap bitmap = null;
                    if (b != null) {
                        byte[] barr =b.getBytes(1,(int)b.length());
                         bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
                    }
                    subCategoryLists.add(new SubCategoryList(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),bitmap,resultSet.getString(6)));
                }
                resultSet.close();

                categoryLists.get(i).setSubCategoryLists(subCategoryLists);
            }

            connected = true;

            connection.close();

        } catch (Exception e) {

            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);
        System.out.println(mRandom.nextInt());

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }
}