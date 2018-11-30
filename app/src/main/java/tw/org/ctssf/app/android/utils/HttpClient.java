package tw.org.ctssf.app.android.utils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Gary on 2018/10/2.
 */

public class HttpClient {
    public static final String TAG = "HBL-HttpClient";
    private final int READ_TIMEOUT = 10000;  //ms
    private final int CONN_TIMEOUT = 5000;  //ms
    private final int ACTION_QUERY_GET = 101;
    private final int ACTION_SEND_POST = 102;

    private ScheduledThreadPoolExecutor mExecutor = null;

    // GET
    public void async_query_GET(String targetUrl, String[] cbParams, HttpResponseCallback callback) {
        Log.d(TAG, "async_query_GET");
        if(mExecutor == null) {
            mExecutor = new ScheduledThreadPoolExecutor(5, new ScheduledThreadPoolExecutor.DiscardOldestPolicy());
        }
        SendHttpRequestTask task = new SendHttpRequestTask(ACTION_QUERY_GET, targetUrl, null, null, cbParams, callback);
        task.executeOnExecutor(mExecutor, targetUrl);
    }

    public Bundle query_GET(String targetUrl) {
        HttpURLConnection http = null;
        int respCode = 0;
        String response = null;
        Bundle bundle = null;
        String line = null;
        BufferedReader br = null;

        Log.d(TAG, "query_GET");

        try {
            http = getHttpConnection(targetUrl, "GET");
            if(http != null) {
                respCode = http.getResponseCode();
                if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_FORBIDDEN
                        || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                    response = "";
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "err: " + e.toString());
            e.printStackTrace();
            return null;
        } finally {
            if(http != null) {
                http.disconnect();
            }
        }

        Log.d(TAG, "query_GET code: " + respCode);

        if(response != null && !response.isEmpty()) {
            Log.d(TAG, "query_GET response: " + response);
            if(bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("response", response);
        }
        return bundle;
    }

    // POST
    public void async_send_POST(String targetUrl, HashMap<String, String> params, String[] cbParams, HttpResponseCallback callback) {
        Log.d(TAG, "async_send_POST");
        if(mExecutor == null) {
            mExecutor = new ScheduledThreadPoolExecutor(5, new ScheduledThreadPoolExecutor.DiscardOldestPolicy());
        }
        SendHttpRequestTask task = new SendHttpRequestTask(ACTION_SEND_POST, targetUrl, params, null, cbParams, callback);
        task.executeOnExecutor(mExecutor, targetUrl);
    }

    public Bundle send_POST(String targetUrl, HashMap<String, String> params) {
        HttpURLConnection http = null;
        int respCode = 0;
        String response = null;
        Bundle bundle = null;
        String paramStr = "";
        String line = null;
        BufferedReader br = null;

        Log.d(TAG, "send_POST");

        try {
            if(params != null && !params.isEmpty()) {
                paramStr = getParamString(params);
                Log.d(TAG, "send_POST : " + paramStr);
            }

            http = getHttpConnection(targetUrl, "POST");
            if(http != null) {
                if (paramStr != null && !paramStr.isEmpty()) {
                    OutputStream os = http.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(paramStr);
                    writer.flush();
                    writer.close();
                    os.close();
                }
                respCode = http.getResponseCode();
                if (respCode == HttpURLConnection.HTTP_OK || respCode == HttpURLConnection.HTTP_FORBIDDEN
                        || respCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                    response = "";
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = null;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "err: " + e.toString());
            e.printStackTrace();
            return null;
        } finally {
            if(http != null) {
                http.disconnect();
            }
        }

        Log.d(TAG, "send_POST code: " + respCode);

        if(response != null && !response.isEmpty()) {
            Log.d(TAG, "send_POST response: " + response);
            if(bundle == null) {
                bundle = new Bundle();
            }
            bundle.putString("response", response);
        }
        return bundle;
    }

    private String getParamString(HashMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            try {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return result.toString();
    }

    private HttpURLConnection getHttpConnection(String targetUrl, String method) {
        HttpURLConnection http = null;
        try {
            Log.d(TAG, method + "  " + targetUrl);
            URL url = new URL(targetUrl);
            http = (HttpURLConnection) url.openConnection();
            http.setConnectTimeout(CONN_TIMEOUT);
            http.setReadTimeout(READ_TIMEOUT);
            http.setRequestProperty("Accept",
                    "text/html, application/xhtml+xml, */*");
            http.setRequestMethod(method);
            http.connect();
            if(method.equals("POST")) {
                http.setDoInput(true);
                http.setDoOutput(true);
            }
        } catch (Exception e) {
            http = null;
            Log.e(TAG, "err: "  + e.toString());
            e.printStackTrace();
        }
        return http;
    }

    private class SendHttpRequestTask extends AsyncTask<String, Void, Bundle> {
        int mAction = 0;
        String mTargetUrl = null;
        HashMap<String, String> mParams = null;
        byte[] mData = null;
        String[] mCbParams = null;
        HttpResponseCallback mCallback = null;

        public SendHttpRequestTask(int action, String targetUrl, HashMap<String, String> params, byte[] data, String[] cbParams, HttpResponseCallback callback) {
            mAction = action;
            mTargetUrl = targetUrl;
            mParams = params;
            mData = data;
            mCbParams = cbParams;
            mCallback = callback;
        }

        @Override
        protected Bundle doInBackground(String... params) {
            Bundle result = null;
            switch (mAction) {
                case ACTION_QUERY_GET:
                    result = query_GET(mTargetUrl);
                    break;
                case ACTION_SEND_POST:
                    result = send_POST(mTargetUrl, mParams);
                    break;
            }
            if(result != null) {
                result.putStringArray("cb_params", mCbParams);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Bundle result) {
            if(mCallback != null) {
                mCallback.onResponse(result);
            }
        }
    }

    public interface HttpResponseCallback {
        void onResponse(Bundle result);
    }
}
