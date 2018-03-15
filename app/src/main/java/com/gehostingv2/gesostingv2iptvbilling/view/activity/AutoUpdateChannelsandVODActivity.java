package com.gehostingv2.gesostingv2iptvbilling.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.gehostingv2.gesostingv2iptvbilling.R;
import com.gehostingv2.gesostingv2iptvbilling.miscelleneious.common.AppConst;
import com.gehostingv2.gesostingv2iptvbilling.miscelleneious.common.Utils;
import com.gehostingv2.gesostingv2iptvbilling.model.callback.GetIPTVCredentialsCallback;
import com.gehostingv2.gesostingv2iptvbilling.model.callback.MyDetailsCallback;
import com.gehostingv2.gesostingv2iptvbilling.model.callback.ValidationIPTVCallback;
import com.gehostingv2.gesostingv2iptvbilling.model.database.DatabaseUpdatedStatusDBModel;
import com.gehostingv2.gesostingv2iptvbilling.model.database.ExpandedMenuModel;
import com.gehostingv2.gesostingv2iptvbilling.model.database.LiveStreamDBHandler;
import com.gehostingv2.gesostingv2iptvbilling.presenter.ClientDetailPresenter;
import com.gehostingv2.gesostingv2iptvbilling.presenter.LoginIPTVPresenter;
import com.gehostingv2.gesostingv2iptvbilling.presenter.XMLTVPresenter;
import com.gehostingv2.gesostingv2iptvbilling.view.adapter.ExpandndableListAdapter;
import com.gehostingv2.gesostingv2iptvbilling.view.fragment.PaymentSuccessfulFragment;
import com.gehostingv2.gesostingv2iptvbilling.view.interfaces.ClientDetailInterface;
import com.gehostingv2.gesostingv2iptvbilling.view.interfaces.LoginIPTVInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

import static com.gehostingv2.gesostingv2iptvbilling.miscelleneious.common.AppConst.TAG_HOME;
import static com.gehostingv2.gesostingv2iptvbilling.miscelleneious.common.AppConst.TAG_MY_DOMAINS;

