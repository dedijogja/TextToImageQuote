package info.isakuiki.baper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.startapp.android.publish.adsCommon.StartAppAd;

import info.isakuiki.R;
import info.isakuiki.ndewo.NdewoDewe;
import info.isakuiki.sampah.ViewWrapper;
import info.isakuiki.terus.NgunuiTerus;
import info.isakuiki.urip.TukangGaeUrip;

public class SemuaGambarBaper extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private String[] kataBaper;

    public SemuaGambarBaper(Context context, String[] kataBaper) {
        this.context = context;
        this.kataBaper = kataBaper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(new RecyPerItem(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int position) {
        final RecyPerItem holder = (RecyPerItem) holders.itemView;

        int[] arr = new int[NgunuiTerus.DAFTAR_BITMAP_DARK.size()];
        for(int i = 0; i<arr.length; i++){
            arr[i] = i;
        }
        int[] newArr = rotateArray((position*-1), arr);
        holder.imgCuplikan.setImageBitmap(NgunuiTerus.DAFTAR_BITMAP_DARK.get(newArr[0]));
        holder.textView.setText(kataBaper[position]);
        holder.textView.setTextColor(NgunuiTerus.KUMPULAN_WARNA_TEXT[NgunuiTerus.INDEX_WARNA(position)]);

        holder.tampunganList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) context;
                final NdewoDewe app = (NdewoDewe) activity.getApplication();
                String status = app.getStatusIklan();
                if(status.equals(NgunuiTerus.BERHASIL_LOAD_IKLAN)) {
                    if (!app.isBolehMenampilkanIklanHitung() || !app.isBolehMenampilkanIklanWaktu()
                            || !app.getInterstitial().isLoaded()) {
                        nyetarUrip(position);
                    }
                    app.getInterstitial().setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            nyetarUrip(position);

                            app.loadIntersTitial();
                            super.onAdClosed();
                        }
                        @Override
                        public void onAdFailedToLoad(int i) {
                            if(app.getHitungFailed() < 2 ) {
                                app.loadIntersTitial();
                                app.setHitungFailed();
                            }
                            super.onAdFailedToLoad(i);
                        }

                        @Override
                        public void onAdLoaded() {
                            app.setHitungFailed(0);
                            super.onAdLoaded();
                        }
                    });
                    app.tampilkanInterstitial();
                }else{
                    if(app.getPenghitungStartApp() == 0) {
                       // Log.d("iklan", "startApp inters tampil " + app.getPenghitungStartApp());
                        app.setPenghitungStartApp(1);
                        nyetarUrip(position);
                        StartAppAd.showAd(context);
                    }else{
                        //Log.d("iklan", "startApp tidak tampil " + app.getPenghitungStartApp());
                        app.setPenghitungStartApp(0);
                        nyetarUrip(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return kataBaper.length;
    }


    class RecyPerItem extends FrameLayout {
        private RelativeLayout tampunganList;
        private ImageView imgCuplikan;
        private TextView textView;
        public RecyPerItem(Context context) {
            super(context);
            inflate(context, R.layout.design_item_gambar, this);
            tampunganList = (RelativeLayout) findViewById(R.id.tampungan_list);
            imgCuplikan = (ImageView) findViewById(R.id.cuplikanGambar);
            textView = (TextView) findViewById(R.id.textSemuaGambar);
        }
    }

    private int[] rotateArray(int n, int[] data){
        // rotating left?
        if(n < 0) {
            n = -n % data.length; // convert to +ve number specifying how
            // many positions left to rotate & mod
            n = data.length - n;  // rotate left by n = rotate right by length - n
        }
        int[] result = new int[data.length];
        for(int i = 0; i < data.length; i++){
            result[(i+n) % data.length ] = data[i];
        }
        return result;
    }


    private void nyetarUrip(int position){
        Intent intent = new Intent(context, TukangGaeUrip.class);
        intent.putExtra(NgunuiTerus.KODE_INDEX_KONTEN, String.valueOf(position));
        intent.putExtra(NgunuiTerus.KODE_TEXT, kataBaper[position]);
        context.startActivity(intent);
    }

}
