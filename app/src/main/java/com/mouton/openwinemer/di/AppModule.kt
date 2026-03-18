package com.mouton.openwinemer.di

import android.app.Application
import androidx.room.Room
import com.mouton.openwinemer.data.local.WineDao
import com.mouton.openwinemer.data.local.WineDatabase
import com.mouton.openwinemer.data.repository.WineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Fournit la base de données Room
    @Provides
    @Singleton
    fun provideDatabase(app: Application): WineDatabase =
        Room.databaseBuilder(
            app,
            WineDatabase::class.java,
            "wines.db"
        ).build()

    // Fournit le DAO
    @Provides
    fun provideWineDao(db: WineDatabase): WineDao = db.wineDao()

    // Fournit le repository
    @Provides
    @Singleton
    fun provideWineRepository(
        wineDao: WineDao
    ): WineRepository = WineRepository(wineDao)
}
