package com.smartscheduler.app.ui.calendar;

import android.app.Application;
import com.smartscheduler.app.data.repository.EventRepository;
import com.smartscheduler.app.domain.usecase.GenerateScheduleUseCase;
import com.smartscheduler.app.util.IcsParser;
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
public final class CalendarViewModel_Factory implements Factory<CalendarViewModel> {
  private final Provider<Application> applicationProvider;

  private final Provider<EventRepository> eventRepositoryProvider;

  private final Provider<IcsParser> icsParserProvider;

  private final Provider<GenerateScheduleUseCase> generateScheduleUseCaseProvider;

  public CalendarViewModel_Factory(Provider<Application> applicationProvider,
      Provider<EventRepository> eventRepositoryProvider, Provider<IcsParser> icsParserProvider,
      Provider<GenerateScheduleUseCase> generateScheduleUseCaseProvider) {
    this.applicationProvider = applicationProvider;
    this.eventRepositoryProvider = eventRepositoryProvider;
    this.icsParserProvider = icsParserProvider;
    this.generateScheduleUseCaseProvider = generateScheduleUseCaseProvider;
  }

  @Override
  public CalendarViewModel get() {
    return newInstance(applicationProvider.get(), eventRepositoryProvider.get(), icsParserProvider.get(), generateScheduleUseCaseProvider.get());
  }

  public static CalendarViewModel_Factory create(Provider<Application> applicationProvider,
      Provider<EventRepository> eventRepositoryProvider, Provider<IcsParser> icsParserProvider,
      Provider<GenerateScheduleUseCase> generateScheduleUseCaseProvider) {
    return new CalendarViewModel_Factory(applicationProvider, eventRepositoryProvider, icsParserProvider, generateScheduleUseCaseProvider);
  }

  public static CalendarViewModel newInstance(Application application,
      EventRepository eventRepository, IcsParser icsParser,
      GenerateScheduleUseCase generateScheduleUseCase) {
    return new CalendarViewModel(application, eventRepository, icsParser, generateScheduleUseCase);
  }
}
