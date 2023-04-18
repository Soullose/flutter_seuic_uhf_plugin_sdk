# Seuic Scanner SDK
-keep class com.seuic.** {*; }
-keep class com.example.** {*; }
-keep class com.seuic.uhf.** {*; }
-keep class com.seuic.scankey.** {*; }
-dontwarn com.seuic.**

# Flutter wrapper
-keep class io.flutter.app.** { *; }
-keep class io.flutter.plugin.**  { *; }
-keep class io.flutter.util.**  { *; }
-keep class io.flutter.view.**  { *; }
-keep class io.flutter.**  { *; }
-keep class io.flutter.plugins.**  { *; }
-keep class androidx.lifecycle.** { *; }



# Apache Commons
-keep class org.apache.commons.** { *; }
-dontwarn org.apache.commons.logging.**

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepattributes Signature
-keepattributes Annotation