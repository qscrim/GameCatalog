# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.example.gamecatalog.data.remote.** { *; }
-keep class com.example.gamecatalog.data.model.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Kotlin Coroutines & Flow
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}