package com.lotusreichhart.gencanvas.core.data.database.di

import android.content.Context
import androidx.room.Room
import com.lotusreichhart.gencanvas.core.data.database.GenCanvasDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesGenCanvasDatabase(
        @ApplicationContext context: Context
    ): GenCanvasDatabase {
        return Room.databaseBuilder(
            context,
            GenCanvasDatabase::class.java,
            "gen_canvas_database"
        ).build()
    }
}