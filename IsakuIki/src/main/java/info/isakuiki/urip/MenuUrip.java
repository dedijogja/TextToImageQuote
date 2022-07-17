package info.isakuiki.urip;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.splash.SplashConfig;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import info.isakuiki.R;
import info.isakuiki.ndewo.NdewoDewe;
import info.isakuiki.ngemeng.NgemengKeJeniAh;
import info.isakuiki.terus.NgunuiTerus;


public class MenuUrip extends AppCompatActivity{

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urip_menu_aktifity);

        NdewoDewe app = (NdewoDewe) getApplication();
        String status = app.getStatusIklan();
        if(status.equals(NgunuiTerus.BERHASIL_LOAD_IKLAN)){
            AdView adView = new AdView(this);
            adView.setAdUnitId(new NgemengKeJeniAh(this).getAdBanner());
            adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
            adView.loadAd(new AdRequest.Builder().build());
            ((LinearLayout) findViewById(R.id.iklanMenu)).removeAllViews();
            ((LinearLayout) findViewById(R.id.iklanMenu)).addView(adView);
        }else if(status.equals(NgunuiTerus.GAGAL_LOAD_IKLAN)){
            StartAppSDK.init(this, new NgemengKeJeniAh(this).getStartAppId(), false);
            if (!isNetworkConnected()) {
                StartAppAd.disableSplash();
            }else{
                StartAppAd.showSplash(this, savedInstanceState,
                        new SplashConfig()
                                .setTheme(SplashConfig.Theme.USER_DEFINED)
                                .setCustomScreen(R.layout.activity_load_semua_urip)
                );
            }
        }

        if(status.equals(NgunuiTerus.GAGAL_LOAD_IKLAN)){
            findViewById(R.id.imgTemp).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.tmblSemuaKoleksiKata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUrip.this, SemuaKoleksiUrip.class));
            }
        });

        findViewById(R.id.tmblKoleksiFavorit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUrip.this, SemuaFavoritUrip.class));
            }
        });

        findViewById(R.id.tmblSemuaKoleksiGambar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuUrip.this, SemuaKoleksiGambarUrip.class));
            }
        });

        findViewById(R.id.tmblCustomDpMaker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Masukkan Teks");
                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(NgunuiTerus.DEV_NAME);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MenuUrip.this, SemuaKoleksiGambarUrip.class);
                                intent.putExtra(NgunuiTerus.KODE_TEXT, input.getText().toString());
                                startActivity(intent);
                            }
                        });
                alertDialog.setNegativeButton("BATAL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Konfirmasi Keluar");
        alertDialog.setMessage("Apakah anda ingin menutup aplikasi ini?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YA",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "TIDAK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
