-if class com.smartscheduler.app.data.remote.ChatMessage
-keepnames class com.smartscheduler.app.data.remote.ChatMessage
-if class com.smartscheduler.app.data.remote.ChatMessage
-keep class com.smartscheduler.app.data.remote.ChatMessageJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
