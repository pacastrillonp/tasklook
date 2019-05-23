package co.pacastrillonp.dcp.view

import androidx.lifecycle.ViewModel
import co.pacastrillonp.dcp.di.util.ViewModelKey
import co.pacastrillonp.dcp.viewmodel.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

}