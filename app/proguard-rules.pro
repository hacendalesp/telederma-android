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
# OrmLite uses reflection
# Butterknife
# Uncomment this to preserve the line number information for
# debugging stack traces.
-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.* { *; }

#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-keep class com.telederma.gov.co.**
#-keepclassmembers class com.telederma.gov.co.** {*;}

-keep class com.telederma.gov.co.database.**
-keepclassmembers class com.telederma.gov.co.database.** {*;}

#-keep class com.telederma.gov.co.modelo.**
#-keepclassmembers class com.telederma.gov.co.modelo.** {*;}

-keepattributes *Annotation*,Signature

-dontnote com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
-keepclassmembers class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper {
    <init>(android.content.Context);
}

-dontnote com.j256.ormlite.field.DatabaseFieldConfig
-keepclassmembers class com.j256.ormlite.field.DatabaseFieldConfig {
    <fields>;
}

-dontnote com.j256.ormlite.dao.Dao
-keepclassmembers class * implements com.j256.ormlite.dao.Dao {
    <init>(**);
    <init>(**, java.lang.Class);
}

-dontnote com.j256.ormlite.android.AndroidLog
-keep class com.j256.ormlite.android.AndroidLog {
    <init>(java.lang.String);
}

-dontnote com.j256.ormlite.table.DatabaseTable
-keep @com.j256.ormlite.table.DatabaseTable class * {
    void set*(***);
    *** get*();
}

-dontnote com.j256.ormlite.field.DatabaseField
-keepclassmembers @interface com.j256.ormlite.field.DatabaseField {
    <methods>;
}

-dontnote com.j256.ormlite.field.ForeignCollectionField
-keepclassmembers @interface com.j256.ormlite.field.ForeignCollectionField {
    <methods>;
}

-keepclasseswithmembers class * {
    @com.j256.ormlite.field.DatabaseField <fields>;
}

-keepclasseswithmembers class * {
    @com.j256.ormlite.field.ForeignCollectionField <fields>;
}

-dontnote org.joda.time.DateTime
-keep,allowobfuscation class org.joda.time.DateTime
-keepclassmembers class org.joda.time.DateTime {
    <init>(long);
    long getMillis();
}


-dontwarn com.j256.ormlite.android.**
-dontwarn com.j256.ormlite.logger.**
-dontwarn com.j256.ormlite.misc.**

-keep class org.apache.commons.logging.**               { *; }
-keep class com.amazonaws.services.sqs.QueueUrlHandler  { *; }
-keep class com.amazonaws.javax.xml.transform.sax.*     { public *; }
-keep class com.amazonaws.javax.xml.stream.**           { *; }
-keep class com.amazonaws.services.**.model.*Exception* { *; }
-keep class org.codehaus.**                             { *; }
-keepattributes Signature,*Annotation*

-dontwarn javax.xml.stream.events.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.apache.commons.logging.impl.**
-dontwarn org.apache.http.conn.scheme.**

# Class names are needed in reflection
-keepnames class com.amazonaws.**
-keepnames class com.amazon.**
# Request handlers defined in request.handlers
-keep class com.amazonaws.services.**.*Handler
# The following are referenced but aren't required to run
-dontwarn com.fasterxml.jackson.**
-dontwarn org.apache.commons.logging.**
# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**
# The SDK has several references of Apache HTTP client
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**