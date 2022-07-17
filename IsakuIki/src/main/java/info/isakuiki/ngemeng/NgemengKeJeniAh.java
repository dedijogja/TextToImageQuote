package info.isakuiki.ngemeng;


import android.content.Context;

import info.isakuiki.kekrep.text.deskriptor.DeskripsiText;

public class NgemengKeJeniAh {
    static {
        System.loadLibrary("native-lib");
    }

    private Context context;

    public NgemengKeJeniAh(Context context){
        if(!context.getPackageName().equals(packageName(context))){
            throw new RuntimeException("Failed native exception!");
        }
        this.context = context;
    }

    public String getKeyAssets() {
        return keyDesAssets(context);
    }


    public String getAdInterstitial() {
        return new DeskripsiText(keyDesText(context), adInterstitial(context)).dapatkanTextAsli();
    }

    public String getAdBanner() {
        return new DeskripsiText(keyDesText(context), adBanner(context)).dapatkanTextAsli();
    }


    public String getStartAppId() {
        return new DeskripsiText(keyDesText(context), startAppId(context)).dapatkanTextAsli();
    }


    public native String packageName(Context context);
    public native String keyDesText(Context context);
    public native String keyDesAssets(Context context);
    public native String adInterstitial(Context context);
    public native String adBanner(Context context);
    public native String startAppId(Context context);
}
