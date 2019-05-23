package co.pacastrillonp.dcp.di.util

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ArkboxMessageViewModelFactory):
            ViewModelProvider.Factory
}