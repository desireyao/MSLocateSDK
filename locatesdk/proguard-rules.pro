# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-target 1.8
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-dontusemixedcaseclassnames
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-keepparameternames
-verbose
-ignorewarnings

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends no.nordicsemi.android.dfu.DfuBaseService

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper

-keep public class * extends android.preference.Preference

-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep - Library. Keep all public and protected classes, fields, and methods.
#-keep public class * {
#    public <fields>;
#    public <methods>;
#}

#-keep class com.marslocate.beacon.BeaconInfo{
#    public <fields>;
#    public <methods>;
#}

-keep class com.marslocate.listener.**{
    public <fields>;
    public <methods>;
}

#
-keep class com.marslocate.model.** {
    public <fields>;
    public <methods>;
}

#-keep class com.marslocate.network.**{
#     *;
#}

-keep class com.marslocate.network.bean.**{
     *;
}

-keep class com.marslocate.network.enums.**{
     *;
}

-keep class com.marslocate.sdk.** {
    public <fields>;
    public <methods>;
}

-keep class com.marslocate.log.** {
    public <fields>;
    public <methods>;
}

-keep class com.marslocate.util.** {
    public <fields>;
    public <methods>;
}

-keep class com.marslocate.volley.** {
    *;
}

-keep public class com.beacool.jni{
  *;
}

#AltBeacon
-dontwarn org.altbeacon.**
-keep class org.altbeacon.** { *; }
-keep interface org.altbeacon.**{ *; }

# Gson library
-keep class com.google.gson.**{
    *;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