public class AutoUpdateChannelsandVODActivity extends AppCompatActivity implements LoginIPTVInterface,
    NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener,
    ClientDetailInterface

    {


        @BindView(R.id.tv_account_info)
        TextView tvAccountInfo;
//    @BindView(R.id.rl_account_info)
//    RelativeLayout rlAccountInfo;
//    @BindView(R.id.tv_username_label)
//    TextView tvUsernameLabel;
//    @BindView(R.id.tv_status_label)
//    TextView tvStatusLabel;
//    @BindView(R.id.tv_expiry_date_label)
//    TextView tvExpiryDateLabel;
//    @BindView(R.id.tv_is_trial_label)
//    TextView tvIsTrialLabel;
//    @BindView(R.id.tv_active_conn_label)
//    TextView tvActiveConnLabel;
//    @BindView(R.id.tv_created_at_label)
//    TextView tvCreatedAtLabel;
//    @BindView(R.id.tv_max_connections_label)
//    TextView tvMaxConnectionsLabel;
//    @BindView(R.id.tv_username)
//    TextView tvUsername;
//    @BindView(R.id.tv_status)
//    TextView tvStatus;
//    @BindView(R.id.tv_expiry_date)
//    TextView tvExpiryDate;
//    @BindView(R.id.tv_is_trial)
//    TextView tvIsTrial;
//    @BindView(R.id.tv_active_conn)
//    TextView tvActiveConn;
//    @BindView(R.id.tv_created_at)
//    TextView tvCreatedAt;
//    @BindView(R.id.tv_max_connections)
//    TextView tvMaxConnections;

        @BindView(R.id.iv_back_press)
        ImageView ivBackpress;


//    @BindView(R.id.content_drawer)
//    RelativeLayout contentDrawer;
//    @BindView(R.id.appbar_toolbar)
//    AppBarLayout appbarToolbar;

        @BindView(R.id.bt_save_changes)
        Button btSaveChanges;
        @BindView(R.id.rg_radio)
        RadioGroup rgRadio;
        @BindView(R.id.disable)
        RadioButton disable;
        @BindView(R.id.enable)
        RadioButton enable;


        private ProgressDialog progressDialog;

        private LoginIPTVPresenter loginPresenter;

        private String clientEmail;


        @BindView(R.id.tv_header_title)
        ImageView tvHeaderTitle;
        @BindView(R.id.toolbar)
        Toolbar toolbar;
        @BindView(R.id.frame)
        FrameLayout frame;
        @BindView(R.id.navigationmenu)
        ExpandableListView navigationmenu;
        @BindView(R.id.nav_view)
        NavigationView navView;
        @BindView(R.id.drawer_layout)
        DrawerLayout drawerLayout;
        @BindView(R.id.activity_dashboard)
        RelativeLayout activityDashboard;
        @BindView(R.id.navigation_drawer_bottom)
        NavigationView navigationDrawerBottom;

//    @BindView(R.id.cv_account_info)
//    CardView cvAccountInfo;

        private int clientId;
        private ClientDetailPresenter clientDetailPresenter;
        private TextView clientNanmeTv;
        private TextView clientEmailTv;
        private TextView clientremaingCreditsTv;
        private ExpandndableListAdapter mMenuAdapter;
        private List<ExpandedMenuModel> listDataHeader;
        private HashMap<ExpandedMenuModel, List<String>> listDataChild;
        private String selectedItem = TAG_HOME;
        private Handler mHandler;

        private ImageView logoHomeIV;
        private boolean condition_true = false;
        private ActionBarDrawerToggle actionBarDrawerToggle;

        private Typeface fontOPenSansBold;
        private Typeface fontOPenSansRegular;

        private Context context;
        //    private ProgressDialog progressDialog;
        private SharedPreferences loginPreferencesAfterLogin;
        private SharedPreferences.Editor loginPrefsEditor;
        //    private LoginPresenter loginPresenter;
        private TextView clientNameTv;
        private TypedValue tv;
        private int actionBarHeight;
        private LiveStreamDBHandler liveStreamDBHandler;
        private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelLive =
                new DatabaseUpdatedStatusDBModel();
        private DatabaseUpdatedStatusDBModel databaseUpdatedStatusDBModelEPG =
                new DatabaseUpdatedStatusDBModel();
        private String userName = "";
        private String userPassword = "";

        private XMLTVPresenter xmltvPresenter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_auto_update_channelsand_vod);
        ButterKnife.bind(this);

        context = this;
        clientDetailPresenter = new ClientDetailPresenter(this, context);
        mHandler = new Handler();
        if (navView != null) {
            setupDrawerContent(navView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                setupDrawerContent(navView);
            }
        }
        prepareListData();
        mMenuAdapter = new ExpandndableListAdapter(context, listDataHeader, listDataChild, navigationmenu);
        // setting list adapter
        navigationmenu.setAdapter(mMenuAdapter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        setToggleIconPosition();
        changeStatusBarColor();
        toolbarSet();
        headerView();

        SharedPreferences pref = getSharedPreferences(AppConst.SHARED_PREFERENCE_WHMCS, MODE_PRIVATE);
        clientId = pref.getInt(AppConst.CLIENT_ID, -1);
        clientEmail = pref.getString(AppConst.PREF_EMAIL_WHMCS, "");
        SharedPreferences prefUsername = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_IPTV, MODE_PRIVATE);
        String username = prefUsername.getString(AppConst.LOGIN_PREF_USERNAME_IPTV, "");
        String password = prefUsername.getString(AppConst.LOGIN_PREF_PASSWORD_IPTV, "");
        if (clientId != -1 && clientId != 0) {
            if (!clientEmail.isEmpty() && clientEmail != null && clientNanmeTv != null) {
                clientNanmeTv.setText(clientEmail);
            } else {
                clientDetailPresenter.getMyDetail(clientId);
            }
            if (!username.isEmpty() && !username.equals("") &&
                    !password.isEmpty() && !password.equals("")) {
                clientDetailPresenter.getIPTVDetails(username, password);
            } else {
                clientDetailPresenter.getIPTVServices(clientId);
            }
        } else if (username != null && !username.isEmpty()) {
            clientNanmeTv.setText(username);
        }

        initialize();
        setHeaderFont();
    }

    private void setHeaderFont() {
        fontOPenSansBold = Typeface.createFromAsset(this.getAssets(), "fonts/open_sans.ttf");
        fontOPenSansRegular = Typeface.createFromAsset(this.getAssets(), "fonts/open_sans_regular.ttf");
        tvAccountInfo.setTypeface(fontOPenSansBold);
    }

    private void initialize() {
        context = this;
        liveStreamDBHandler = new LiveStreamDBHandler(context);
//        xmltvPresenter = new XMLTVPresenter(, context);
        loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_PREF_AUTOUPDATE, MODE_PRIVATE);
        String diable = loginPreferencesAfterLogin.getString(AppConst.LOGIN_PREF_AUTOUPDATE, "");
        if(diable.equals(getResources().getString(R.string.enable))){
            enable.setChecked(true);
        }else{
            disable.setChecked(true);
        }
    }


    private void setToggleIconPosition() {
        tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            // make toggle drawable center-vertical, you can make each view alignment whatever you want
            if (toolbar.getChildAt(i) instanceof ImageButton) {
                Toolbar.LayoutParams lp = (Toolbar.LayoutParams) toolbar.getChildAt(i).getLayoutParams();
                lp.gravity = Gravity.CENTER_VERTICAL;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Fragment newFragment = new PaymentSuccessfulFragment();
            AutoUpdateChannelsandVODActivity activity = this;
            FragmentTransaction t = activity.getSupportFragmentManager().beginTransaction();
            t.replace(R.id.frame, newFragment, AppConst.TAG_PAYMENT_SUCCESS);
            t.addToBackStack(AppConst.TAG_PAYMENT_SUCCESS
            );
            t.commit();
        }
    }

    private void toolbarSet() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        logoHomeIV = (ImageView) toolbar.findViewById(R.id.tv_header_title);
        if (logoHomeIV != null)
            logoHomeIV.setImageResource(R.drawable.logo_home_new);
    }

    private void headerView() {
        View header = navView.getHeaderView(0);
        ImageView closeDrawerIV = (ImageView) header.findViewById(R.id.iv_close_drawer);
        clientNanmeTv = (TextView) header.findViewById(R.id.tv_client_name);
        closeDrawerIV.setOnClickListener(this);
        View footer = navigationDrawerBottom.getHeaderView(0);
        TextView logoutUserTV = (TextView) footer.findViewById(R.id.tv_logout);
        logoutUserTV.setOnClickListener(this);
    }

    private void changeStatusBarColor() {
        Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    /**
     * this commneted code can be used if to
     * clear the menu from activity as well as
     * from fragments
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        condition_true = true;
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                return super.onOptionsItemSelected(item);
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;

        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setupDrawerContent(NavigationView navigationView) {

        navigationmenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                //Log.d("DEBUG", "submenu item clicked");

                selectedItem = (String) expandableListView.getExpandableListAdapter().getChild(i, i1);
//                loadFragment(selectedItem);
                if ((selectedItem.equalsIgnoreCase(getString(R.string.drawer_billing_my_invoices))
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_services_order_new_services))
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_support_ticket))
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_support_open_ticket))
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_services_my_services)))) {
                    drawerLayout.closeDrawers();
                }
                selectedFragment(selectedItem);
                return false;
            }
        });
        navigationmenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                int index = expandableListView.getFlatListPosition(ExpandableListView.getPackedPositionForGroup(i));
                selectedItem = listDataHeader.get(i).getIconName();
                if (selectedItem.equalsIgnoreCase(getString(R.string.drawer_home))
                        || selectedItem.equalsIgnoreCase(TAG_MY_DOMAINS)
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_live_tv))
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_live_tv_guide))
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_account_info))
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_vod))
                        || selectedItem.equalsIgnoreCase(getString(R.string.drawer_settings))) {
                    drawerLayout.closeDrawers();
                }
                selectedFragment(selectedItem);
                return false;
            }
        });

//        if (mPendingRunnable != null) {
//            mHandler.post(mPendingRunnable);
//        }

        invalidateOptionsMenu();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.closeDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (drawerView != null && drawerView == navigationmenu) {
                    super.onDrawerSlide(drawerView, 0);
                } else {
                    super.onDrawerSlide(drawerView, slideOffset);
                }
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onResume() {
        super.onResume();

        context = this;
        clientDetailPresenter = new ClientDetailPresenter(this, context);
        mHandler = new Handler();
        if (navView != null) {
            setupDrawerContent(navView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    setupDrawerContent(navView);
            }
        }
        prepareListData();
        mMenuAdapter = new ExpandndableListAdapter(context, listDataHeader, listDataChild, navigationmenu);
        // setting list adapter
        navigationmenu.setAdapter(mMenuAdapter);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        setToggleIconPosition();
        changeStatusBarColor();
        toolbarSet();
        headerView();

        SharedPreferences pref1 = getSharedPreferences(AppConst.SHARED_PREFERENCE_WHMCS, MODE_PRIVATE);
        clientId = pref1.getInt(AppConst.CLIENT_ID, -1);
        clientEmail = pref1.getString(AppConst.PREF_EMAIL_WHMCS, "");
        SharedPreferences prefUsername = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_IPTV, MODE_PRIVATE);
        String username = prefUsername.getString(AppConst.LOGIN_PREF_USERNAME_IPTV, "");
        String password = prefUsername.getString(AppConst.LOGIN_PREF_PASSWORD_IPTV, "");
        if (clientId != -1 && clientId != 0) {
            if (!clientEmail.isEmpty() && clientEmail != null && clientNanmeTv != null) {
                clientNanmeTv.setText(clientEmail);
            } else {
                clientDetailPresenter.getMyDetail(clientId);
            }
            if (!username.isEmpty() && !username.equals("") &&
                    !password.isEmpty() && !password.equals("")) {
                clientDetailPresenter.getIPTVDetails(username, password);
            } else {
                clientDetailPresenter.getIPTVServices(clientId);
            }
        } else if (username != null && !username.isEmpty() &&
                !username.equals("") && !password.isEmpty() && !password.equals("")) {
            clientNanmeTv.setText(username);
            clientDetailPresenter.getIPTVDetails(username, password);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.

        if (actionBarDrawerToggle != null)
            actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectedFragment(String selectedItem) {
        toolbarSet();
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 0) {
            String name = fragmentManager.getBackStackEntryAt(0).getName();
            getSupportFragmentManager().popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        Utils.startActivityOtherDash(context,selectedItem);


    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

        ExpandedMenuModel homeHeader = new ExpandedMenuModel();
        homeHeader.setIconName(getString(R.string.drawer_home));
        homeHeader.setIconImg(R.drawable.drawer_home);
        // Adding data header
        listDataHeader.add(homeHeader);

        ExpandedMenuModel liveTv = new ExpandedMenuModel();
        liveTv.setIconName(getString(R.string.drawer_live_tv));
        liveTv.setIconImg(R.drawable.livetv_icon); //livetv_icon
        // Adding data header
        listDataHeader.add(liveTv);

        ExpandedMenuModel liveTvWihGuie = new ExpandedMenuModel();
        liveTvWihGuie.setIconName(getString(R.string.drawer_live_tv_guide));
        liveTvWihGuie.setIconImg(R.drawable.guide_icon);
        // Adding data header
        listDataHeader.add(liveTvWihGuie);

        ExpandedMenuModel accountInfo = new ExpandedMenuModel();
        accountInfo.setIconName(getString(R.string.drawer_account_info));
        accountInfo.setIconImg(R.drawable.account_info);
        // Adding data header
        listDataHeader.add(accountInfo);

        ExpandedMenuModel videoOnDemand = new ExpandedMenuModel();
        videoOnDemand.setIconName(getString(R.string.drawer_vod));
        videoOnDemand.setIconImg(R.drawable.vod_icon);
        // Adding data header
        listDataHeader.add(videoOnDemand);

        ExpandedMenuModel tvArchive = new ExpandedMenuModel();
        tvArchive.setIconName(getString(R.string.drawer_tv_archieve));
        tvArchive.setIconImg(R.drawable.tv_archive);
        // Adding data header
        listDataHeader.add(tvArchive);

        ExpandedMenuModel settings = new ExpandedMenuModel();
        settings.setIconName(getString(R.string.drawer_settings));
        settings.setIconImg(R.drawable.settings_icon);
        // Adding data header
        listDataHeader.add(settings);

        ExpandedMenuModel servicesHeader = new ExpandedMenuModel();
        servicesHeader.setIconName(getString(R.string.drawer_services));
        servicesHeader.setIconImg(R.drawable.drawer_services);
        // Adding data header
        listDataHeader.add(servicesHeader);

        ExpandedMenuModel billingHeader = new ExpandedMenuModel();
        billingHeader.setIconName(getString(R.string.drawer_billing));
        billingHeader.setIconImg(R.drawable.drawer_billing);
        // Adding data header
        listDataHeader.add(billingHeader);

        ExpandedMenuModel supportHeader = new ExpandedMenuModel();
        supportHeader.setIconName(getString(R.string.drawer_support));
        supportHeader.setIconImg(R.drawable.drawer_suppport);
        // Adding data header
        listDataHeader.add(supportHeader);

        ExpandedMenuModel log_out = new ExpandedMenuModel();
        log_out.setIconName(getString(R.string.drawer_logout));
        log_out.setIconImg(R.drawable.logout_icon);
        // Adding data header
        listDataHeader.add(log_out);


        List<String> servicesList = new ArrayList<String>();
        servicesList.add(getString(R.string.drawer_services_my_services));
        servicesList.add(getString(R.string.drawer_services_order_new_services));
//        servicesList.add("ORDER NEW SERVICES");

        List<String> billingList = new ArrayList<String>();
        billingList.add(getString(R.string.drawer_billing_my_invoices));
//        billingList.add("ORDER NEW SERVICES");
//        billingList.add("MY CREDITS");

        List<String> supportList = new ArrayList<String>();
        supportList.add(getString(R.string.drawer_support_ticket));
        supportList.add(getString(R.string.drawer_support_open_ticket));
        listDataChild.put(listDataHeader.get(7), servicesList);
        listDataChild.put(listDataHeader.get(8), billingList);
        listDataChild.put(listDataHeader.get(9), supportList);



    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        } else if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            Intent dashboardIntent = new Intent(this, DashboardActivity.class);
            startActivity(dashboardIntent);
//            homeFragmentToLoad();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return false;
    }


    //    {R.id.iv_close_drawer,R.id.tv_logout,})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_close_drawer:
                drawerLayout.closeDrawers();
                break;
            case R.id.tv_logout:
                SharedPreferences loginPreferences;
                SharedPreferences.Editor loginPreferencesEditor;
                loginPreferences = context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_IPTV, MODE_PRIVATE);
                loginPreferencesEditor = loginPreferences.edit();
                loginPreferencesEditor.clear();
                loginPreferencesEditor.commit();

                Toast.makeText(this, context.getResources().getString(R.string.logout_successfully), Toast.LENGTH_SHORT).show();
                Intent intentLogout = new Intent(this, LoginWelcomeActivity.class);
                SharedPreferences loginPreferencesClientid;
                SharedPreferences.Editor loginPreferencesClientidEditor;
                loginPreferencesClientid = getSharedPreferences(AppConst.SHARED_PREFERENCE_WHMCS, MODE_PRIVATE);
                loginPreferencesClientidEditor = loginPreferencesClientid.edit();
                loginPreferencesClientidEditor.clear();
                loginPreferencesClientidEditor.commit();
                startActivity(intentLogout);
                break;

            case R.id.iv_back_press:
                Intent dashboardIntent = new Intent(this, DashboardActivity.class);
                startActivity(dashboardIntent);
                break;
        }
    }

    @Override
    public void getClientDetails(MyDetailsCallback myDetailsCallback) {
        if (myDetailsCallback != null &&
                myDetailsCallback.getResult().equals("success")) {
            String email = myDetailsCallback.getEmail();
            SharedPreferences loginPreferencesEmail;
            SharedPreferences.Editor loginPreferencesClientidEditor;
            loginPreferencesEmail = getSharedPreferences(AppConst.SHARED_PREFERENCE_WHMCS, MODE_PRIVATE);
            loginPreferencesClientidEditor = loginPreferencesEmail.edit();
            loginPreferencesClientidEditor.putString(AppConst.PREF_EMAIL_WHMCS, email);
            loginPreferencesClientidEditor.commit();
            if (!email.isEmpty() && email != null) {
                clientNanmeTv.setText(email);
            }
        }
    }


    @Override
    public void getIPTVCredentials(GetIPTVCredentialsCallback getIPTVCredentialsCallback) {
        if (getIPTVCredentialsCallback != null) {
            if (getIPTVCredentialsCallback != null &&
                    getIPTVCredentialsCallback.getResult().equals("success") &&
                    getIPTVCredentialsCallback.getMessage().equals("active services")) {
                String username = getIPTVCredentialsCallback.getUsername();
                String password = getIPTVCredentialsCallback.getPassword();
                if (username.equals("") || username.isEmpty()) {
                    Intent invoiceIntent = new Intent(this, ErrorCode210IPTVUserNameActivity.class);
                    startActivity(invoiceIntent);
                } else if (username != null && !username.isEmpty() && !username.equals("")
                        || password == null || password.equals("") || password.isEmpty()) {
                    Intent invoiceIntent = new Intent(this, ErrorCode211IPTVPasswordActivity.class);
                    startActivity(invoiceIntent);
                } else if (!username.equals("") && !password.equals("")
                        && !username.isEmpty() && !password.isEmpty()) {
                    clientDetailPresenter.getIPTVDetails(username, password);
                }

            } else if (getIPTVCredentialsCallback != null &&
                    getIPTVCredentialsCallback.getResult().equals("success") &&
                    getIPTVCredentialsCallback.getMessage().equals("no active iptv services found")) {

            } else if (getIPTVCredentialsCallback != null &&
                    getIPTVCredentialsCallback.getResult().equals("success") &&
                    getIPTVCredentialsCallback.getMessage().equals("no iptv services found")) {
                String totalResutl = getIPTVCredentialsCallback.getToatlResult();
                if (totalResutl.equals("0")) {
                    Intent noSubsriptionIntent = new Intent(this, NoServicePurchasedActivity.class);
                    startActivity(noSubsriptionIntent);
                }
            }
        } else if (getIPTVCredentialsCallback == null) {
            Intent invoiceIntent = new Intent(this, AuthorizationIssueActivity.class);
            startActivity(invoiceIntent);
        }
    }

    @Override
    public void getIPTVDetails(ValidationIPTVCallback validationIPTVCallback, String validateLogin) {
        if (validationIPTVCallback != null) {
            Integer auth = validationIPTVCallback.getUserLoginInfo().getAuth();
            String userStatus = validationIPTVCallback.getUserLoginInfo().getStatus();
            if (auth == 1) {
                if (userStatus.equals("Active")) {
                    String username = validationIPTVCallback.getUserLoginInfo().getUsername();
                    String password = validationIPTVCallback.getUserLoginInfo().getPassword();
                    String serverPort = validationIPTVCallback.getServerInfo().getPort();
                    String serverUrl = validationIPTVCallback.getServerInfo().getUrl();
                    String expDate = validationIPTVCallback.getUserLoginInfo().getExpDate();
                    String isTrial = validationIPTVCallback.getUserLoginInfo().getIsTrial();
                    String activeCons = validationIPTVCallback.getUserLoginInfo().getActiveCons();
                    String createdAt = validationIPTVCallback.getUserLoginInfo().getCreatedAt();
                    String maxConnections = validationIPTVCallback.getUserLoginInfo().getMaxConnections();
                    List<String> allowedFormatList = validationIPTVCallback.getUserLoginInfo().getAllowedOutputFormats();
                    String allowedFormat = null;
                    if (allowedFormatList.size() != 0) {
                        allowedFormat = allowedFormatList.get(0);
                    }
                    SharedPreferences sharedPreferencesLogin = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_IPTV, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferencesLogin.edit();
                    editor1.putString(AppConst.LOGIN_PREF_USERNAME_IPTV, username);
                    editor1.putString(AppConst.LOGIN_PREF_PASSWORD_IPTV, password);
//                    editor1.putString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, allowedFormat);
                    editor1.putString(AppConst.LOGIN_PREF_SERVER_PORT, serverPort);
                    editor1.putString(AppConst.LOGIN_PREF_SERVER_URL, serverUrl);
                    editor1.putString(AppConst.LOGIN_PREF_EXP_DATE, expDate);
                    editor1.putString(AppConst.LOGIN_PREF_IS_TRIAL, isTrial);
                    editor1.putString(AppConst.LOGIN_PREF_ACTIVE_CONS, activeCons);
                    editor1.putString(AppConst.LOGIN_PREF_CREATE_AT, createdAt);
                    editor1.putString(AppConst.LOGIN_PREF_MAX_CONNECTIONS, maxConnections);
                    editor1.commit();
                } else if (!userStatus.equals("Active")) {
                    Intent invoiceIntent = new Intent(this, ErrorCode215IPTVServiceStatusActivity.class);
                    invoiceIntent.putExtra(AppConst.IPTV_SERVICE_STATUS, userStatus);
                    startActivity(invoiceIntent);
                }
            } else if (auth == 0) {
                Intent invoiceIntent = new Intent(this, AuthorizationIssueActivity.class);
                startActivity(invoiceIntent);
            }
        }
    }


    @Override
    public void atStart() {
    }

    @Override
    public void onFinish() {
    }

    @Override
    public void onFailed(String errorMessage) {
        if (context != null && !errorMessage.isEmpty()) {
            Utils.showToast(context, getResources().getString(R.string.network_error));
        }
    }

    @Override
    public void validateLogin(ValidationIPTVCallback validationIPTVCallback, String validateLogin) {

    }

    @OnClick(R.id.tv_header_title)
    public void onViewClicked() {

        Intent dasIntent1 = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(dasIntent1);
    }

    @OnClick({R.id.bt_save_changes, R.id.btn_back_playerselection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_save_changes:
                int selectedId = rgRadio.getCheckedRadioButtonId();
                RadioButton selectedPlayer = (RadioButton) findViewById(selectedId);
                loginPreferencesAfterLogin = getSharedPreferences(AppConst.LOGIN_PREF_AUTOUPDATE, MODE_PRIVATE);
                loginPrefsEditor = loginPreferencesAfterLogin.edit();
                if (loginPrefsEditor != null) {
                    if(selectedPlayer.getText().toString().equals(getResources().getString(R.string.disable))){
                        loginPrefsEditor.putString(AppConst.LOGIN_PREF_AUTOUPDATE, getResources().getString(R.string.disable));
                    }else{
                        loginPrefsEditor.putString(AppConst.LOGIN_PREF_AUTOUPDATE, getResources().getString(R.string.enable));
                    }
                    loginPrefsEditor.commit();
                    Toast.makeText(AutoUpdateChannelsandVODActivity.this, getResources().getString(R.string.player_setting_save), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AutoUpdateChannelsandVODActivity.this, getResources().getString(R.string.player_setting_error), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_back_playerselection:

                this.finish();

                break;
        }

    }
}

