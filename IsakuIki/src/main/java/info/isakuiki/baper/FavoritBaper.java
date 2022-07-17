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
import info.isakuiki.urip.SemuaFavoritUrip;
import info.isakuiki.urip.SemuaKoleksiUrip;

public class FavoritBaper extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private String[] kataBaper;

    public FavoritBaper(Context context, String[] kataBaper) {
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
        holder.tampunganList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SemuaFavoritUrip semuaKoleksiUrip = (SemuaFavoritUrip) context;
                semuaKoleksiUrip.setPosisi(position);
            }
        });

        holder.textPerItem.setText(kataBaper[position]);
        holder.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("tercopy", kataBaper[position]);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Teks dicopy ke Clip Board", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, kataBaper[position]);
                context.startActivity(Intent.createChooser(sharingIntent, "Bagikan dengan..."));
            }
        });
    }

    @Override
    public int getItemCount() {
        return kataBaper.length;
    }


    class RecyPerItem extends FrameLayout {
        private RelativeLayout tampunganList;
        private TextView textPerItem;
        private ImageView btnCopy, btnShare;
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
