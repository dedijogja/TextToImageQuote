#include <jni.h>
#include <string>
#include <string.h>

//key text sudah diganti, key asset sudah diganti
std::string keyDesText = "4LB&%x`B$dV5)^+;";
std::string keyDesAssets = "xFARNbBPhdrePoN3rr2NWw==";

//kode sudah diganti isokuikicorp
std::string adInterstitial =        "bFwf\\{e{PB=%(=-6@-!*_=7#0*)~4^#*=3(q0-";
std::string adBanner =              "bFwf\\{e{PB=%(=-6@-!*_=7#0*)~9_%^89+060";

//startApp sudah diganti Isoku iki corp
std::string startAppId = "4Q0-q$=_*";

//area package, mempersulit proses replace String .so file dengan memisahkan menjadi beberapa bagian
std::string awalP = "info.i";
std::string tengahP = "sakuiki";
std::string ahirP = "katadpikhlasdansabar";
std::string finalPackage = awalP + tengahP +"."+ ahirP;

jstring dapatkanPackage(JNIEnv* env, jobject activity) {
    jclass android_content_Context =env->GetObjectClass(activity);
    jmethodID midGetPackageName = env->GetMethodID(android_content_Context,"getPackageName", "()Ljava/lang/String;");
    jstring packageName= (jstring)env->CallObjectMethod(activity, midGetPackageName);
    return packageName;
}

const char * cekStatus(JNIEnv* env, jobject activity, const char * text){
    if(strcmp(env->GetStringUTFChars(dapatkanPackage(env, activity), NULL), finalPackage.c_str()) != 0){
        jclass Exception = env->FindClass("java/lang/RuntimeException");
        env->ThrowNew(Exception, "Operation cannot be completed");
        return NULL;
    }
    return text;
}


extern "C"
JNIEXPORT jstring JNICALL Java_info_isakuiki_ngemeng_NgemengKeJeniAh_keyDesText(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesText.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_info_isakuiki_ngemeng_NgemengKeJeniAh_keyDesAssets(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, keyDesAssets.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_info_isakuiki_ngemeng_NgemengKeJeniAh_packageName(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, finalPackage.c_str()));
}


extern "C"
JNIEXPORT jstring JNICALL Java_info_isakuiki_ngemeng_NgemengKeJeniAh_adInterstitial(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adInterstitial.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_info_isakuiki_ngemeng_NgemengKeJeniAh_adBanner(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, adBanner.c_str()));
}

extern "C"
JNIEXPORT jstring JNICALL Java_info_isakuiki_ngemeng_NgemengKeJeniAh_startAppId(
        JNIEnv *env, jobject, jobject activity ) {
    return env->NewStringUTF(cekStatus(env, activity, startAppId.c_str()));
}

