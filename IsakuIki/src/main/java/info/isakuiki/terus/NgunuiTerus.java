package info.isakuiki.terus;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NgunuiTerus {
    public static final String DEV_NAME = "ISAKUIKI CORP";

    public static final String NAMA_FOLDER_KONTEN = "pitutur";
    public static final String NAMA_FOLDER_GBR = "gember";
    public static final String NAMA_FOLDER_DRAWABLE = "drawable";

    public static final String GAGAL_LOAD_IKLAN = "iklanGagalDiload";
    public static final String BERHASIL_LOAD_IKLAN = "iklanBerhasilDIload";
    public static final String KODE_INDEX_KONTEN = "posisi";
    public static final String KODE_FONT_SIZE = "fontsize";
    public static final String KODE_TEXT = "kodetext";

    public static int INDEX_WARNA(int posisi){
        int[] arr = new int[KUMPULAN_WARNA_TEXT.length];
        for(int i = 0; i<arr.length; i++){
            arr[i] = i;
        }
        int[] newArr = rotateArray((posisi*-1), arr);
        return newArr[0];
    }

    public static final int[] KUMPULAN_WARNA_TEXT = {
            Color.parseColor("#D24D57"),
            Color.parseColor("#F22613"),
            Color.parseColor("#F1A9A0"),
            Color.parseColor("#DCC6E0"),
            Color.parseColor("#AEA8D3"),
            Color.parseColor("#EEEEEE"),
            Color.parseColor("#F9690E"),
            Color.parseColor("#81CFE0"),
            Color.parseColor("#C5EFF7"),
            Color.parseColor("#26C281"),
            Color.parseColor("#ECF0F1"),
            Color.parseColor("#5C97BF"),
            Color.parseColor("#87D37C"),
            Color.parseColor("#26A65B"),
            Color.parseColor("#1BBC9B"),
            Color.parseColor("#C8F7C5"),
            Color.parseColor("#2ECC71"),
            Color.parseColor("#E9D460"),
            Color.parseColor("#E87E04"),
            Color.parseColor("#D35400"),
            Color.parseColor("#26A65B"),
            Color.parseColor("#BFBFBF"),
    };

    private static int[] rotateArray(int n, int[] data){
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

    public static String[] LIST_SEMUA_KONTEN(Context context) throws IOException {
        List<String> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list(NAMA_FOLDER_KONTEN);
        Collections.addAll(modelist, fileNames);
        String[] dap = new String[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }

    public static String[] LIST_SEMUA_GBR(Context context) throws IOException {
        List<String> modelist = new ArrayList<>();
        String[] fileNames = context.getAssets().list(NAMA_FOLDER_GBR);
        Collections.addAll(modelist, fileNames);
        String[] dap = new String[modelist.size()];
        for(int i = 0; i<modelist.size(); i++){
            dap[i] = modelist.get(i);
        }
        return dap;
    }

    public static String[] PENYIMPAN_SEMUA_KATA = null;
    public static String PENYIMPAN_ALAMAT_KONTEN = null;
    public static String[] PENYIMPAN_ALAMAT_GBR = null;
    public static List<Bitmap> DAFTAR_BITMAP_DARK = null;
}
