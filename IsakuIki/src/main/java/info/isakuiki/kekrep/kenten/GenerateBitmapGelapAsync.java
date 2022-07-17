package info.isakuiki.kekrep.kenten;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import info.isakuiki.ngemeng.NgemengKeJeniAh;
import info.isakuiki.terus.NgunuiTerus;

public class GenerateBitmapGelapAsync extends AsyncTask<String, Void, List<Bitmap>> {
    private ProgressDialog dialog;
    private ListenerDecrypt listenerDecrypt;
    private Context context;

    public GenerateBitmapGelapAsync(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, "Mempersiapkan",
                "Mohon tunggu ...", true);
        dialog.show();
    }

    @Override
    protected List<Bitmap> doInBackground(String... listFileDeskripsi) {
        SecretKey secretKey =  new SecretKeySpec(Base64.decode(new NgemengKeJeniAh(context).getKeyAssets(), Base64.DEFAULT),
                0, Base64.decode(new NgemengKeJeniAh(context).getKeyAssets(), Base64.DEFAULT).length, "AES");
        List<Bitmap> bitmaps = new ArrayList<>();
        for (String aListFileDeskripsi : listFileDeskripsi) {
            try {
                InputStream inputStream = context.getAssets().open(NgunuiTerus.NAMA_FOLDER_GBR + "/" + aListFileDeskripsi);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);
                inputStream.close();

                Cipher AesCipher = Cipher.getInstance("AES");
                AesCipher.init(Cipher.DECRYPT_MODE, secretKey);
                byte[] hasilDekripsi = AesCipher.doFinal(bytes);
                bitmaps.add(darkenBitMap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(hasilDekripsi, 0, hasilDekripsi.length), 100, 100)));
            } catch (IOException | BadPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | NoSuchPaddingException | InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return bitmaps;
    }

    @Override
    protected void onPostExecute(List<Bitmap> hasilDeskripsi) {
        dialog.dismiss();
        listenerDecrypt.onSelesaiDecrypt(hasilDeskripsi);
    }

    public void setListenerDecrypt(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(List<Bitmap> hasilDeskripsi);
    }

    private Bitmap darkenBitMap(Bitmap bm) {
        Canvas canvas = new Canvas(bm);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);
        return bm;
    }
}
