package co.pacastrillonp.dcp.di

import android.content.Context
import co.pacastrillonp.dcp.DPCApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideApplicationContext(application: DPCApplication): Context = application.applicationContext

}