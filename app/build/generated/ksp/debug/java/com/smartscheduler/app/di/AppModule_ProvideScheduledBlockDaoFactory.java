package com.smartscheduler.app.di;

import com.smartscheduler.app.data.local.AppDatabase;
import com.smartscheduler.app.data.local.ScheduledBlockDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class AppModule_ProvideScheduledBlockDaoFactory implements Factory<ScheduledBlockDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideScheduledBlockDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ScheduledBlockDao get() {
    return provideScheduledBlockDao(dbProvider.get());
  }

  public static AppModule_ProvideScheduledBlockDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideScheduledBlockDaoFactory(dbProvider);
  }

  public static ScheduledBlockDao provideScheduledBlockDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideScheduledBlockDao(db));
  }
}
