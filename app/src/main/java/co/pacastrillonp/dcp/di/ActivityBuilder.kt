package co.pacastrillonp.dcp.di

import co.pacastrillonp.dcp.di.util.ActivityScoped
import co.pacastrillonp.dcp.view.MainActivity
import co.pacastrillonp.dcp.view.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    internal abstract fun MainActivity(): MainActivity

}