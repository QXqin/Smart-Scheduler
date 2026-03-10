package com.smartscheduler.app.di

import android.content.Context
import androidx.room.Room
import com.smartscheduler.app.data.local.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "smart_scheduler.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFixedEventDao(db: AppDatabase): FixedEventDao = db.fixedEventDao()

    @Provides
    fun provideTodoDao(db: AppDatabase): TodoDao = db.todoDao()

    @Provides
    fun provideScheduledBlockDao(db: AppDatabase): ScheduledBlockDao = db.scheduledBlockDao()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
}
