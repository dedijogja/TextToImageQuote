package info.isakuiki.urip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.IOException;

import info.isakuiki.R;
import info.isakuiki.baper.SemuaKoleksiBaper;
import info.isakuiki.kekrep.kenten.KataKrep;
import info.isakuiki.ndewo.NdewoDewe;
import info.isakuiki.ngemeng.NgemengKeJeniAh;
import info.isakuiki.sampah.JarakSampah;
import info.isakuiki.terus.NgewangiPaporit;
import info.isakuiki.terus.NgunuiTerus;

public class SemuaKoleksiUrip extends AppCompatActivity {

    private int posisi = 0;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urip_semua_koleksi);

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
                    StartAppSDK.init(SemuaKoleksiUrip.this, new NgemengKeJeniAh(SemuaKoleksiUrip.this).getStartAppId(), false);
                    StartAppAd.disableSplash();
                    ((LinearLayout)findViewById(R.id.iklanSemuaKoleksi)).removeAllViews();
                    ((LinearLayout)findViewById(R.id.iklanSemuaKoleksi)).addView(new Banner(SemuaKoleksiUrip.this));
                    super.onAdFailedToLoad(i);
                }

                @Override
                public void onAdLoaded() {
                    ((LinearLayout)findViewById(R.id.iklanSemuaKoleksi)).removeAllViews();
                    ((LinearLayout)findViewById(R.id.iklanSemuaKoleksi)).addView(adView);
                    super.onAdLoaded();
                }
            });

        }else{
            ((LinearLayout)findViewById(R.id.iklanSemuaKoleksi)).addView(new Banner(SemuaKoleksiUrip.this));
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
                        urusanEmbah();
                    }
                });
                kataKrep.execute(NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            urusanEmbah();
        }

    }

    private void urusanEmbah(){
        ubahTeks(posisi);
        RecyclerView recyclerView = findViewById(R.id.recycleSemuaKoleksi);
        SemuaKoleksiBaper allQuotesAdapter = new SemuaKoleksiBaper(this, NgunuiTerus.PENYIMPAN_SEMUA_KATA);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new JarakSampah(5, this));
        recyclerView.setAdapter(allQuotesAdapter);
        allQuotesAdapter.notifyDataSetChanged();

        findViewById(R.id.tmblFavorit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NgewangiPaporit.apakahSudahterbookmark(posisi, context)){
                    if(NgewangiPaporit.hapusDariFavorit(posisi, context)) {
                        ((ImageView) findViewById(R.id.tmblFavorit)).setImageResource(R.drawable.b_off);
                        Toast.makeText(context, "Dihapus dari favorit!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "Terjadi kesalahan tak dikekal!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(NgewangiPaporit.simpanKeFavorit(posisi, context)){
                        ((ImageView) findViewById(R.id.tmblFavorit)).setImageResource(R.drawable.b_on);
                        Toast.makeText(context, "Ditambahkan ke favorit!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Terjadi kesalahan tak dikekal!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        findViewById(R.id.tmblGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SemuaKoleksiUrip.this, SemuaKoleksiGambarUrip.class);
                intent.putExtra(NgunuiTerus.KODE_INDEX_KONTEN, String.valueOf(posisi));
                startActivity(intent);
            }
        });



        ((SeekBar) findViewById(R.id.seekBarSemuaKoleksi)).setProgress(25);
        ((SeekBar) findViewById(R.id.seekBarSemuaKoleksi)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView) findViewById(R.id.textPreview)).setTextSize(TypedValue.COMPLEX_UNIT_SP, (progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    public void setPosisi(int posisi){
        this.posisi = posisi;
        ubahTeks(this.posisi);
        if(NgewangiPaporit.apakahSudahterbookmark(posisi, context)){
            ((ImageView) findViewById(R.id.tmblFavorit)).setImageResource(R.drawable.b_on);
        }else{
            ((ImageView) findViewById(R.id.tmblFavorit)).setImageResource(R.drawable.b_off);
        }
    }

    private void ubahTeks(int posisi){
        ((TextView) findViewById(R.id.textPreview)).setText(NgunuiTerus.PENYIMPAN_SEMUA_KATA[posisi]);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
