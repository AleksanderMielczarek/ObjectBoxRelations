package com.github.aleksandermielczarek.objectboxrelations

import android.app.Application
import io.objectbox.Box
import io.objectbox.BoxStore

/**
 * Created by Aleksander Mielczarek on 24.08.2017.
 */
class App : Application() {

    lateinit var boxStore: BoxStore
    lateinit var entityOuterBox: Box<EntityOuter>
    lateinit var entityInnerBox: Box<EntityInner>

    override fun onCreate() {
        super.onCreate()

        boxStore = MyObjectBox.builder()
                .androidContext(this)
                .build()
        entityOuterBox = boxStore.boxFor(EntityOuter::class.java)
        entityInnerBox = boxStore.boxFor(EntityInner::class.java)
    }
}