// BackupModule.kt
package com.mouton.openwinemer.di

import android.content.Context
import com.google.gson.Gson
import com.mouton.openwinemer.data.local.WineDao
import com.mouton.openwinemer.domain.usecase.BackupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module Hilt qui explique à Hilt comment créer un BackupUseCase.
 * Sans ce module, Hilt ne sait pas construire l'objet et plante.
 */
@Module
@InstallIn(SingletonComponent::class)
object BackupModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideBackupUseCase(
        @ApplicationContext context: Context,
        wineDao: WineDao,
        gson: Gson
    ): BackupUseCase {
        return BackupUseCase(
            context = context,
            wineDao = wineDao,
            gson = gson
        )
    }
}
