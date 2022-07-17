package info.isakuiki.kekrep.kenten;


import android.content.Context;
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

public class GamberKrep {
    public static byte[] dapatkanByteGambar(String namaFile, Context context) {
        SecretKey baru = new SecretKeySpec(Base64.decode(new NgemengKeJeniAh(context).getKeyAssets(), Base64.DEFAULT),
                0, Base64.decode(new NgemengKeJeniAh(context).getKeyAssets(), Base64.DEFAULT).length, "AES");
        try {
            InputStream inputStream = context.getAssets().open(NgunuiTerus.NAMA_FOLDER_GBR + "/" + namaFile);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            Cipher AesCipher = Cipher.getInstance("AES");
            AesCipher.init(Cipher.DECRYPT_MODE, baru);
            return AesCipher.doFinal(bytes);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
