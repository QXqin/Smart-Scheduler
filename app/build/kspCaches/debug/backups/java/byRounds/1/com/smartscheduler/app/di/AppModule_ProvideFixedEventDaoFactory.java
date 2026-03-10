package com.smartscheduler.app.di;

import com.smartscheduler.app.data.local.AppDatabase;
import com.smartscheduler.app.data.local.FixedEventDao;
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
public final class AppModule_ProvideFixedEventDaoFactory implements Factory<FixedEventDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideFixedEventDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public FixedEventDao get() {
    return provideFixedEventDao(dbProvider.get());
  }

  public static AppModule_ProvideFixedEventDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideFixedEventDaoFactory(dbProvider);
  }

  public static FixedEventDao provideFixedEventDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideFixedEventDao(db));
  }
}
