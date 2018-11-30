package tw.org.ctssf.app.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Gary on 2018/10/9.
 */

public class LoadPerferenceDataClient {
    private ScheduledThreadPoolExecutor mExecutor = null;

    public void GetSharedPreferences(Context c, String perferencekey, int mode, PerferenceDataTaskCallback callback){
        if(mExecutor == null) {
            mExecutor = new ScheduledThreadPoolExecutor(5, new ScheduledThreadPoolExecutor.DiscardOldestPolicy());
        }
        LoadPerferenceTask task = new LoadPerferenceTask(c, perferencekey, mode, callback);
        task.executeOnExecutor(mExecutor, perferencekey);
    }

    private class LoadPerferenceTask extends AsyncTask<String, Void, String> {
        int mMode = 0;
        String mKey = null;
        Context mContext;
        PerferenceDataTaskCallback mCallback = null;

        public LoadPerferenceTask(Context c, String key, int mode, PerferenceDataTaskCallback callback) {
            mContext = c;
            mKey = key;
            mMode = mode;
            mCallback = callback;
        }

        @Override
        protected String doInBackground(String... params) {
            String data = null;
            SharedPreferences spref = mContext.getSharedPreferences(Utils.SHAREED_PREFERENCE_KEY, mMode);
            data  = spref.getString(mKey, null);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            if(mCallback != null) {
                mCallback.onResponse(result);
            }
        }
    }

    public interface PerferenceDataTaskCallback {
        void onResponse(String result);
    }
}
