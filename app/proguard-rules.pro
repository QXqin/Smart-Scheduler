# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keep class com.smartscheduler.app.data.remote.** { *; }
-keep class com.squareup.moshi.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**
