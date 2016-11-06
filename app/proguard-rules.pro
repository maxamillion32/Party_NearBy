# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/ram/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#Use Progaurd settings to avoid obfuscating class/method name
-dontskipnonpubliclibraryclasses
-dontobfuscate
-forceprocessing
-optimizationpasses 5
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging*/
-allowaccessmodification
-repackageclasses ''



#obfuscation, Keep the attributes of InnerClasses, keep your class and keep the class members of the class
-keepattributes InnerClasses
-keep class com.app.pojo**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application

#config to remove debugging Log API  or remove log statements
-keep class * extends android.app.Activity
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

## UserVoice
-keep class android.support.v7.widget.SearchView { *; }
