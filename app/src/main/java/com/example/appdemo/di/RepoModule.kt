package com.example.appdemo.di

import com.example.appdemo.data.repository.MainRepositoryImpl
import com.example.appdemo.data.repository.IMainRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepoModule {

    @Binds
    abstract fun bindMainRemoteService(mainRepositoryImpl: MainRepositoryImpl): IMainRepo
}