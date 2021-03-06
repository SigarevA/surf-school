package ru.samsung.itshool.memandos.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.samsung.itshool.memandos.model.repo.MemDatabase
import javax.inject.Singleton

@Module
class RoomModule {
    @Singleton
    @Provides
    fun provideMemDatabase(context: Context): MemDatabase {
        return Room.databaseBuilder(
            context,
            MemDatabase::class.java, "database-name"
        ).build()
    }
}