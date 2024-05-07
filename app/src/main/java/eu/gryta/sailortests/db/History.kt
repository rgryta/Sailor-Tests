package eu.gryta.sailortests.db

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index

class History : RealmObject {
    @Index
    var date: RealmInstant = RealmInstant.now()

    var points: Int = 0
    var maxPoints: Int = 0
}