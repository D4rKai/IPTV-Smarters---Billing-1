package com.gehostingv2.gesostingv2iptvbilling.view.nstplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gehostingv2.gesostingv2iptvbilling.R;
import com.gehostingv2.gesostingv2iptvbilling.miscelleneious.common.AppConst;
import com.gehostingv2.gesostingv2iptvbilling.model.database.LiveStreamCategoryIdDBModel;
import com.gehostingv2.gesostingv2iptvbilling.model.database.LiveStreamDBHandler;
import com.gehostingv2.gesostingv2iptvbilling.model.database.LiveStreamsDBModel;
import com.gehostingv2.gesostingv2iptvbilling.model.database.PasswordStatusDBModel;
import com.gehostingv2.gesostingv2iptvbilling.view.adapter.SearchableAdapter;

import java.util.ArrayList;


public class NSTPlayerVodActivity extends Activity implements View.OnClickListener {

    public Context context;
    public String mFilePath;
    public Activity activity;
    public String scaleType;
    public View playButton;
    public View pauseButton;
    public View forwardButton;
    public View rewindButton;
    public View nextButton;
    public View prevButton;
    public View channelListButton;
    public EditText et_search;
    //    public LinearLayout ll_root;
    public RelativeLayout rl_middle;
    public ListView listChannels;
    public ArrayList<LiveStreamsDBModel> allMovies;
    private ArrayList<LiveStreamsDBModel> filterList;
    public boolean fullScreenOnly = true;
    public long defaultRetryTime = 5 * 1000;
    public String title;
    public String url;
    public boolean showNavIcon = true;
    public int currentWindowIndex = 0;
    NSTPlayerVod player;
    LiveStreamDBHandler liveStreamDBHandler;
    private SharedPreferences loginPreferencesSharedPref;
    SearchableAdapter adapter;
    SearchableAdapter mSearchableAdapter;

    public TextView tv_categories_view;
    private ArrayList<LiveStreamCategoryIdDBModel> allMoviesCategories;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailAvailable;
    public LinearLayout ll_categories_view;
    private int currentCategoryIndex = 0;
    private AppCompatImageView btn_cat_back;
    private AppCompatImageView btn_cat_forward;






    private ArrayList<String> listPassword = new ArrayList<>();
    private ArrayList<PasswordStatusDBModel> categoryWithPasword;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlcked;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetailUnlckedDetail;
    private ArrayList<LiveStreamCategoryIdDBModel> moviesListDetailAvailable;
    private ArrayList<LiveStreamCategoryIdDBModel> liveListDetail;
    private ArrayList<LiveStreamsDBModel> movieListDetailUnlcked;
    private ArrayList<LiveStreamsDBModel> movieListDetailUnlckedDetail;



//    public boolean clicked;
    /**
     * play video
     *
     * @param context
     * @param url     url,title
     */
    public static void play(Activity context, String... url) {
        Intent intent = new Intent(context, NSTPlayerVodActivity.class);
        intent.putExtra("url", url[0]);
        if (url.length > 1) {
            intent.putExtra("title", url[1]);
        }
        context.startActivity(intent);
    }

    public static Config configPlayer(Activity activity) {
        return new Config(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nst_player_vod_activity);





        liveListDetailUnlcked = new ArrayList<LiveStreamCategoryIdDBModel>();
        liveListDetailUnlckedDetail = new ArrayList<LiveStreamCategoryIdDBModel>();
        liveListDetailAvailable = new ArrayList<LiveStreamCategoryIdDBModel>();
        liveListDetail = new ArrayList<LiveStreamCategoryIdDBModel>();




//        Config config = getIntent().getParcelableExtra("config");
//        if (config == null || TextUtils.isEmpty(config.url)) {
//            Toast.makeText(this, R.string.giraffe_player_url_empty, Toast.LENGTH_SHORT).show();
//        } else {

        context = this;
        loginPreferencesSharedPref = getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE_IPTV, MODE_PRIVATE);
        String username = loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_USERNAME_IPTV, "");
        final String password = loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_PASSWORD_IPTV, "");
        String allowedFormat = loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_ALLOWED_FORMAT, "");
        String serverUrl = loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_URL, "");
        String serverPort = loginPreferencesSharedPref.getString(AppConst.LOGIN_PREF_SERVER_PORT, "");
        int opened_video_id = getIntent().getIntExtra("VIDEO_ID", 0);
        int currentWindowIndex = getIntent().getIntExtra("VIDEO_NUM", 0);
        String videoTitle = getIntent().getStringExtra("VIDEO_TITLE");
        String videoExtension = getIntent().getStringExtra("EXTENSION_TYPE");
