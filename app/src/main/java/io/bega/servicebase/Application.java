package io.bega.servicebase;

import android.content.Context;
import android.os.Parcel;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;

import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.okhttp.OkHttpStack;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import io.bega.servicebase.BuildConfig;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import mortar.Mortar;
import dagger.ObjectGraph;
import mortar.MortarScope;
import io.bega.servicebase.util.logging.Logger;

public class Application extends android.app.Application {
	private static final Logger LOG = Logger.getLogger(Application.class);

	protected MortarScope rootScope;

    protected ObjectGraph objectGraph;

	@Override
	public void onCreate() {
        super.onCreate();

        LOG.info("Starting application");

        LOG.info("Add crash panel");
       // CustomActivityOnCrash.install(this);

        LOG.debug("Initialising application object graph...");
        objectGraph = ObjectGraph.create(new ApplicationModule(this));

        // Eagerly validate development builds (too slow for production).
        LOG.debug("Creating root Mortar scope...");

        initRealmConfiguration();
        rootScope = Mortar.createRootScope(BuildConfig.DEBUG, objectGraph);
        if (BuildConfig.DEBUG)
        {
            LeakCanary.install(this);
        }

        // Upload Service
        UploadService.NAMESPACE =  BuildConfig.APPLICATION_ID;
        UploadService.HTTP_STACK = new OkHttpStack();
    }

    public void Inject(Object object)
    {
        objectGraph.inject(object);
    }

    private void initRealmConfiguration() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
	public Object getSystemService(String name) {
		if (Mortar.isScopeSystemService(name)) {
			return rootScope;
		}
		return super.getSystemService(name);
	}
}
