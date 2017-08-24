package com.github.aleksandermielczarek.objectboxrelations

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.objectbox.rx.RxQuery
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Aleksander Mielczarek on 24.08.2017.
 */
class MainActivity : AppCompatActivity() {

    private val disposables = CompositeDisposable()
    private val entityOuterBox by lazy { (application as App).entityOuterBox }
    private val entityInnerBox by lazy { (application as App).entityInnerBox }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status.text = "DELETE"
        delete()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { status.text = "CREATE" }
                .observeOn(Schedulers.io())
                .andThen(create())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete { status.text = "READ" }
                .observeOn(Schedulers.io())
                .andThen(read())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy { status.text = "ENTITIES: ${it.size}, RELATION: ${it[5].data1} ${it[5].data6.target.data1}" }
                .addTo(disposables)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun delete(): Completable {
        return Completable.fromAction {
            entityOuterBox.removeAll()
            entityInnerBox.removeAll()
        }
    }

    private fun create(): Completable {
        return Flowable.range(0, 1000)
                .map {
                    val inner = EntityInner()
                    inner.data1 = it.toString()
                    inner.data2 = it.toString()
                    inner.data3 = it.toString()
                    inner.data4 = it.toString()
                    inner.data5 = it.toString()
                    val outer = EntityOuter()
                    outer.data1 = it.toString()
                    outer.data1 = it.toString()
                    outer.data2 = it.toString()
                    outer.data3 = it.toString()
                    outer.data4 = it.toString()
                    outer.data5 = it.toString()
                    outer.data6.target = inner
                    return@map outer
                }
                .toList()
                .map { entityOuterBox.put(it) }
                .toCompletable()
    }

    private fun read(): Single<List<EntityOuter>> {
        return RxQuery.observable(entityOuterBox.query().build())
                .firstOrError()
    }
}