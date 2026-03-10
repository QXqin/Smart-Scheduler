package com.smartscheduler.app.domain.usecase;

import com.smartscheduler.app.data.repository.EventRepository;
import com.smartscheduler.app.data.repository.LlmRepository;
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
public final class GenerateScheduleUseCase_Factory implements Factory<GenerateScheduleUseCase> {
  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<TodoRepository> todoRepositoryProvider;

  private final Provider<LlmRepository> llmRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public GenerateScheduleUseCase_Factory(Provider<EventRepository> eventRepositoryProvider,
      Provider<TodoRepository> todoRepositoryProvider,
      Provider<LlmRepository> llmRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.todoRepositoryProvider = todoRepositoryProvider;
    this.llmRepositoryProvider = llmRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public GenerateScheduleUseCase get() {
    return newInstance(eventRepositoryProvider.get(), todoRepositoryProvider.get(), llmRepositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static GenerateScheduleUseCase_Factory create(
      Provider<EventRepository> eventRepositoryProvider,
      Provider<TodoRepository> todoRepositoryProvider,
      Provider<LlmRepository> llmRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new GenerateScheduleUseCase_Factory(eventRepositoryProvider, todoRepositoryProvider, llmRepositoryProvider, settingsRepositoryProvider);
  }

  public static GenerateScheduleUseCase newInstance(EventRepository eventRepository,
      TodoRepository todoRepository, LlmRepository llmRepository,
      SettingsRepository settingsRepository) {
    return new GenerateScheduleUseCase(eventRepository, todoRepository, llmRepository, settingsRepository);
  }
}
