package io.bega.servicebase.screen.main;

import static android.content.Intent.*;
import static com.atomicleopard.expressive.Expressive.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

//import com.facebook.FacebookSdk;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.squareup.otto.Bus;

import io.bega.servicebase.R;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.actionbar.MenuItemSelectionHandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Bind;
import flow.Flow;
import io.bega.servicebase.activity.WindowOwner;
import io.bega.servicebase.drawerItems.CustomPrimaryDrawerItem;
import io.bega.servicebase.drawerItems.CustomUrlPrimaryDrawerItem;
import io.bega.servicebase.drawerItems.OverflowMenuDrawerItem;
import io.bega.servicebase.model.event.MenuMessageBusEvent;
import mortar.Mortar;
import mortar.MortarActivityScope;
import mortar.MortarScope;

public class MainActivity extends AppCompatActivity implements ActionBarOwner.View, WindowOwner.Activity {

    private static final int PROFILE_SETTING = 1;

    @Inject
    MixpanelAPI mixpanel;
    @Inject
    ActionBarOwner actionBarOwner;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    MainPresenter mainPresenter;
    @Inject
    Bus bus;

    ActionBarConfig lastActionBarConfig;

    @Bind(R.id.app_toolbar)
    Toolbar actionBarToolbar;
    @Bind(R.id.container)
    MainView mainView;

    /*
    Drawer support
     */
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private Bundle bundle;
    private Map<String, IProfile> profiles = new HashMap<String, IProfile>();

    private IProfile profile;
    private IProfile profile2;
    private IProfile profile3;
    private IProfile profile4;
    private IProfile profile5;

    /*
    End drawer support
     */


    private MortarActivityScope activityScope;
    private Flow mainFlow;
    private Map<Integer, MenuItemSelectionHandler> menuItemSelectionHandlers;

    @Override
    public Object getSystemService(String name) {
        if (Mortar.isScopeSystemService(name)) {
            return activityScope;
        }
        return super.getSystemService(name);
    }

    /**
     * Inform the view about back events.
     */
    @Override
    public void onBackPressed() {
        // Give the view a chance to handle going back. If it declines the honor, let super do its thing.
        if (!mainFlow.goBack()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        //menu.clear();Google


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);
        if (lastActionBarConfig != null && lastActionBarConfig.getMenuIdResource() != 0)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(lastActionBarConfig.getMenuIdResource(), menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Inform the view about up events.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MenuItemSelectionHandler action = menuItemSelectionHandlers.get(item.getItemId());
        if (action != null) {
            return action.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //AppEventsLogger.deactivateApp(this);
    }

    public Bus getBus()
    {
        return bus;
    }

    @Override
    public void setActionBarConfig(ActionBarConfig config) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
                return;
        }

        String title = config.getTitle();
        actionBar.setTitle(title != null ? title : getString(R.string.app_name));

        if (config.isVisible() && !actionBar.isShowing()) {
            actionBar.show();
            // since actionbar is in overlay mode, set the container padding to compensate
            mainView.setPadding(0, getActionBarHeight(), 0, 0);
        } else if (!config.isVisible() && actionBar.isShowing()) {
            actionBar.hide();

            // remove padding so we get full bleed when action bar is hidden
            mainView.setPadding(0, 0, 0, 0);
        }
        else
        {
            // hack for the first time.
            mainView.setPadding(0, getActionBarHeight(), 0, 0);
        }

        if (config.isVisible() && config.isDrawerLayoutEnabled()) {
            setDrawerLayout(actionBarToolbar);
        }

        if (lastActionBarConfig != null)
        {
                if (config.getMenuIdResource() != 0 && config.getMenuIdResource() != lastActionBarConfig.getMenuIdResource())
                {
                    lastActionBarConfig = config;
                    supportInvalidateOptionsMenu();
                }
        }
        else
        {
            if (config.getMenuIdResource() != 0)
            {
                lastActionBarConfig = config;
                supportInvalidateOptionsMenu();
            }
        }

        actionBar.setDisplayHomeAsUpEnabled(config.isHomeAsUpEnabled());
    }


