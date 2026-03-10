package com.smartscheduler.app.di;

import com.smartscheduler.app.data.local.AppDatabase;
import com.smartscheduler.app.data.local.TodoDao;
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
public final class AppModule_ProvideTodoDaoFactory implements Factory<TodoDao> {
  private final Provider<AppDatabase> dbProvider;

  public AppModule_ProvideTodoDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TodoDao get() {
    return provideTodoDao(dbProvider.get());
  }

  public static AppModule_ProvideTodoDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new AppModule_ProvideTodoDaoFactory(dbProvider);
  }

  public static TodoDao provideTodoDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTodoDao(db));
  }
}