//        mFilePath = "http://iptv.dguk.co.uk/timeshift/test1234/4321test/2/2017-10-27:05-00/1313.ts";
        mFilePath = "http://" + serverUrl + ":" + serverPort + "/movie/" + username + "/" + password + "/";
        liveStreamDBHandler = new LiveStreamDBHandler(this);
//        allMovies =
//                liveStreamDBHandler.getAllLiveStreasWithCategoryId("0", "movie");




        categoryWithPasword = new ArrayList<PasswordStatusDBModel>();
        movieListDetailUnlcked = new ArrayList<LiveStreamsDBModel>();
        movieListDetailUnlckedDetail = new ArrayList<LiveStreamsDBModel>();
//        moviesListDetailAvailable1 = new ArrayList<LiveStreamsDBModel>();
        ArrayList<LiveStreamsDBModel> vodAvailable = liveStreamDBHandler.getAllLiveStreasWithCategoryId("0", "movie");
        int parentalStatusCount = liveStreamDBHandler.getParentalStatusCount();
        if (parentalStatusCount > 0 && vodAvailable != null) {
            listPassword = getPasswordSetCategories();
            if (listPassword != null) {
                movieListDetailUnlckedDetail = getUnlockedCategoriesAll(vodAvailable,
                        listPassword);
            }
            allMovies = movieListDetailUnlckedDetail;
        } else {
            allMovies = vodAvailable;
        }









        int positionToSelect = 0;
        positionToSelect = getIndexOfMovies(allMovies,currentWindowIndex);
        currentWindowIndex = positionToSelect;
        getIntent().putExtra("VIDEO_NUM", currentWindowIndex);


        player = new NSTPlayerVod(this);
        player.setCurrentWindowIndex(currentWindowIndex);

//        player.setTitle(videoTitle + " -- " + Integer.toString(opened_video_id) + " -- " + currentWindowIndex);
        player.setTitle(videoTitle);
        player.setDefaultRetryTime(defaultRetryTime);
        player.setFullScreenOnly(fullScreenOnly);
        player.setScaleType(TextUtils.isEmpty(scaleType) ? NSTPlayerVod.SCALETYPE_FITPARENT : scaleType);
//            player.setTitle(TextUtils.isEmpty(Integer.toString(opened_video_id)) ? "" : Integer.toString(opened_video_id));
        player.setShowNavIcon(showNavIcon);
        player.showAll();
//        player.tryFullScreen(true);
        player.play(mFilePath, opened_video_id ,videoExtension);
        player.hideSystemUi();

        findViewById(R.id.exo_next).setOnClickListener(this);
        findViewById(R.id.exo_prev).setOnClickListener(this);
        listChannels = (ListView) findViewById(R.id.lv_ch);
        et_search = (EditText) findViewById(R.id.et_search);
        ll_categories_view = (LinearLayout) findViewById(R.id.ll_categories_view);


        playButton = findViewById(R.id.exo_play);

        if (playButton != null) {
            playButton.setOnClickListener(this);
        }
        pauseButton = findViewById(R.id.exo_pause);
        if (pauseButton != null) {
            pauseButton.setOnClickListener(this);
        }
