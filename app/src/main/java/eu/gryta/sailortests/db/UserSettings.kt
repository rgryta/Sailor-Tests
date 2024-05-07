package eu.gryta.sailortests.db

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class UserSettings : RealmObject {
    @PrimaryKey
    var key: String = ""
    var value: String = ""
}