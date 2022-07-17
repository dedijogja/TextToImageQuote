package info.isakuiki.kekrep.kenten;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import info.isakuiki.ngemeng.NgemengKeJeniAh;
import info.isakuiki.terus.NgunuiTerus;

public class KataKrep extends AsyncTask<String, Void, String[]>{

    private Context context;
    private ListenerDecrypt listenerDecrypt;

    public KataKrep(Context context) {
        this.context = context;
    }

    @Override
    protected String[] doInBackground(String... params) {
        SecretKey baru =  new SecretKeySpec(Base64.decode(new NgemengKeJeniAh(context).getKeyAssets(), Base64.DEFAULT),
                0, Base64.decode(new NgemengKeJeniAh(context).getKeyAssets(), Base64.DEFAULT).length, "AES");

        String[] lines = null;
        try {
            InputStream inputStream = context.getAssets().open(NgunuiTerus.NAMA_FOLDER_KONTEN + "/" + params[0]);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, baru);
            byte[] byteResult = AesCipher.doFinal(bytes);

            lines = new String(byteResult).split("[\r\n]+");

        } catch (NoSuchAlgorithmException | IllegalBlockSizeException | InvalidKeyException | IOException | BadPaddingException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return lines;
    }

    @Override
    protected void onPostExecute(String[] hasil) {
        listenerDecrypt.onSelesaiDecrypt(hasil);
    }

    public void setListenerDecrypt(ListenerDecrypt listenerDecrypts){
        if(listenerDecrypt == null){
            this.listenerDecrypt = listenerDecrypts;
        }
    }

    public interface ListenerDecrypt{
        void onSelesaiDecrypt(String[] hasil);
    }
}