//        playerStartIconsUpdate();

        forwardButton = findViewById(R.id.exo_ffwd);
        if (forwardButton != null) {
            forwardButton.setOnClickListener(this);
        }
        rewindButton = findViewById(R.id.exo_rew);
        if (rewindButton != null) {
            rewindButton.setOnClickListener(this);
        }
        prevButton = findViewById(R.id.exo_prev);
        if (prevButton != null) {
            prevButton.setOnClickListener(this);
        }
        nextButton = findViewById(R.id.exo_next);
        if (nextButton != null) {
            nextButton.setOnClickListener(this);
        }
        channelListButton = findViewById(R.id.btn_list);
        if (channelListButton != null) {
            channelListButton.setOnClickListener(this);
        }

        tv_categories_view = (TextView) findViewById(R.id.tv_categories_view);
        btn_cat_back = (AppCompatImageView) findViewById(R.id.btn_category_back);
        btn_cat_forward = (AppCompatImageView) findViewById(R.id.btn_category_forward);
        btn_cat_back.setOnClickListener(this);
        btn_cat_forward.setOnClickListener(this);
        rl_middle = (RelativeLayout) findViewById(R.id.middle);





        tv_categories_view.setText(getResources().getString(R.string.all));



        allMoviesCategories = liveStreamDBHandler.getAllMovieCategories();
        LiveStreamCategoryIdDBModel liveStream = new LiveStreamCategoryIdDBModel();
        liveStream.setLiveStreamCategoryID("0");
        liveStream.setLiveStreamCategoryName(getResources().getString(R.string.all));



        int parentalStatusCount1 = liveStreamDBHandler.getParentalStatusCount();
        if(parentalStatusCount1>0 && allMoviesCategories!=null){
            listPassword = getPasswordSetCategories();
            liveListDetailUnlckedDetail = getUnlockedCategories(allMoviesCategories,  //liveListDetail
                    listPassword);
            liveListDetailUnlcked.add(0, liveStream);
            liveListDetailAvailable = liveListDetailUnlckedDetail;

        }else {
            liveListDetail.add(0, liveStream);
            liveListDetailAvailable = allMoviesCategories;
        }

        if(allMoviesCategories!=null) {
//            allMoviesCategories.add(0, liveStream);
            liveListDetailAvailable = liveListDetailAvailable;
        }


        if (allMovies != null){
            setVodListAdapter(allMovies);
        }





//        if (rl_middle != null && listChannels.getVisibility()==View.VISIBLE) {
//                rl_middle.setOnClickListener(this);
//        }

//        if(rl_middle!=null && listChannels!=null && listChannels.getVisibility()==View.VISIBLE) {
//            rl_middle.setOnClickListener(new View.OnClickListener(){
//                                           public void onClick(View v){
//                                                 et_search.setVisibility(View.GONE);
//                                                 listChannels.setVisibility(View.GONE);
//                                                rl_middle.setClickable(false);
//                                               rl_middle.setOnClickListener(null);
//                                           }
//
//                }
//            );
//            rl_middle.setOnClickListener(this);
//        }else if(rl_middle!=null){
//            rl_middle.setClickable(false);
//            rl_middle.setOnClickListener(null);
//        }

//        case R.id.middle:
//        if(rl_middle!=null && listChannels!=null && listChannels.getVisibility()==View.VISIBLE) {
////                    player.setShowNavIcon(true);
//            et_search.setVisibility(View.GONE);
//            listChannels.setVisibility(View.GONE);
////                    toggleView(listChannels);
//        }
//        break;
//        ll_root  = (LinearLayout) findViewById(R.id.root);
//        if (ll_root != null) {
//            ll_root.setOnClickListener(this);
//        }



//        List<String> channelList = new ArrayList<String>();

//        adapter = new ArrayAdapter<String>(this,
//                R.layout.channel_list, channelList);







