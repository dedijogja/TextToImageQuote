package info.isakuiki.babu;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class KotakAsMin extends RelativeLayout {

    public KotakAsMin(Context context) {
        super(context);
    }

    public KotakAsMin(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KotakAsMin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KotakAsMin(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        int b = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(b, b);
    }

}
