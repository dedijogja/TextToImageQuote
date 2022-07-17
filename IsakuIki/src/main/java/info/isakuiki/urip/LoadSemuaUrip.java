package info.isakuiki.urip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.AdListener;

import java.io.IOException;

import info.isakuiki.R;
import info.isakuiki.kekrep.kenten.KataKrep;
import info.isakuiki.ndewo.NdewoDewe;
import info.isakuiki.terus.NgunuiTerus;

public class LoadSemuaUrip extends AppCompatActivity {

    private boolean statusIklan = true;
    int hitung = 0;
    int loadIklanBerapaKali = 5;
    private NdewoDewe ndewoDewe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_semua_urip);

        if(NgunuiTerus.PENYIMPAN_SEMUA_KATA == null || NgunuiTerus.PENYIMPAN_ALAMAT_GBR == null || NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN == null) {
            try {
                NgunuiTerus.PENYIMPAN_ALAMAT_GBR = NgunuiTerus.LIST_SEMUA_GBR(this);
                NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN = NgunuiTerus.LIST_SEMUA_KONTEN(this)[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
            urusaneSimbah();
        }else {
            urusaneSimbah();
        }
    }


    private void urusaneSimbah(){
        ndewoDewe = (NdewoDewe) getApplication();
        ndewoDewe.initInterstitial();
        ndewoDewe.loadIntersTitial();
        ndewoDewe.getInterstitial().setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                hitung++;
                //Log.d("iklan", "gagal "+ String.valueOf(hitung));
                if(hitung<loadIklanBerapaKali){
                    if(statusIklan) {
                        ndewoDewe.loadIntersTitial();
                    }
                }
                if(hitung == loadIklanBerapaKali){
                    if(statusIklan) {
                        statusIklan = false;
                        ndewoDewe.setStatusIklan(NgunuiTerus.GAGAL_LOAD_IKLAN);
                        bukaActivity();
                    }
                }
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                if(statusIklan) {
                    Log.d("iklan", "berhasil");
                    statusIklan = false;
                    ndewoDewe.setStatusIklan(NgunuiTerus.BERHASIL_LOAD_IKLAN);
                    bukaActivity();
                }
                super.onAdLoaded();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(statusIklan) {
                    statusIklan = false;
                    ndewoDewe.setStatusIklan(NgunuiTerus.GAGAL_LOAD_IKLAN);
                    bukaActivity();
                }
            }
        }, 15000);
    }

    private void bukaActivity(){
        KataKrep kataKrep = new KataKrep(this);
        kataKrep.setListenerDecrypt(new KataKrep.ListenerDecrypt() {
            @Override
            public void onSelesaiDecrypt(String[] hasil) {
                NgunuiTerus.PENYIMPAN_SEMUA_KATA = hasil;
                Intent intent = new Intent(LoadSemuaUrip.this, MenuUrip.class);
                startActivity(intent);
                finish();
            }
        });
        kataKrep.execute(NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN );
    }
}
