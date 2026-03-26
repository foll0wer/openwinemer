package com.mouton.openwinemer.di

import android.app.Application
import androidx.room.Room
import com.mouton.openwinemer.data.local.WineDao
import com.mouton.openwinemer.data.local.WineDatabase
import com.mouton.openwinemer.data.local.MIGRATION_1_2 // <-- add this import
import com.mouton.openwinemer.data.repository.WineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides the Room database instance.
    // This is where we register the migration from version 1 to 2.
    @Provides
    @Singleton
    fun provideDatabase(app: Application): WineDatabase =
        Room.databaseBuilder(
            app,
            WineDatabase::class.java,
            "wines.db"
        )
        // Register migration so Room can upgrade existing databases safely
        .addMigrations(MIGRATION_1_2)
        .build()

    // Provides the DAO.
    @Provides
    fun provideWineDao(db: WineDatabase): WineDao = db.wineDao()

    // Provides the repository.
    @Provides
    @Singleton
    fun provideWineRepository(
        wineDao: WineDao
    ): WineRepository = WineRepository(wineDao)
}
