package io.bega.servicebase.screen.main;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.otto.Bus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import org.joda.time.DateTime;

import flow.Backstack;
import flow.Flow;
import flow.Parcer;
import io.bega.servicebase.R;
import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;
import io.bega.servicebase.analytics.EventTracker;
import io.bega.servicebase.model.UserToken;
import io.bega.servicebase.screen.calendar.CalendarScreen;
import io.bega.servicebase.screen.record.RecordScreen;
import mortar.Blueprint;
import io.bega.servicebase.SharedPreferencesKeys;
import io.bega.servicebase.model.User;
import io.bega.servicebase.repository.JsonSharedPreferencesRepository;
import io.bega.servicebase.screen.home.HomeScreen;
import io.bega.servicebase.screen.splash.SplashScreen;
import io.bega.servicebase.util.flow.FlowOwner;
import io.bega.servicebase.util.logging.Logger;

@Singleton
public class MainPresenter extends FlowOwner<Blueprint, MainView> implements Drawer.OnDrawerItemClickListener, PopupMenu.OnMenuItemClickListener {
	private static final Logger LOG = Logger.getLogger(MainPresenter.class);

	private final JsonSharedPreferencesRepository sharedPreferences;
	private final Bus bus;

	private EventTracker tracker;


	@Inject
	public MainPresenter(Context context, EventTracker eventTracker, Parcer<Object> parcer, JsonSharedPreferencesRepository sharedPreferences, Bus bus) {
		super(context, parcer);
		this.sharedPreferences = sharedPreferences;
		this.bus = bus;
		this.tracker = eventTracker;
	}

	@Override
	public void onLoad(Bundle savedInstanceState) {
		LOG.debug("Registering with otto to receive bus events");
		bus.register(this);
		tracker.track("start application serradell");
		super.onLoad(savedInstanceState);
	}

	@Override
	public void dropView(MainView view) {
		LOG.debug("Unregistering with otto");
		bus.unregister(this);
		tracker.track("end application serradell");
		super.dropView(view);
	}

	@Override
	protected Blueprint getFirstScreen() {
		if (userTokenValid()) {
			return new HomeScreen();
		} else {
			LOG.warn("No user account found, redirecting to splash screen");
			return new SplashScreen();
		}
	}

	protected boolean userTokenValid() {
		UserToken token = sharedPreferences.getObject(SharedPreferencesKeys.USER_TOKEN, UserToken.class);

		if (token == null) {
			return false;
		}

		return token.isValid();
	}

	protected boolean userAccountExists() {
		return sharedPreferences.getObject(SharedPreferencesKeys.USER_ACCOUNT, User.class) != null;
	}


	public void getProfiles()
	{

	}

	@Override
	public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
		if (drawerItem instanceof SecondaryDrawerItem) {

			SecondaryDrawerItem secondaryDrawerItem = (SecondaryDrawerItem) drawerItem;
            LOG.info("MAIN", secondaryDrawerItem.getName().getText());
			return false;
		}

		if (drawerItem instanceof PrimaryDrawerItem) {
			PrimaryDrawerItem primaryDrawerItem = (PrimaryDrawerItem) drawerItem;
            LOG.info("MAIN", primaryDrawerItem.getName().getText());
            switch (position)
            {
                case 1:
                {
                    getFirstScreen();
                    break;
                }
                case 2:
                {
                    getFlow().goTo(new RecordScreen());
                    break;
                }
                case 3:
                {
					//String id = getFlow().getBackstack().current().getId();
                    getFlow().goTo(new CalendarScreen());
                    break;
                }
                case 4:
                {

                    break;
                }

            }

			return false;
		}

		return false;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		return false;
	}
}
