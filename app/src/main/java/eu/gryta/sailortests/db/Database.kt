package eu.gryta.sailortests.db

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object Database {
    fun initUserSettings(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                UserSettings::class
            )
        ).name("usersettings").schemaVersion(1).deleteRealmIfMigrationNeeded().build()
        return Realm.open(config)
    }

    fun initHistory(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                History::class
            )
        ).name("history").schemaVersion(1).deleteRealmIfMigrationNeeded().build()
        return Realm.open(config)
    }
}