//        }
    }





    private ArrayList<LiveStreamCategoryIdDBModel> getUnlockedCategories(ArrayList<LiveStreamCategoryIdDBModel> liveListDetail,

                                                                         ArrayList<String> listPassword) {
        for(LiveStreamCategoryIdDBModel user1 : liveListDetail)
        {
            boolean flag = false;
            for(String user2 : listPassword)
            {
                if(user1.getLiveStreamCategoryID().equals(user2))
                {
                    flag = true;
                    break;
                }
            }
            if(flag == false)
            {
                liveListDetailUnlcked.add(user1);
            }
        }
        return liveListDetailUnlcked;
    }

    private ArrayList<String> getPasswordSetCategories() {
        categoryWithPasword =
                liveStreamDBHandler.getAllPasswordStatus();
        if(categoryWithPasword!=null) {
            for (PasswordStatusDBModel listItemLocked : categoryWithPasword) {
                if (listItemLocked.getPasswordStatus().equals(AppConst.PASSWORD_SET)) {
                    listPassword.add(listItemLocked.getPasswordStatusCategoryId());
                }
            }
        }
        return listPassword;

    }








    private ArrayList<LiveStreamsDBModel> getUnlockedCategoriesAll(ArrayList<LiveStreamsDBModel> liveListDetail,

                                                                   ArrayList<String> listPassword) {
        for (LiveStreamsDBModel user1 : liveListDetail) {
            boolean flag = false;
            for (String user2 : listPassword) {
                if (user1.getCategoryId().equals(user2)) {
                    flag = true;
                    break;
                }
            }
            if (flag == false) {
                movieListDetailUnlcked.add(user1);
            }
        }
        return movieListDetailUnlcked;
    }

    public void setVodListAdapter(final ArrayList<LiveStreamsDBModel> allMovies){
        int video_num = getIntent().getIntExtra("VIDEO_NUM", 0);
        int positionToSelect = video_num;
        adapter = new SearchableAdapter(this,allMovies);
//            ListView listView = (ListView)
//                    findViewById(R.id.lv_ch);
        // Assign adapter to ListView
        if (listChannels != null){
            listChannels.setAdapter(adapter);
            listChannels.setSelection(positionToSelect);
//            listChannels.requestFocus();
//            listChannels.setFocusable(true);
//            listChannels.getChildAt(0).setFocusable(true);


//            int count = listChannels.getChildCount();
//            for (int i = 0; i <= listChannels.getLastVisiblePosition() - listChannels.getFirstVisiblePosition(); i++){
//
//            }

//            for(int j=0; j<count; j++){
//
//            }
//            View abc = listChannels.getFocusedChild();
//            abc.setBackgroundResource(R.drawable.bg_key);            //listChannels.setSelector(R.drawable.selector_search_fields);
//            listChannels.setAdapter(adapter);
//            listChannels.setTextFilterEnabled(true);
            listChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
//                Utils.showToast(context, "Testing: "+position);
//                String a = (String) parent.getAdapter().getItem(position);
                    view.setSelected(true);
                    ArrayList<LiveStreamsDBModel> filteredData = adapter.getFilteredData();
                    if(filteredData!=null){
                        int num = Integer.parseInt(filteredData.get(position).getNum());
                        num = getIndexOfMovies(allMovies,num);
                        player.setCurrentWindowIndex(num);

//                        player.setTitle(filteredData.get(position).getName() + " -- " + filteredData.get(position).getStreamId() + " -- " + num);
                        player.setTitle(filteredData.get(position).getName());
                        player.play(mFilePath, Integer.parseInt(filteredData.get(position).getStreamId()),filteredData.get(position).getContaiinerExtension());
                    }else{
                        //                    player.setCurrentWindowIndex(position + 1);
                        int num = Integer.parseInt(allMovies.get(position).getNum());
                        num = getIndexOfMovies(allMovies,num);
                        player.setCurrentWindowIndex(num);
//                        player.setTitle(allMovies.get(position).getName() + " -- " + allMovies.get(position).getStreamId() + " -- " + num);
                        player.setTitle(allMovies.get(position).getName());
                        player.play(mFilePath, Integer.parseInt(allMovies.get(position).getStreamId()),allMovies.get(position).getContaiinerExtension());

                    }
                    listChannels.setVisibility(View.GONE);
                    et_search.setVisibility(View.GONE);
                    ll_categories_view.setVisibility(View.GONE);


                }
            });

            et_search.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    if(adapter!=null) {
                        adapter.getFilter().filter(cs.toString());
                    }

                    // When user changed the Text
