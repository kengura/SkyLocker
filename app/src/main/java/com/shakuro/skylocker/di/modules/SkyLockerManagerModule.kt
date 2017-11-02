package com.shakuro.skylocker.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.shakuro.skylocker.R
import com.shakuro.skylocker.model.quiz.QuizInteractor
import com.shakuro.skylocker.model.settings.SettingsInteractor
import com.shakuro.skylocker.model.SkyLockerManager
import com.shakuro.skylocker.model.models.db.DaoMaster
import com.shakuro.skylocker.model.models.db.DaoSession
import com.shakuro.skylocker.model.settings.SettingsRepository
import com.shakuro.skylocker.model.skyeng.SkyEngApi
import dagger.Module
import dagger.Provides
import org.apache.commons.io.IOUtils
import com.shakuro.skylocker.system.LockServiceManager
import ru.terrakok.gitlabclient.model.system.ResourceManager
import java.io.File
import java.io.FileOutputStream
import javax.inject.Singleton

@Module(includes = arrayOf(SkyEngApiModule::class, ContextModule::class, SystemServicesModule::class))
class SkyLockerManagerModule {

    @Provides
    @Singleton
    fun provideSkyLockerManager(skyEngApi: SkyEngApi,
                                preferences: SharedPreferences,
                                daoSession: DaoSession): SkyLockerManager {
        return SkyLockerManager(skyEngApi, preferences, daoSession)
    }

    @Provides
    @Singleton
    fun provideDaoSession(@ApplicationContext context: Context,
                          @SkyLockerDBFile dbFile: File): DaoSession {
        if (!dbFile.exists()) {
            val inputStream = context.resources.openRawResource(R.raw.skylockerdb)
            val outputStream = FileOutputStream(dbFile)
            IOUtils.copy(inputStream, outputStream)
            IOUtils.closeQuietly(inputStream)
            IOUtils.closeQuietly(outputStream)
        }
        val db = DaoMaster.DevOpenHelper(context, dbFile.absolutePath).writableDb
        return DaoMaster(db).newSession()
    }

    @Provides
    @Singleton
    @SkyLockerDBFile
    fun provideSkyLockerDBFile(@ApplicationContext context: Context): File {
        return File(context.filesDir, SkyLockerManager.DB_FILE_NAME)
    }

    @Provides
    @Singleton
    fun provideLockServiceManager(@ApplicationContext context: Context) = LockServiceManager(context)

    @Provides
    @Singleton
    fun provideSettingsInteractor(slManager: SkyLockerManager, lockServiceManager: LockServiceManager, sr: SettingsRepository, rm: ResourceManager) =
        SettingsInteractor(slManager, lockServiceManager, sr, rm)

    @Provides
    @Singleton
    fun provideQuizInteractor(skyLockerManager: SkyLockerManager) = QuizInteractor(skyLockerManager)

    @Provides
    @Singleton
    fun provideSettingsRepository(preferences: SharedPreferences)= SettingsRepository(preferences)
}