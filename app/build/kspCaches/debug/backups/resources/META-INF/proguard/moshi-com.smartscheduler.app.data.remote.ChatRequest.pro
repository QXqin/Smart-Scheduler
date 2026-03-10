-if class com.smartscheduler.app.data.remote.ChatRequest
-keepnames class com.smartscheduler.app.data.remote.ChatRequest
-if class com.smartscheduler.app.data.remote.ChatRequest
-keep class com.smartscheduler.app.data.remote.ChatRequestJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.smartscheduler.app.data.remote.ChatRequest
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.smartscheduler.app.data.remote.ChatRequest
-keepclassmembers class com.smartscheduler.app.data.remote.ChatRequest {
    public synthetic <init>(java.lang.String,java.util.List,double,com.smartscheduler.app.data.remote.ResponseFormat,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
