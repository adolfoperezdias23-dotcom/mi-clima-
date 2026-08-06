# Los DTO se (de)serializan con Gson por reflexión: conservar sus campos
-keep class com.miclima.app.data.remote.dto.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
