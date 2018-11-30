package tw.org.ctssf.app.android.utils;

import android.content.Context;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import tw.org.ctssf.app.android.R;
import tw.org.ctssf.app.android.model.VolleyRequestQueue;
import tw.org.ctssf.app.android.model.myImageLoader;

/**
 * Created by Gary on 2018/10/16.
 */

public class HBLImageLoader {
    public static void loadTeamImage(Context context, String imageUrl, NetworkImageView v){
        ImageLoader imageLoader = VolleyRequestQueue.getInstance(context)
                .getImageLoader();
        imageLoader.get(imageUrl, myImageLoader.getImageListener(v,
                R.mipmap.team_default, R.mipmap.team_default));
    }

    public static void loadPlayerImage(Context context, String imageUrl, NetworkImageView v){
        ImageLoader imageLoader = VolleyRequestQueue.getInstance(context)
                .getImageLoader();
        imageLoader.get(imageUrl, myImageLoader.getImageListener(v,
                R.mipmap.player_default, R.mipmap.player_default));
    }
}