    @Override
    public Context getMortarContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isWrongInstance()) {
            finish();
            return;
        }

        //FacebookSdk.sdkInitialize(this);
        initActivityScope(savedInstanceState);
        inflateViewContainerLayout();
        configureActionBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // activityScope may be null in case isWrongInstance() returned true in onCreate()
        if (isFinishing() && activityScope != null) {
            mixpanel.flush();
            actionBarOwner.dropView(this);
            MortarScope parentScope = Mortar.getScope(getApplication());
            parentScope.destroyChild(activityScope);
            activityScope = null;
        }
    }

    public boolean isTablet() {

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches >= 6.5) {
            return true;
            // 6.5inch device or bigger
        } else {
            return false;
            // smaller device
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        activityScope.onSaveInstanceState(outState);
    }

    /**
     * Dev tools and the play store (and others?) launch with a different intent, and so
     * lead to a redundant instance of this activity being spawned. <a
     * href="http://stackoverflow.com/questions/17702202/find-out-whether-the-current-activity-will-be-task-root-eventually-after-pendin"
     * >Details</a>.
     */
    private boolean isWrongInstance() {
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            boolean isMainAction = intent.getAction() != null && intent.getAction().equals(ACTION_MAIN);
            return intent.hasCategory(CATEGORY_LAUNCHER) && isMainAction;
        }
        return false;
    }

    /**
     * Configure the app activity bar
     */
    private void configureActionBar() {
        setSupportActionBar(actionBarToolbar);

        menuItemSelectionHandlers = map(
                android.R.id.home, new UpSelectionHandler(),
                R.id.action_calendar, new CalendarSelectionHandler(),
                R.id.action_appointment_refresh, new RefreshSelectionHandler(),
                R.id.action_appointment_today, new TodaySelectionHandler()
                //R.id.action_environment, new SwitchEnvironmentSelectionHandler()
        );

        actionBarOwner.takeView(this);
    }

    /**
     * Inflate the view container layout and inject our view components
     */
    private void inflateViewContainerLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainFlow = mainView.getFlow();
    }

    /**
     * Set the drawerlayout
     */
    private void setDrawerLayout(Toolbar toolbar) {
        //save our header or result
        profile = new ProfileDrawerItem().withName("Industrial Prueba").withEmail("industrial.demo@gmail.com").withIcon(getResources().getDrawable(R.drawable.user_icon));
        /*profile2 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile2)).withIdentifier(2);
        profile3 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile3));
        profile4 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile4)).withIdentifier(4);
        profile5 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
        */
        if (result != null) {
            // refresh
            return;
        }

        // Create the AccountHeader
        buildHeader(false, bundle);
        //Create the drawer

        result = new DrawerBuilder()

                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home)
                                .withOnDrawerItemClickListener(
                                        mainPresenter
                                ),
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_menu_drawer_appointment)
                                .withDescription(R.string.drawer_item_menu_drawer_appointment_desc)
                                .withIcon(FontAwesome.Icon.faw_list)
                                .withOnDrawerItemClickListener(
                                        mainPresenter
                                ),

                        //here we use a customPrimaryDrawerItem we defined in our sample app
                        //this custom DrawerItem extends the PrimaryDrawerItem so it just overwrites some methods
                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_menu_drawer_calendar)
                                .withDescription(R.string.drawer_item_menu_drawer_calendar_desc)
                                .withOnDrawerItemClickListener(
                                        mainPresenter
                                ).withIcon(FontAwesome.Icon.faw_calendar),
                      /*  new CustomPrimaryDrawerItem()
                                .withBackgroundRes(R.color.accent)
                                .withName(R.string.drawer_item_free_play)
                                .withIcon(FontAwesome.Icon.faw_gamepad),*/

                        new PrimaryDrawerItem()
                                .withName(R.string.drawer_item_menu_drawer_resume)
                                .withDescription(R.string.drawer_item_menu_drawer_resume_desc)
                                .withOnDrawerItemClickListener(
                                        mainPresenter
                                )
                                .withIcon(FontAwesome.Icon.faw_calculator)
                       /* new CustomUrlPrimaryDrawerItem().withName(R.string.drawer_item_fragment_drawer).withDescription(R.string.drawer_item_fragment_drawer_desc).withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460"),
                        new SectionDrawerItem().withName(R.string.drawer_item_section_header),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cart_plus),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_database).withEnabled(false),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withSelectedIconColor(Color.RED).withIconTintingEnabled(true).withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus_one).actionBar().paddingDp(5).colorRes(R.color.material_drawer_dark_primary_text)).withTag("Bullhorn"),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false) */
                ) // add the items we want to use with our Drawer
                .withOnDrawerNavigationListener(new Drawer.OnDrawerNavigationListener() {
                    @Override
                    public boolean onNavigationClickListener(View clickedView) {
                        //this method is only called if the Arrow icon is shown. The hamburger is automatically managed by the MaterialDrawer
                        //if the back arrow is shown. close the activity
                        MainActivity.this.finish();
                        //return true if we have consumed the event
                        return true;
                    }
                })
                .addStickyDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog)
                                .withOnDrawerItemClickListener(mainPresenter)
                                .withIdentifier(10),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_open_source)
                                .withOnDrawerItemClickListener(mainPresenter)
                                .withIcon(FontAwesome.Icon.faw_github),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_logout)
                                .withIcon(FontAwesome.Icon.faw_sign_out)
                                .withOnDrawerItemClickListener(mainPresenter)
                )
                .withSavedInstance(bundle)
                .build();
        DrawerLayout drawerLayout = result.getDrawerLayout();
        // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

    }

    /**
     * small helper method to reuse the logic to build the AccountHeader
     * this will be used to replace the header of the drawer with a compact/normal header
     *
     * @param compact
     * @param savedInstanceState
     */
    private void buildHeader(boolean compact, Bundle savedInstanceState) {
        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withCompactStyle(compact)
                .addProfiles(
                        profile,

                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_fast_forward).actionBar().paddingDp(5).colorRes(R.color.material_drawer_dark_primary_text)).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman").withEmail("batman@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile5));
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        // TODO buscar el metodo para descargarse los usuarios.
        // headerResult.setProfiles(profiles.values().toArray());
    }

    /**
     * Initalise the root activity Mortar scope
     */
    private void initActivityScope(Bundle savedInstanceState) {
        MortarScope parentScope = Mortar.getScope(getApplication());
        activityScope = Mortar.requireActivityScope(parentScope, new MainScreen());
        Mortar.inject(this, this);
        activityScope.onCreate(savedInstanceState);
    }

    private int getActionBarHeight() {
        TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int height = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return height;
    }

    @Override
    public void addFlagsToWindow(int flags) {
        getWindow().addFlags(flags);
    }


    private class UpSelectionHandler implements MenuItemSelectionHandler {
        @Override
        public boolean execute() {
            return mainFlow.goUp();
        }
    }


    private class CalendarSelectionHandler implements MenuItemSelectionHandler {
        @Override
        public boolean execute() {
            bus.post(new MenuMessageBusEvent(0xFF, null));
            return true;
        }
    }

    private class RefreshSelectionHandler implements MenuItemSelectionHandler {
        @Override
        public boolean execute() {
            bus.post(new MenuMessageBusEvent(0xFE, null));
            return true;
        }
    }

    private class TodaySelectionHandler implements MenuItemSelectionHandler {
        @Override
        public boolean execute() {
            bus.post(new MenuMessageBusEvent(0xFD, null));
            return true;
        }
    }



    @Override
    protected void attachBaseContext(Context baseContext) {
        // baseContext = Flow.configure(baseContext, this).install();
        super.attachBaseContext(baseContext);
    }

    private class SwitchEnvironmentSelectionHandler implements MenuItemSelectionHandler {
        @Override
        public boolean execute() {
      /*new BottomSheet.Builder(MainActivity.this)
        .title("Choose Environment")
        .sheet(R.menu.menu_environment)
        .listener(new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            sharedPreferences.edit().putInt(SharedPreferencesKeys.ENVIRONMENT, which).apply();
            restartApp();  // required to reinitialise object graph
          }
        })
        .build()
        .show(); */
            return true;
        }

        private void restartApp() {
            Context context = MainActivity.this;
            Intent mStartActivity = new Intent(context, MainActivity.class);

            int mPendingIntentId = 123456;
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);

            System.exit(0);  // die die die!
        }
    }
}
