package rus.voiceassistant

import android.app.Application
import com.j256.ormlite.android.apptools.OpenHelperManager
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.table.TableUtils
import rus.voiceassistant.database.DatabaseHelper
import rus.voiceassistant.model.actions.Alarm
import rus.voiceassistant.model.actions.Book
import rus.voiceassistant.model.actions.Notification

/**
 * Created by RUS on 15.04.2016.
 */
class MyApplication : Application() {

    companion object {
        lateinit var databaseHelper: DatabaseHelper
        lateinit var alarmDao: Dao<Alarm, Int>
        lateinit var notificationDao: Dao<Notification, Int>
        lateinit var bookDao: Dao<Book, Int>
    }

    override fun onCreate() {
        super.onCreate()

        databaseHelper = getDatabaseHelper()

        alarmDao = databaseHelper.alarmDAO
        notificationDao = databaseHelper.notificationDAO
        bookDao = databaseHelper.bookDAO

    }

    private fun getDatabaseHelper(): DatabaseHelper {
        return OpenHelperManager.getHelper(this, DatabaseHelper::class.java)
    }

    override fun onTerminate() {
        super.onTerminate()

        OpenHelperManager.releaseHelper()
        //databaseHelper = null
    }

}