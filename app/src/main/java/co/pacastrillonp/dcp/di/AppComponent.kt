package co.pacastrillonp.dcp.di

import co.pacastrillonp.dcp.DPCApplication
import co.pacastrillonp.dcp.di.util.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ViewModelModule::class,
        ActivityBuilder::class]
)
interface AppComponent : AndroidInjector<DPCApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<DPCApplication>()
}