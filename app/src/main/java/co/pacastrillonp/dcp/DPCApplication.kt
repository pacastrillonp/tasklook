package co.pacastrillonp.dcp

import co.pacastrillonp.dcp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class DPCApplication : DaggerApplication() {


    override fun onCreate() {
        super.onCreate()
    }


    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().create(this)
}