package eu.gryta.sailortests


import android.app.Application
import android.content.Context
import eu.gryta.sailortests.db.Database

import io.realm.kotlin.Realm

class App : Application() {

    var userSettings: Realm? = null
    var history: Realm? = null

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        userSettings = Database.initUserSettings()
        history = Database.initHistory()
    }

    override fun onTerminate() {
        super.onTerminate()
        userSettings?.close()
        history?.close()
    }


    companion object {
        var instance: App? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}