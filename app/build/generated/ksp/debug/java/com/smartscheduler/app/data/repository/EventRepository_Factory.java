package com.smartscheduler.app.data.repository;

import com.smartscheduler.app.data.local.FixedEventDao;
import com.smartscheduler.app.data.local.ScheduledBlockDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class EventRepository_Factory implements Factory<EventRepository> {
  private final Provider<FixedEventDao> fixedEventDaoProvider;

  private final Provider<ScheduledBlockDao> scheduledBlockDaoProvider;

  public EventRepository_Factory(Provider<FixedEventDao> fixedEventDaoProvider,
      Provider<ScheduledBlockDao> scheduledBlockDaoProvider) {
    this.fixedEventDaoProvider = fixedEventDaoProvider;
    this.scheduledBlockDaoProvider = scheduledBlockDaoProvider;
  }

  @Override
  public EventRepository get() {
    return newInstance(fixedEventDaoProvider.get(), scheduledBlockDaoProvider.get());
  }

  public static EventRepository_Factory create(Provider<FixedEventDao> fixedEventDaoProvider,
      Provider<ScheduledBlockDao> scheduledBlockDaoProvider) {
    return new EventRepository_Factory(fixedEventDaoProvider, scheduledBlockDaoProvider);
  }

  public static EventRepository newInstance(FixedEventDao fixedEventDao,
      ScheduledBlockDao scheduledBlockDao) {
    return new EventRepository(fixedEventDao, scheduledBlockDao);
  }
}
