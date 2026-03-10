package com.smartscheduler.app.ui.settings;

import com.smartscheduler.app.data.repository.EventRepository;
import com.smartscheduler.app.data.repository.SettingsRepository;
import com.smartscheduler.app.data.repository.TodoRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<SettingsRepository> settingsRepositoryProvider;

  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<TodoRepository> todoRepositoryProvider;

  public SettingsViewModel_Factory(Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<EventRepository> eventRepositoryProvider,
      Provider<TodoRepository> todoRepositoryProvider) {
    this.settingsRepositoryProvider = settingsRepositoryProvider;
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.todoRepositoryProvider = todoRepositoryProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(settingsRepositoryProvider.get(), eventRepositoryProvider.get(), todoRepositoryProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<SettingsRepository> settingsRepositoryProvider,
      Provider<EventRepository> eventRepositoryProvider,
      Provider<TodoRepository> todoRepositoryProvider) {
    return new SettingsViewModel_Factory(settingsRepositoryProvider, eventRepositoryProvider, todoRepositoryProvider);
  }

  public static SettingsViewModel newInstance(SettingsRepository settingsRepository,
      EventRepository eventRepository, TodoRepository todoRepository) {
    return new SettingsViewModel(settingsRepository, eventRepository, todoRepository);
  }
}
