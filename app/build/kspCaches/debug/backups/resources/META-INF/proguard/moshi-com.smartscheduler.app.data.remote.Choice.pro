-if class com.smartscheduler.app.data.remote.Choice
-keepnames class com.smartscheduler.app.data.remote.Choice
-if class com.smartscheduler.app.data.remote.Choice
-keep class com.smartscheduler.app.data.remote.ChoiceJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.smartscheduler.app.data.remote.Choice
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-if class com.smartscheduler.app.data.remote.Choice
-keepclassmembers class com.smartscheduler.app.data.remote.Choice {
    public synthetic <init>(int,com.smartscheduler.app.data.remote.ChatMessage,java.lang.String,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
