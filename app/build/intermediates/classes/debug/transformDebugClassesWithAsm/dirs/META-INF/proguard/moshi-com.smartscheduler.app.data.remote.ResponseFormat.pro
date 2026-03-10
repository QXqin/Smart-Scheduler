-if class com.smartscheduler.app.data.remote.ResponseFormat
-keepnames class com.smartscheduler.app.data.remote.ResponseFormat
-if class com.smartscheduler.app.data.remote.ResponseFormat
-keep class com.smartscheduler.app.data.remote.ResponseFormatJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
