package com.smartscheduler.app.data.repository;

import com.squareup.moshi.Moshi;
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
public final class LlmRepository_Factory implements Factory<LlmRepository> {
  private final Provider<Moshi> moshiProvider;

  public LlmRepository_Factory(Provider<Moshi> moshiProvider) {
    this.moshiProvider = moshiProvider;
  }

  @Override
  public LlmRepository get() {
    return newInstance(moshiProvider.get());
  }

  public static LlmRepository_Factory create(Provider<Moshi> moshiProvider) {
    return new LlmRepository_Factory(moshiProvider);
  }

  public static LlmRepository newInstance(Moshi moshi) {
    return new LlmRepository(moshi);
  }
}
