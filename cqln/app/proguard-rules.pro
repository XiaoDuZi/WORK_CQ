# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\AndroidStudioSDK\Sdk/tools/proguard/proguard-android.txt
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

#################-----  代码混淆文件声明  -----###############################

# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

#-optimizations !code/simplification/*,!field/*,!class/merging/*,!method/removal/parameter,!method/propagation/*,!method/marking/static,!class/unboxing/enum,!code/removal/advanced,!code/allocation/variable

#指定代码的压缩级别
-optimizationpasses 5

-allowaccessmodification
#预校验 不校验
-dontpreverify
#优化  不优化输入的类文件
#-dontoptimize

# The remainder of this file is identical to the non-optimized version
# of the Proguard configuration file (except that the other file has
# flags to turn off optimization).
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#混淆时是否记录日志
-verbose
#保护注解
-keepattributes *Annotation*
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
#保持 native 方法不被混淆
# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
#保持自定义控件类不被混淆
# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保持枚举 enum 类不被混淆
# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆,基本是model实体类
-keepnames class * implements java.io.Serializable
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#如果引用了v4或者v7包
# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

#忽略警告
-ignorewarnings

#淆保护自己项目的部分代码以及引用的第三方jar包
# 由于 compile fileTree(include: ['*.jar'], dir: 'libs') 这里声明了,所以不需要下面的了

#-libraryjars /libs/ipaneltvlibrary-2015.jar
#-libraryjars /libs/volley.jar
#-libraryjars /libs/universal-image-loader-1.9.3.jar

-dontwarn android.net.telecast.**
-keep class android.net.telecast.JSectionFilter.**{*;}


#-------------保护第三方jar包---------------------------start
#image-load
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }

# volley
-dontwarn com.android.volley.jar.**
-keep class com.android.volley.**{*;}

#ipanel
-dontwarn ipaneltv.toolkit.**
-dontwarn ipaneltv.uuids.**

-keep class ipaneltv.toolkit.fragment.**{*;}


#butterknife
-dontwarn butterknife.internal.**
-keep class butterknife.**{*;}
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#gson
-dontwarn com.google.gson.Gson.**
-keep class com.google.gson.Gson.**{*;}

#v4
-dontwarn android.support.v4.**
-keep class android.support.v4.**{*;}

#-------------保护第三方jar包-----------------------------end

-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.**
-keep class * extends java.lang.annotation.Annotation { *; }

#keep 边框View-------start
-keep class com.utvgo.hifi.views.FocusBorderView.**{*;}
-keep class com.utvgo.hifi.views.ScaleView.**{*;}
-keep class com.utvgo.hifi.views.ItemSquare.**{*;}

-keepclassmembers class com.utvgo.hifi.views.FocusBorderView{
     <fields>;
     <methods>;
}
-keepclassmembers class com.utvgo.hifi.views.ScaleView{
     <fields>;
     <methods>;
}

-keepclassmembers class com.utvgo.hifi.views.ItemSquare{
     <fields>;
     <methods>;
}
#keep 边框View-------end

# 保持哪些类不被混淆
-keep class android.** {*; }
-keep public class * extends android.view
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.pm
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService


#public变量不能混淆
-keepclassmembers class com.utvgo.hifi.activity.MainActivity{
    public <fields>;
}
-keepclassmembers class com.utvgo.hifi.activity.BaseActivity{
    public <fields>;
}
-keepclassmembers class com.utvgo.hifi.constant.ConstantEnum{
    public <fields>;
}


#枚举类不能去混淆
-keepclassmembers class com.utvgo.hifi.utils.Enumer{
   public static **[] values();
   public static ** valueOf(java.lang.String);
}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#bean最好避免混淆
-keep class com.utvgo.hifi.bean.** { *; }
-keep class hifidatasdk.bean.** { *; }

-dontwarn   com.utvgo.hifi.bean.**
-dontwarn   hifidatasdk.bean.**

########### 不优化泛型和反射 ##########
-keepattributes Signature

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
}



# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }


# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }


##---------------End: proguard configuration for Gson  ----------

