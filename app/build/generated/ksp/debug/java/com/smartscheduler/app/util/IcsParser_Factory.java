package com.smartscheduler.app.util;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class IcsParser_Factory implements Factory<IcsParser> {
  @Override
  public IcsParser get() {
    return newInstance();
  }

  public static IcsParser_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static IcsParser newInstance() {
    return new IcsParser();
  }

  private static final class InstanceHolder {
    private static final IcsParser_Factory INSTANCE = new IcsParser_Factory();
  }
}
