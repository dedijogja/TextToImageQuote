package info.isakuiki.urip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import info.isakuiki.R;
import info.isakuiki.baper.FavoritBaper;
import info.isakuiki.kekrep.kenten.KataKrep;
import info.isakuiki.sampah.JarakSampah;
import info.isakuiki.terus.NgewangiPaporit;
import info.isakuiki.terus.NgunuiTerus;


public class SemuaFavoritUrip extends AppCompatActivity {

    private Context context = this;
    private int posisi = 0;
    private String[] semuaFavorit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_favorit_urip);

        if(NgunuiTerus.PENYIMPAN_SEMUA_KATA == null || NgunuiTerus.PENYIMPAN_ALAMAT_GBR == null || NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN == null) {
            try {
                NgunuiTerus.PENYIMPAN_ALAMAT_GBR = NgunuiTerus.LIST_SEMUA_GBR(this);
                NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN = NgunuiTerus.LIST_SEMUA_KONTEN(this)[0];
                KataKrep kataKrep = new KataKrep(this);
                kataKrep.setListenerDecrypt(new KataKrep.ListenerDecrypt() {
                    @Override
                    public void onSelesaiDecrypt(String[] hasil) {
                        NgunuiTerus.PENYIMPAN_SEMUA_KATA = hasil;
                        mbahTuo();
                    }
                });
                kataKrep.execute(NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            mbahTuo();
        }

    }

    private void mbahTuo(){

        List<Integer> semuafavor = NgewangiPaporit.getDataFavorit(this);
        if(!semuafavor.isEmpty()) {
            findViewById(R.id.viewJikaKosong).setVisibility(View.GONE);
            findViewById(R.id.viewJikaTidakKosong).setVisibility(View.VISIBLE);

            semuaFavorit = new String[NgewangiPaporit.getDataFavorit(this).size()];
            for (int i = 0; i < NgewangiPaporit.getDataFavorit(this).size(); i++) {
                semuaFavorit[i] = NgunuiTerus.PENYIMPAN_SEMUA_KATA[semuafavor.get(i)];
            }

            ubahTeks(posisi);
            RecyclerView recyclerView = findViewById(R.id.recycleSemuaFavorit);
            FavoritBaper bookmarkedQuotesAdapter = new FavoritBaper(this, semuaFavorit);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new JarakSampah(5, this));
            recyclerView.setAdapter(bookmarkedQuotesAdapter);


            findViewById(R.id.tmblApply).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    posisi = 0;
                    mbahTuo();
                    Toast.makeText(context, "Memuat ulang...", Toast.LENGTH_SHORT).show();
                }
            });

            findViewById(R.id.tmblFavoritFavorit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NgewangiPaporit.apakahSudahterbookmark(NgewangiPaporit.getIndexSesungguhnya(semuaFavorit[posisi]), context)){
                        if(NgewangiPaporit.hapusDariFavorit(NgewangiPaporit.getIndexSesungguhnya(semuaFavorit[posisi]), context)) {
                            ((ImageView) findViewById(R.id.tmblFavoritFavorit)).setImageResource(R.drawable.b_off);
                            Toast.makeText(context, "Dihapus dari favorit!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Terjadi kesalahan tak dikekal!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if(NgewangiPaporit.simpanKeFavorit(NgewangiPaporit.getIndexSesungguhnya(semuaFavorit[posisi]), context)){
                            ((ImageView) findViewById(R.id.tmblFavoritFavorit)).setImageResource(R.drawable.b_on);
                            Toast.makeText(context, "Ditambahkan ke favorit!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "Terjadi kesalahan tak dikekal!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            findViewById(R.id.tmblGenerateFavorit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SemuaFavoritUrip.this, SemuaKoleksiGambarUrip.class);
                    intent.putExtra(NgunuiTerus.KODE_INDEX_KONTEN, String.valueOf(NgewangiPaporit.getIndexSesungguhnya(semuaFavorit[posisi])));
                    startActivity(intent);
                }
            });
        }else{
            findViewById(R.id.viewJikaKosong).setVisibility(View.VISIBLE);
            findViewById(R.id.viewJikaTidakKosong).setVisibility(View.GONE);
            findViewById(R.id.bukaSemuaKoleksiKata).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SemuaFavoritUrip.this, SemuaKoleksiUrip.class));
                }
            });
            findViewById(R.id.bukaSemuaKoleksiGambar).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SemuaFavoritUrip.this, SemuaKoleksiGambarUrip.class));
                }
            });
            findViewById(R.id.refreshFavorit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(NgewangiPaporit.getDataFavorit(context).isEmpty()){
                        Toast.makeText(SemuaFavoritUrip.this, "Daftar Favorit Masih Kosong!", Toast.LENGTH_SHORT).show();
                    }else{
                        posisi = 0;
                        mbahTuo();
                    }
                }
            });
        }
    }

    public void setPosisi(int posisi){
        this.posisi = posisi;
        ubahTeks(this.posisi);
        if(NgewangiPaporit.apakahSudahterbookmark(NgewangiPaporit.getIndexSesungguhnya(semuaFavorit[posisi]), context)){
            ((ImageView) findViewById(R.id.tmblFavoritFavorit)).setImageResource(R.drawable.b_on);
        }else{
            ((ImageView) findViewById(R.id.tmblFavoritFavorit)).setImageResource(R.drawable.b_off);
        }
    }

    private void ubahTeks(int posisi){
        ((TextView) findViewById(R.id.textPreviewFavorit)).setText(semuaFavorit[posisi]);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
