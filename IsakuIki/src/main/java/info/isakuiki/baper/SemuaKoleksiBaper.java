package info.isakuiki.baper;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import info.isakuiki.R;
import info.isakuiki.sampah.ViewWrapper;
import info.isakuiki.terus.NgunuiTerus;
import info.isakuiki.urip.SemuaKoleksiUrip;

public class SemuaKoleksiBaper extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private String[] semuaKoleksi;

    public SemuaKoleksiBaper(Context context, String[] allQuotes) {
        this.context = context;
        this.semuaKoleksi = allQuotes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<>(new RecyPerItem(context));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holders, final int position) {
        final RecyPerItem holder = (RecyPerItem) holders.itemView;
        holder.tampunganList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SemuaKoleksiUrip semuaKoleksiUrip = (SemuaKoleksiUrip) context;
                semuaKoleksiUrip.setPosisi(position);
            }
        });

        holder.textPerItem.setText(semuaKoleksi[position]);
        holder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("tercopy", semuaKoleksi[position]);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Teks dicopy ke Clip Board", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, semuaKoleksi[position]);
                context.startActivity(Intent.createChooser(sharingIntent, "Bagikan dengan..."));
            }
        });
    }

    @Override
    public int getItemCount() {
        return semuaKoleksi.length;
    }


    class RecyPerItem extends FrameLayout {
        private RelativeLayout tampunganList;
        private TextView textPerItem;
        private ImageView btnCopy;
        private ImageView btnShare;
        public RecyPerItem(Context context) {
            super(context);
            inflate(context, R.layout.design_item, this);
            tampunganList = (RelativeLayout) findViewById(R.id.tampungan_list);
            textPerItem = (TextView) findViewById(R.id.textPerItem);
            btnCopy = (ImageView) findViewById(R.id.btnCOpyText);
            btnShare = (ImageView) findViewById(R.id.btnShareText);
        }
    }
}
