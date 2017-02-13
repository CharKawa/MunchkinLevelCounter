package com.g_art.munchkinlevelcounter.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.analytics.Analytics;
import com.g_art.munchkinlevelcounter.model.DaoMaster;
import com.g_art.munchkinlevelcounter.model.DaoSession;
import com.g_art.munchkinlevelcounter.util.SettingsMigration;
import com.g_art.munchkinlevelcounter.util.db.EntityManager;
import com.g_art.munchkinlevelcounter.util.db.MigrationHelper;

import org.greenrobot.greendao.query.QueryBuilder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * MunchkinLevelCounter
 * Created by G_Art on 29/10/2014.
 */
public class MyApplication extends Application {

	private DaoMaster.DevOpenHelper helper;
	private SQLiteDatabase db;
	private DaoMaster daoMaster;

	@Override
	public void onCreate() {
		super.onCreate();
		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath(getString(R.string.font_path))
				.setFontAttrId(R.attr.fontPath)
				.build()
		);
		Analytics.init(this);

		SettingsMigration.startMigration(getBaseContext());
		QueryBuilder.LOG_SQL = true;
		QueryBuilder.LOG_VALUES = true;

		// do this once, for example in your Application class
		helper = new DaoMaster.DevOpenHelper(this, "level-counter-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		final DaoSession daoSession = daoMaster.newSession();
		EntityManager.getInstance().init(daoSession);

		MigrationHelper.migrateFromSharedPrefsToDatabase(getBaseContext());
	}
}