//                    Filter Abc = adapter.getFilter();
//                    Abc.filter(cs);
//                    Utils.showToast(context, "Testing: "+ Abc);

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });


        }
    }

    public void backbutton(){
        if(currentCategoryIndex!=0) {
            currentCategoryIndex = currentCategoryIndex - 1;
        }

        if (liveListDetailAvailable != null && liveListDetailAvailable.size() > 0 && currentCategoryIndex < liveListDetailAvailable.size()) {
            String currentCatID = liveListDetailAvailable.get(currentCategoryIndex).getLiveStreamCategoryID();
            String currentCatName = liveListDetailAvailable.get(currentCategoryIndex).getLiveStreamCategoryName();
            if (liveStreamDBHandler != null) {
//                allMovies =
//                        liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "movie");





                ArrayList<LiveStreamsDBModel> vodAvailable = liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "movie");

                categoryWithPasword = new ArrayList<PasswordStatusDBModel>();
                movieListDetailUnlcked = new ArrayList<LiveStreamsDBModel>();
                movieListDetailUnlckedDetail = new ArrayList<LiveStreamsDBModel>();
//                moviesListDetailAvailable1 = new ArrayList<LiveStreamsDBModel>();

                int parentalStatusCount = liveStreamDBHandler.getParentalStatusCount();
                if (parentalStatusCount > 0 && vodAvailable != null) {
                    listPassword = getPasswordSetCategories();
                    if (listPassword != null) {
                        movieListDetailUnlckedDetail = getUnlockedCategoriesAll(vodAvailable,
                                listPassword);
                    }
                    allMovies = movieListDetailUnlckedDetail;
                } else {
                    allMovies = vodAvailable;
                }
            }
            if (tv_categories_view != null) {
                tv_categories_view.setText(currentCatName);
            }
            setVodListAdapter(allMovies);
        }

    }
    public void nextbutton(){
        if(currentCategoryIndex!=liveListDetailAvailable.size()-1) {
            currentCategoryIndex = currentCategoryIndex + 1;
        }

        if (liveListDetailAvailable != null && liveListDetailAvailable.size() > 0 && currentCategoryIndex < liveListDetailAvailable.size()) {
            String currentCatID = liveListDetailAvailable.get(currentCategoryIndex).getLiveStreamCategoryID();
            String currentCatName = liveListDetailAvailable.get(currentCategoryIndex).getLiveStreamCategoryName();
            if (liveStreamDBHandler != null) {
//                allMovies =
//                        liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "movie");



                ArrayList<LiveStreamsDBModel> vodAvailable = liveStreamDBHandler.getAllLiveStreasWithCategoryId(currentCatID, "movie");

                categoryWithPasword = new ArrayList<PasswordStatusDBModel>();
                movieListDetailUnlcked = new ArrayList<LiveStreamsDBModel>();
                movieListDetailUnlckedDetail = new ArrayList<LiveStreamsDBModel>();
//                moviesListDetailAvailable1 = new ArrayList<LiveStreamsDBModel>();

                int parentalStatusCount = liveStreamDBHandler.getParentalStatusCount();
                if (parentalStatusCount > 0 && vodAvailable != null) {
                    listPassword = getPasswordSetCategories();
                    if (listPassword != null) {
                        movieListDetailUnlckedDetail = getUnlockedCategoriesAll(vodAvailable,
                                listPassword);
                    }
                    allMovies = movieListDetailUnlckedDetail;
                } else {
                    allMovies = vodAvailable;
                }

            }
            if (tv_categories_view != null) {
                tv_categories_view.setText(currentCatName);
            }
            setVodListAdapter(allMovies);
        }

    }

    public int getIndexOfMovies(ArrayList<LiveStreamsDBModel> allMovies, int num) {
        int positionToSelect = 0;
        for (int i = 0; i < allMovies.size(); i++) {
            if (Integer.parseInt(allMovies.get(i).getNum()) == num) {
                positionToSelect = i;
                break;
            }
//            channelList.add(allMovies.get(i).getName());
        }
        return positionToSelect;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.hideSystemUi();
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }


    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if(listChannels!=null && listChannels.getVisibility() == View.VISIBLE){
//                    listChannels.setFocusable(true);
//                    listChannels.requestFocus();
                }else{
                    showTitleBarAndFooter();
                    findViewById(R.id.exo_prev).performClick();

                }
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                if(listChannels!=null && listChannels.getVisibility() == View.VISIBLE){
//                    listChannels.setFocusable(true);
//                    listChannels.requestFocus();
                }else{
                    showTitleBarAndFooter();
                    findViewById(R.id.exo_next).performClick();
                }
                return true;

            default:
                return super.onKeyUp(keyCode, event);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        final boolean uniqueDown = event.getRepeatCount() == 0;

        switch (keyCode) {

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if(listChannels!=null && listChannels.getVisibility() == View.VISIBLE){
                    if(et_search!=null){
                        et_search.setText("");
                    }
                    backbutton();
                    listChannels.setFocusable(true);
                    listChannels.requestFocus();

                }else{
                    showTitleBarAndFooter();
                    findViewById(R.id.exo_rew).performClick();
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(listChannels!=null && listChannels.getVisibility() == View.VISIBLE){
                    if(et_search!=null){
                        et_search.setText("");
                    }
                    nextbutton();
                    listChannels.setFocusable(true);
                    listChannels.requestFocus();

                }else{
                    showTitleBarAndFooter();
                    findViewById(R.id.exo_ffwd).performClick();
                }
                return true;


            case KeyEvent.KEYCODE_MEDIA_STEP_FORWARD:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                showTitleBarAndFooter();
                findViewById(R.id.exo_ffwd).performClick();
                return true;

            case KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                showTitleBarAndFooter();
                findViewById(R.id.exo_rew).performClick();
                return true;

            case KeyEvent.KEYCODE_CHANNEL_UP:
                showTitleBarAndFooter();
                findViewById(R.id.exo_next).performClick();
                return true;

            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                showTitleBarAndFooter();
                findViewById(R.id.exo_prev).performClick();
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                showTitleBarAndFooter();
                listChannels.setVisibility(View.VISIBLE);
                et_search.setVisibility(View.VISIBLE);
                ll_categories_view.setVisibility(View.VISIBLE);
//                et_search.requestFocus();
                listChannels.setFocusable(true);
                listChannels.requestFocus();
                return true;
            case KeyEvent.KEYCODE_ENTER:
                showTitleBarAndFooter();
                listChannels.setVisibility(View.VISIBLE);
                et_search.setVisibility(View.VISIBLE);
                ll_categories_view.setVisibility(View.VISIBLE);
//                et_search.requestFocus();
                listChannels.setFocusable(true);
                listChannels.requestFocus();
                return true;
            case KeyEvent.KEYCODE_HEADSETHOOK:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_SPACE:
                if (uniqueDown) {
                    showTitleBarAndFooter();
                    player.doPauseResume();
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
                if (uniqueDown && !player.isPlaying()) {
                    showTitleBarAndFooter();
                    player.start();
                    playerStartIconsUpdate();
                }
                return true;
            case KeyEvent.KEYCODE_MEDIA_STOP:
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
                if (uniqueDown && player.isPlaying()) {
                    showTitleBarAndFooter();
                    player.pause();
                    playerPauseIconsUpdate();
                }
                return true;

            default:
                return super.onKeyDown(keyCode, event);
        }
    }





    public void showTitleBarAndFooter(){
        findViewById(R.id.app_video_top_box).setVisibility(View.VISIBLE);
        findViewById(R.id.app_video_bottom_box).setVisibility(View.VISIBLE);
        findViewById(R.id.app_video_currentTime).setVisibility(View.VISIBLE);
        findViewById(R.id.app_video_endTime).setVisibility(View.VISIBLE);
        findViewById(R.id.app_video_seekBar).setVisibility(View.VISIBLE);
        findViewById(R.id.controls).setVisibility(View.VISIBLE);
    }

    public void hideTitleBarAndFooter(){
        findViewById(R.id.app_video_bottom_box).setVisibility(View.GONE);
        findViewById(R.id.app_video_currentTime).setVisibility(View.GONE);
        findViewById(R.id.app_video_endTime).setVisibility(View.GONE);
        findViewById(R.id.app_video_seekBar).setVisibility(View.GONE);
        findViewById(R.id.app_video_top_box).setVisibility(View.GONE);
        findViewById(R.id.controls).setVisibility(View.GONE);
    }


    //    private void doPauseResume() {
//        if (player == null) {
//            return;
//        }
//
//        if (player.isPlaying()) {
//            player.pause();
//            playerPauseIconsUpdate();
//        } else {
//            player.start();
//            playerStartIconsUpdate();
//        }
////        updatePausePlay();
//    }
    private void playerStartIconsUpdate(){
        playButton.setVisibility(View.GONE);
        pauseButton.setVisibility(View.VISIBLE);
    }
    private void playerPauseIconsUpdate(){
        pauseButton.setVisibility(View.GONE);
        playButton.setVisibility(View.VISIBLE);
    }

//    public void updatePausePlay() {
////        if (mRoot == null || mPauseButton == null || mPlayer == null) {
////            return;
////        }
//
//        if (mPlayer.isPlaying()) {
//            mPauseButton.setImageResource(R.drawable.ic_media_pause);
//        } else {
//            mPauseButton.setImageResource(R.drawable.ic_media_play);
//        }
//    }
//    public class Reminder {
//
//        Timer timer;
//
//        public Reminder(int seconds) {
//            timer = new Timer();
//            timer.schedule(new RemindTask(), seconds*1000);
//        }
//
//        class RemindTask extends TimerTask {
//
//            public void run() {
//                if(!clicked){
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//
//                            findViewById(R.id.app_video_top_box).setVisibility(View.GONE);
//                            findViewById(R.id.controls).setVisibility(View.GONE);
//                            System.out.format("Time's up!%n");
//                            timer.cancel(); //Terminate the timer thread
//
//                        }
//
//                    });
//                }
//
//            }
//        }
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exo_play:
                if (player!=null && playButton != null) {
                    player.start();
                    playerStartIconsUpdate();
                }
                break;
            case R.id.exo_pause:
                if (player!=null && pauseButton != null) {
                    player.pause();
                    playerPauseIconsUpdate();
                }
                break;
            case R.id.exo_ffwd:
                if(player!=null){
                    player.forward(0.2f);
                }
                break;
            case R.id.exo_rew:
                if(player!=null){
                    player.forward(-0.2f);
                }
                break;
            case R.id.exo_prev:
                if(player!=null){
                    previous();
                    int indexPrev = player.getCurrentWindowIndex();
                    if (indexPrev <= allMovies.size() - 1) {
//                        player.setTitle(allMovies.get(indexPrev).getName() + " -- " +  allMovies.get(indexPrev).getStreamId() +" -- "+indexPrev);
                        player.setTitle(allMovies.get(indexPrev).getName());
                        player.play(mFilePath, Integer.parseInt(allMovies.get(indexPrev).getStreamId()),allMovies.get(indexPrev).getContaiinerExtension());
                    }
                }
                break;
            case R.id.exo_next:
                if(player!=null){
                    next();
                    int indexNext = player.getCurrentWindowIndex();
                    if (indexNext <= allMovies.size() - 1) {
//                        player.setTitle(allMovies.get(indexNext).getName() + " -- " +  allMovies.get(indexNext).getStreamId() +" -- "+indexNext);
                        player.setTitle(allMovies.get(indexNext).getName());
                        player.play(mFilePath, Integer.parseInt(allMovies.get(indexNext).getStreamId()),allMovies.get(indexNext).getContaiinerExtension());
                    }
                }
                break;
            case R.id.btn_list:
                if(listChannels!=null && et_search!=null && ll_categories_view!=null) {
                    toggleView(listChannels);
                    toggleView(et_search);
                    toggleView(ll_categories_view);
                    listChannels.setFocusable(true);
                    listChannels.requestFocus();
                }
                break;

            case R.id.btn_category_back:
                backbutton();
                break;
            case R.id.btn_category_forward:
                nextbutton();
                break;

//            case R.id.middle:
//                if(listChannels!=null && et_search!=null) {
//                    et_search.setVisibility(View.GONE);
//                    listChannels.setVisibility(View.GONE);
//                }
//                break;

//            case R.id.root:
//                if(ll_root!=null && listChannels!=null) {
//                    ll_root.setVisibility(View.GONE);
//                    toggleView(listChannels);
//                }
//                break;

        }
    }

    public void toggleView(View view){
        if(view.getVisibility()==View.GONE)
            view.setVisibility(View.VISIBLE);
        else if(view.getVisibility()==View.VISIBLE)
            view.setVisibility(View.GONE);
    }

    private void previous(){
        int currentWindowIndex = player.getCurrentWindowIndex();
        if(currentWindowIndex == 0){
            player.setCurrentWindowIndex(allMovies.size() - 1);
            return;
        }
        player.setCurrentWindowIndex(currentWindowIndex-1);
    }
    private void next() {
        int currentWindowIndex = player.getCurrentWindowIndex();
        if(currentWindowIndex == allMovies.size()-1){
            player.setCurrentWindowIndex(0);
            return;
        }
        player.setCurrentWindowIndex(currentWindowIndex+1);
    }

    @Override
    public void onBackPressed() {
        if(listChannels!=null) {
            listChannels.setFocusable(true);
            listChannels.requestFocus();
        }
        if(listChannels!=null && listChannels.getVisibility() == View.VISIBLE){
            listChannels.setVisibility(View.GONE);
            if(et_search!=null && et_search.getVisibility() == View.VISIBLE) {
                et_search.setVisibility(View.GONE);
            }
            if(ll_categories_view!=null && ll_categories_view.getVisibility() == View.VISIBLE) {
                ll_categories_view.setVisibility(View.GONE);
            }

            if(findViewById(R.id.app_video_top_box).getVisibility() == View.VISIBLE) {
                hideTitleBarAndFooter();
            }
            return;
        }
        if(findViewById(R.id.app_video_top_box).getVisibility() == View.VISIBLE) {
            hideTitleBarAndFooter();
            if(listChannels!=null && listChannels.getVisibility() == View.VISIBLE) {
                listChannels.setVisibility(View.GONE);
                if (et_search != null && et_search.getVisibility() == View.VISIBLE) {
                    et_search.setVisibility(View.GONE);
                }
                if(ll_categories_view!=null && ll_categories_view.getVisibility() == View.VISIBLE) {
                    ll_categories_view.setVisibility(View.GONE);
                }
            }
            return;
        }

        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    public static class Config implements Parcelable {

        public static final Parcelable.Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            public Config[] newArray(int size) {
                return new Config[size];
            }
        };
        private static boolean debug=true;
        private Activity activity;
        private String scaleType;
        private boolean fullScreenOnly = true;
        private long defaultRetryTime = 5 * 1000;
        private String title;
        private String url;
        private boolean showNavIcon = true;

        public Config(Activity activity) {
            this.activity = activity;
        }

        private Config(Parcel in) {
            scaleType = in.readString();
            fullScreenOnly = in.readByte() != 0;
            defaultRetryTime = in.readLong();
            title = in.readString();
            url = in.readString();
            showNavIcon = in.readByte() != 0;
        }

        public static boolean isDebug() {
            return debug;
        }

        public  Config debug(boolean debug) {
            Config.debug = debug;
            return this;
        }

        public Config setTitle(String title) {
            this.title = title;
            return this;
        }

        public Config setDefaultRetryTime(long defaultRetryTime) {
            this.defaultRetryTime = defaultRetryTime;
            return this;
        }

        public void play(String url) {
            this.url = url;
            Intent intent = new Intent(activity, NSTPlayerVodActivity.class);
            intent.putExtra("config", this);
            activity.startActivity(intent);
        }

        public Config setScaleType(String scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Config setFullScreenOnly(boolean fullScreenOnly) {
            this.fullScreenOnly = fullScreenOnly;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(scaleType);
            dest.writeByte((byte) (fullScreenOnly ? 1 : 0));
            dest.writeLong(defaultRetryTime);
            dest.writeString(title);
            dest.writeString(url);
            dest.writeByte((byte) (showNavIcon ? 1 : 0));
        }
    }
}
