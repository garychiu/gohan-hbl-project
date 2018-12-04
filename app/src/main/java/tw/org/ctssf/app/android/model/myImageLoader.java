package tw.org.ctssf.app.android.model;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class myImageLoader extends ImageLoader {
    /**
     * Constructs a new ImageLoader.
     *
     * @param queue      The RequestQueue to use for making image requests.
     * @param imageCache The cache to use as an L1 cache.
     */
    public myImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }


    public static ImageListener getImageListener(final ImageView view,
                                                 final int defaultImageResId, final int errorImageResId) {
        return new ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    Bitmap bmp = response.getBitmap();
                    int oldwidth = bmp.getWidth();
                    int oldheight = bmp.getHeight();
                    boolean needScalSize = false;
                    if(oldwidth > 1000  || oldheight >1000){
                        needScalSize = true;
                    }
                    if(needScalSize) {
                        float scaleWidth = 0.1f;
                        float scaleHeight = 0.1f;

                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth, scaleHeight);
                        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, oldwidth,oldheight, matrix, true);
                        view.setImageBitmap(resizedBitmap);
                    }else{
                        view.setImageBitmap(bmp);
                    }
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }
}
