package ca.tetervak.friendlyeats.repository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRestaurantDataRepository(
        repository: RestaurantDataRepositoryImpl): RestaurantDataRepository
}