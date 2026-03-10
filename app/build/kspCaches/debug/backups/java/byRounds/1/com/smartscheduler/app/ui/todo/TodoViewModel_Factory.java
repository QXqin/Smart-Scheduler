package com.smartscheduler.app.ui.todo;

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
public final class TodoViewModel_Factory implements Factory<TodoViewModel> {
  private final Provider<TodoRepository> todoRepositoryProvider;

  public TodoViewModel_Factory(Provider<TodoRepository> todoRepositoryProvider) {
    this.todoRepositoryProvider = todoRepositoryProvider;
  }

  @Override
  public TodoViewModel get() {
    return newInstance(todoRepositoryProvider.get());
  }

  public static TodoViewModel_Factory create(Provider<TodoRepository> todoRepositoryProvider) {
    return new TodoViewModel_Factory(todoRepositoryProvider);
  }

  public static TodoViewModel newInstance(TodoRepository todoRepository) {
    return new TodoViewModel(todoRepository);
  }
}
