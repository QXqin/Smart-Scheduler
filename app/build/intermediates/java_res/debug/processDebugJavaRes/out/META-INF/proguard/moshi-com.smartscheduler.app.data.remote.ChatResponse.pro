-if class com.smartscheduler.app.data.remote.ChatResponse
-keepnames class com.smartscheduler.app.data.remote.ChatResponse
-if class com.smartscheduler.app.data.remote.ChatResponse
-keep class com.smartscheduler.app.data.remote.ChatResponseJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.smartscheduler.app.data.remote.ChatResponse
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.smartscheduler.app.data.remote.ChatResponse
-keepclassmembers class com.smartscheduler.app.data.remote.ChatResponse {
    public synthetic <init>(java.lang.String,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
