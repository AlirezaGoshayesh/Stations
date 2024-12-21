package ir.bit24.alireza.di

import com.google.gson.Gson
import ir.bit24.alireza.data.ErrorHandler
import ir.bit24.alireza.data.RepositoryImpl
import ir.bit24.alireza.data.remote.RemoteDataSource
import ir.bit24.alireza.data.remote.RemoteDataSourceImpl
import ir.bit24.alireza.data.remote.connection.MService
import ir.bit24.alireza.domain.Repository
import ir.bit24.alireza.domain.exceptions.IErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.bit24.alireza.data.local.LocalDataSource
import ir.bit24.alireza.data.local.LocalDataSourceImpl
import ir.bit24.alireza.data.local.room.StationDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): Repository {
        return RepositoryImpl(remoteDataSource, localDataSource)
    }

    @Singleton
    @Provides
    fun provideRemoteDataSource(
        service: MService
    ): RemoteDataSource {
        return RemoteDataSourceImpl(service)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(
        stationDao: StationDao,
    ): LocalDataSource {
        return LocalDataSourceImpl(stationDao)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideErrorHandler(): IErrorHandler {
        return ErrorHandler()
    }
}