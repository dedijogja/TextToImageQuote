package info.isakuiki.urip;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.IOException;
import java.util.List;

import info.isakuiki.R;
import info.isakuiki.baper.SemuaGambarBaper;
import info.isakuiki.kekrep.kenten.GenerateBitmapGelapAsync;
import info.isakuiki.kekrep.kenten.KataKrep;
import info.isakuiki.ndewo.NdewoDewe;
import info.isakuiki.ngemeng.NgemengKeJeniAh;
import info.isakuiki.terus.NgunuiTerus;


public class SemuaKoleksiGambarUrip extends AppCompatActivity {

    private SemuaGambarBaper semuaGambarBaper;
    private Context context = this;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_koleksi_gambar_urip);

        NdewoDewe ndewoDewe = (NdewoDewe) getApplication();
        String status = ndewoDewe.getStatusIklan();
        if(status.equals(NgunuiTerus.BERHASIL_LOAD_IKLAN)){
            final AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new NgemengKeJeniAh(this).getAdBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adView.setAdListener(new AdListener(){
                @Override
                public void onAdFailedToLoad(int i) {
                    StartAppSDK.init(SemuaKoleksiGambarUrip.this, new NgemengKeJeniAh(SemuaKoleksiGambarUrip.this).getStartAppId(), false);
                    StartAppAd.disableSplash();
                    ((LinearLayout) findViewById(R.id.iklanSemuaGambar)).removeAllViews();
                    ((LinearLayout) findViewById(R.id.iklanSemuaGambar)).addView(new Banner(SemuaKoleksiGambarUrip.this));
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLoaded() {
                    ((LinearLayout) findViewById(R.id.iklanSemuaGambar)).removeAllViews();
                    ((LinearLayout) findViewById(R.id.iklanSemuaGambar)).addView(adView);
                    super.onAdLoaded();
                }
            });
        }else{
            ((LinearLayout) findViewById(R.id.iklanSemuaGambar)).removeAllViews();
            ((LinearLayout) findViewById(R.id.iklanSemuaGambar)).addView(new Banner(SemuaKoleksiGambarUrip.this));
        }

        if(NgunuiTerus.PENYIMPAN_SEMUA_KATA == null || NgunuiTerus.PENYIMPAN_ALAMAT_GBR == null || NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN == null) {
            try {
                NgunuiTerus.PENYIMPAN_ALAMAT_GBR = NgunuiTerus.LIST_SEMUA_GBR(this);
                NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN = NgunuiTerus.LIST_SEMUA_KONTEN(this)[0];
                KataKrep kataKrep = new KataKrep(this);
                kataKrep.setListenerDecrypt(new KataKrep.ListenerDecrypt() {
                    @Override
                    public void onSelesaiDecrypt(String[] hasil) {
                        NgunuiTerus.PENYIMPAN_SEMUA_KATA = hasil;
                        simbahWesTuo();
                    }
                });
                kataKrep.execute(NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            simbahWesTuo();
        }
    }

    private void simbahWesTuo(){
        if(getIntent().getStringExtra(NgunuiTerus.KODE_INDEX_KONTEN) != null) {
            if (NgunuiTerus.DAFTAR_BITMAP_DARK != null) {
                int posisi = Integer.parseInt(getIntent().getStringExtra(NgunuiTerus.KODE_INDEX_KONTEN));
                String[] temp = new String[NgunuiTerus.DAFTAR_BITMAP_DARK.size()];
                for(int i = 0; i<NgunuiTerus.DAFTAR_BITMAP_DARK.size(); i++){
                    temp[i] = NgunuiTerus.PENYIMPAN_SEMUA_KATA[posisi];
                }
                semuaGambarBaper = new SemuaGambarBaper(context, temp);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                recyclerView = findViewById(R.id.recycleSemuaGambar);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(semuaGambarBaper);
                semuaGambarBaper.notifyDataSetChanged();
            } else {
                GenerateBitmapGelapAsync generateBitmapGelapAsync = new GenerateBitmapGelapAsync(this);
                generateBitmapGelapAsync.setListenerDecrypt(new GenerateBitmapGelapAsync.ListenerDecrypt() {
                    @Override
                    public void onSelesaiDecrypt(List<Bitmap> hasilDeskripsi) {
                        NgunuiTerus.DAFTAR_BITMAP_DARK = hasilDeskripsi;

                        int posisi = Integer.parseInt(getIntent().getStringExtra(NgunuiTerus.KODE_INDEX_KONTEN));
                        String[] temp = new String[NgunuiTerus.DAFTAR_BITMAP_DARK.size()];
                        for(int i = 0; i<NgunuiTerus.DAFTAR_BITMAP_DARK.size(); i++){
                            temp[i] = NgunuiTerus.PENYIMPAN_SEMUA_KATA[posisi];
                        }

                        semuaGambarBaper = new SemuaGambarBaper(context, temp);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                        recyclerView = findViewById(R.id.recycleSemuaGambar);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(semuaGambarBaper);
                        semuaGambarBaper.notifyDataSetChanged();
                    }
                });
                generateBitmapGelapAsync.execute(NgunuiTerus.PENYIMPAN_ALAMAT_GBR);
            }
        } else if(getIntent().getStringExtra(NgunuiTerus.KODE_TEXT) != null){
            if (NgunuiTerus.DAFTAR_BITMAP_DARK != null) {
                String[] temp = new String[NgunuiTerus.DAFTAR_BITMAP_DARK.size()];
                for(int i = 0; i<NgunuiTerus.DAFTAR_BITMAP_DARK.size(); i++){
                    temp[i] = getIntent().getStringExtra(NgunuiTerus.KODE_TEXT);
                }

                semuaGambarBaper = new SemuaGambarBaper(context, temp);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                recyclerView = findViewById(R.id.recycleSemuaGambar);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(semuaGambarBaper);
                semuaGambarBaper.notifyDataSetChanged();
            } else {
                GenerateBitmapGelapAsync generateBitmapGelapAsync = new GenerateBitmapGelapAsync(this);
                generateBitmapGelapAsync.setListenerDecrypt(new GenerateBitmapGelapAsync.ListenerDecrypt() {
                    @Override
                    public void onSelesaiDecrypt(List<Bitmap> hasilDeskripsi) {
                        NgunuiTerus.DAFTAR_BITMAP_DARK = hasilDeskripsi;

                        String[] temp = new String[NgunuiTerus.DAFTAR_BITMAP_DARK.size()];
                        for(int i = 0; i<NgunuiTerus.DAFTAR_BITMAP_DARK.size(); i++){
                            temp[i] = temp[i] = getIntent().getStringExtra(NgunuiTerus.KODE_TEXT);
                        }

                        semuaGambarBaper = new SemuaGambarBaper(context, temp);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                        recyclerView = findViewById(R.id.recycleSemuaGambar);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(semuaGambarBaper);
                        semuaGambarBaper.notifyDataSetChanged();
                    }
                });
                generateBitmapGelapAsync.execute(NgunuiTerus.PENYIMPAN_ALAMAT_GBR);
            }
        } else{
            if (NgunuiTerus.DAFTAR_BITMAP_DARK != null) {
                semuaGambarBaper = new SemuaGambarBaper(context, NgunuiTerus.PENYIMPAN_SEMUA_KATA);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                recyclerView = findViewById(R.id.recycleSemuaGambar);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(semuaGambarBaper);
                semuaGambarBaper.notifyDataSetChanged();
            } else {
                GenerateBitmapGelapAsync generateBitmapGelapAsync = new GenerateBitmapGelapAsync(this);
                generateBitmapGelapAsync.setListenerDecrypt(new GenerateBitmapGelapAsync.ListenerDecrypt() {
                    @Override
                    public void onSelesaiDecrypt(List<Bitmap> hasilDeskripsi) {
                        NgunuiTerus.DAFTAR_BITMAP_DARK = hasilDeskripsi;
                        semuaGambarBaper = new SemuaGambarBaper(context, NgunuiTerus.PENYIMPAN_SEMUA_KATA);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 2);
                        recyclerView = findViewById(R.id.recycleSemuaGambar);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(semuaGambarBaper);
                        semuaGambarBaper.notifyDataSetChanged();
                    }
                });
                generateBitmapGelapAsync.execute(NgunuiTerus.PENYIMPAN_ALAMAT_GBR);
            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
