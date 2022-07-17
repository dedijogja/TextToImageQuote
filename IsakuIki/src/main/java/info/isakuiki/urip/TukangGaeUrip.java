package info.isakuiki.urip;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import info.isakuiki.R;
import info.isakuiki.babu.KotakAsMin;
import info.isakuiki.kekrep.kenten.GamberKrep;
import info.isakuiki.kekrep.kenten.KataKrep;
import info.isakuiki.ndewo.NdewoDewe;
import info.isakuiki.ngemeng.NgemengKeJeniAh;
import info.isakuiki.terus.NgunuiTerus;


public class TukangGaeUrip extends AppCompatActivity {

    private int posisiGambar = 0;
    private int fontSize = 25;
    private String text = NgunuiTerus.DEV_NAME;
    private Context context = this;
    private int warnaText = Color.parseColor("#ffffff");
    private byte[] bytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tukang_gae_urip);

        NdewoDewe ndewoDewe = (NdewoDewe) getApplication();
        String status = ndewoDewe.getStatusIklan();
        if(status.equals(NgunuiTerus.BERHASIL_LOAD_IKLAN)){
            final AdView adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(new NgemengKeJeniAh(this).getAdBanner());
            adView.loadAd(new AdRequest.Builder().build());
            adView.setAdListener(new AdListener(){
                @Override
                public void onAdClosed() {
                    StartAppSDK.init(TukangGaeUrip.this, new NgemengKeJeniAh(TukangGaeUrip.this).getStartAppId(), false);
                    StartAppAd.disableSplash();
                    ((LinearLayout)findViewById(R.id.iklanGaeUrip)).removeAllViews();
                    ((LinearLayout)findViewById(R.id.iklanGaeUrip)).addView(new Banner(TukangGaeUrip.this));
                    super.onAdClosed();
                }

                @Override
                public void onAdLoaded() {
                    ((LinearLayout)findViewById(R.id.iklanGaeUrip)).removeAllViews();
                    ((LinearLayout)findViewById(R.id.iklanGaeUrip)).addView(adView);
                    super.onAdLoaded();
                }
            });

        }else{
            ((LinearLayout)findViewById(R.id.iklanGaeUrip)).addView(new Banner(TukangGaeUrip.this));
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
                        urusanWindu();
                    }
                });
                kataKrep.execute(NgunuiTerus.PENYIMPAN_ALAMAT_KONTEN );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            urusanWindu();
        }

    }

    private void urusanWindu(){
        if(getIntent().getStringExtra(NgunuiTerus.KODE_INDEX_KONTEN)!= null){
            posisiGambar = dapatkanPosisiGambarSesungguhnya(Integer.parseInt(getIntent().getStringExtra(NgunuiTerus.KODE_INDEX_KONTEN)));
            text = NgunuiTerus.PENYIMPAN_SEMUA_KATA[dapatkanPosisiTextSesungguhnya(Integer.parseInt(getIntent().getStringExtra(NgunuiTerus.KODE_INDEX_KONTEN)))];
            warnaText = NgunuiTerus.KUMPULAN_WARNA_TEXT[dapatkanPosisiWarnaSesungguhnya(Integer.parseInt(getIntent().getStringExtra(NgunuiTerus.KODE_INDEX_KONTEN)))];
        }
        if(getIntent().getStringExtra(NgunuiTerus.KODE_TEXT) != null){
            text = getIntent().getStringExtra(NgunuiTerus.KODE_TEXT);
        }

        if(getIntent().getStringExtra(NgunuiTerus.KODE_FONT_SIZE) != null){
            fontSize = Integer.parseInt(getIntent().getStringExtra(NgunuiTerus.KODE_FONT_SIZE));
        }
        if(getIntent().getStringExtra(NgunuiTerus.KODE_TEXT) != null){
            text = getIntent().getStringExtra(NgunuiTerus.KODE_TEXT);
        }

        if(getIntent().getStringExtra(NgunuiTerus.KODE_FONT_SIZE) != null){
            fontSize = Integer.parseInt(getIntent().getStringExtra(NgunuiTerus.KODE_FONT_SIZE));
        }

        bytes = GamberKrep.dapatkanByteGambar(NgunuiTerus.PENYIMPAN_ALAMAT_GBR[posisiGambar], this);
        ((ImageView) findViewById(R.id.gbrGaeUrip)).setImageBitmap(darkenBitMap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true)));
        ((TextView) findViewById(R.id.textGaeUrip)).setText(text);
        ((TextView) findViewById(R.id.textGaeUrip)).setTextSize(fontSize);
        ((TextView) findViewById(R.id.textGaeUrip)).setTextColor(warnaText);

        ((SeekBar) findViewById(R.id.seekbarTukangGaeUrip)).setProgress(25);
        ((SeekBar) findViewById(R.id.seekbarTukangGaeUrip)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ((TextView) findViewById(R.id.textGaeUrip)).setTextSize(TypedValue.COMPLEX_UNIT_SP, (progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        urusanMenuBawah();
        urusanMenuAtas();

        if(getIntent().getStringExtra("perintah") != null){
            ColorPickerDialogBuilder
                    .with(context)
                    .setTitle("Pilih Warna")
                    .initialColor(warnaText)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                            ((TextView) findViewById(R.id.textGaeUrip)).setTextColor(selectedColor);
                        }
                    })
                    .setPositiveButton("OK", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            ((TextView) findViewById(R.id.textGaeUrip)).setTextColor(selectedColor);
                            warnaText = selectedColor;
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .build()
                    .show();
        }
    }

    private void urusanMenuAtas(){
        findViewById(R.id.savegaeUrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.gbrTTD).setVisibility(View.VISIBLE);
                Toast.makeText(context, "Memproses, mohon tunggu...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/" + getString(R.string.app_name));
                        myDir.mkdirs();
                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        String fname = getString(R.string.app_name) + "-" + n + ".jpg";
                        File file = new File(myDir, fname);
                        if (file.exists()) file.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            dapatkanBitmap().compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();

                            MediaScannerConnection.scanFile(context, new String[]{
                                            file.getAbsolutePath()},
                                    null, new MediaScannerConnection.OnScanCompletedListener() {
                                        public void onScanCompleted(String path, Uri uri) {

                                        }
                                    });
                            Toast.makeText(context, "Gambar disimpan di folder " + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Gambar gagal disimpan", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2500);
            }
        });
        findViewById(R.id.shareGaeUrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.gbrTTD).setVisibility(View.VISIBLE);
                Toast.makeText(context, "Memproses, mohon tunggu...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String root = Environment.getExternalStorageDirectory().toString();
                        File myDir = new File(root + "/" + getString(R.string.app_name));
                        myDir.mkdirs();
                        Random r = new Random();
                        String fname = "Image-share" +String.valueOf(r.nextInt(999))+ ".jpg";
                        File file = new File(myDir, fname);
                        if (file.exists()) file.delete();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            dapatkanBitmap().compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Uri imageUri = Uri.parse("file://" + file);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                        context.startActivity(Intent.createChooser(intent, "Bagikan menggunakan..."));
                    }
                }, 2500);
            }
        });
        findViewById(R.id.setAsGauUrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.gbrTTD).setVisibility(View.VISIBLE);
                Toast.makeText(context, "Memproses, mohon tunggu...", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "title");
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
                        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        OutputStream outstream;
                        try {
                            outstream = context.getContentResolver().openOutputStream(uri);
                            dapatkanBitmap().compress(Bitmap.CompressFormat.PNG, 100, outstream);
                            outstream.close();
                        } catch (Exception e) {
                            System.err.println(e.toString());
                        }
                        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setDataAndType(uri, "image/*");
                        intent.putExtra("mimeType", "image/*");
                        context.startActivity(Intent.createChooser(intent, "Atur sebagai ..."));
                    }

                }, 2500);
            }
        });
    }

    private Bitmap dapatkanBitmap(){
        KotakAsMin view = findViewById(R.id.kotakLayout);
        view.setDrawingCacheEnabled(false);
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        findViewById(R.id.gbrTTD).setVisibility(View.GONE);
        return bitmap;
    }

    private void urusanMenuBawah(){
        findViewById(R.id.bawahUbahText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Ubah Teks");

                final EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setText(text);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ((TextView) findViewById(R.id.textGaeUrip)).setText(input.getText().toString());
                                text = ((TextView) findViewById(R.id.textGaeUrip)).getText().toString();
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

        findViewById(R.id.bawahWarnaText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle("Pilih Warna")
                        .initialColor(warnaText)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                ((TextView) findViewById(R.id.textGaeUrip)).setTextColor(selectedColor);
                            }
                        })
                        .setPositiveButton("OK", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                ((TextView) findViewById(R.id.textGaeUrip)).setTextColor(selectedColor);
                                warnaText = selectedColor;
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        findViewById(R.id.bawahTextStyle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence pilihan[] = new CharSequence[] {"Normal", "Bold", "Italic", "Bold Italic"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Text Style");
                builder.setItems(pilihan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int posisi) {
                        switch (posisi){
                            case 0:
                                ((TextView) findViewById(R.id.textGaeUrip)).setTypeface(null, Typeface.NORMAL);
                                break;
                            case 1:
                                ((TextView) findViewById(R.id.textGaeUrip)).setTypeface(null, Typeface.BOLD);
                                break;
                            case 2:
                                ((TextView) findViewById(R.id.textGaeUrip)).setTypeface(null, Typeface.ITALIC);
                                break;
                            case 3:
                                ((TextView) findViewById(R.id.textGaeUrip)).setTypeface(null, Typeface.BOLD_ITALIC);
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        findViewById(R.id.bawahPresetBG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence pilihan[] = new CharSequence[] {"Normal", "Digelapkan", "Dicerahkan", "Normal Blur", "Digelapkan Blur", "Dicerahkan Blur"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Background Preset");
                builder.setItems(pilihan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int posisi) {
                        switch (posisi){
                            case 0:
                                ((ImageView) findViewById(R.id.gbrGaeUrip)).setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                break;
                            case 1:
                                ((ImageView) findViewById(R.id.gbrGaeUrip)).setImageBitmap(darkenBitMap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true)));
                                break;
                            case 2:
                                ((ImageView) findViewById(R.id.gbrGaeUrip)).setImageBitmap(lightenBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true)));
                                break;
                            case 3:
                                ((ImageView) findViewById(R.id.gbrGaeUrip)).setImageBitmap(fastblur(BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true),
                                        1, 10));
                                break;
                            case 4:
                                ((ImageView) findViewById(R.id.gbrGaeUrip)).setImageBitmap(fastblur(darkenBitMap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true)),
                                        1, 10));
                                break;
                            case 5:
                                ((ImageView) findViewById(R.id.gbrGaeUrip)).setImageBitmap(fastblur(lightenBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length).copy(Bitmap.Config.ARGB_8888, true)),
                                        1, 10));
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {
        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        if (radius < 1) {
            return (null);
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }
        yw = yi = 0;
        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;
        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;
            for (x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;
                sir = stack[i + radius];
                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];
                rbs = r1 - Math.abs(i);
                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];
                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;
                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];
                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];
                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];
                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];
                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;
                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];
                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];
                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];
                yi += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }

    private Bitmap darkenBitMap(Bitmap bm) {
        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);
        return bm;
    }

    private Bitmap lightenBitmap(Bitmap bm) {
        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);
        return bm;
    }

    private int dapatkanPosisiGambarSesungguhnya(int posisi){
        int[] arr = new int[NgunuiTerus.PENYIMPAN_ALAMAT_GBR.length];
        for(int i = 0; i<arr.length; i++){
            arr[i] = i;
        }
        int[] newArr = rotateArray((posisi*-1), arr);
        return newArr[0];
    }

    private int dapatkanPosisiTextSesungguhnya(int posisi){
        int[] arr = new int[NgunuiTerus.PENYIMPAN_SEMUA_KATA.length];
        for(int i = 0; i<arr.length; i++){
            arr[i] = i;
        }
        int[] newArr = rotateArray((posisi*-1), arr);
        return newArr[0];
    }

    private int dapatkanPosisiWarnaSesungguhnya(int posisi){
        int[] arr = new int[NgunuiTerus.KUMPULAN_WARNA_TEXT.length];
        for(int i = 0; i<arr.length; i++){
            arr[i] = i;
        }
        int[] newArr = rotateArray((posisi*-1), arr);
        return newArr[0];
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

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
           finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan lagi untuk kembali!", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
