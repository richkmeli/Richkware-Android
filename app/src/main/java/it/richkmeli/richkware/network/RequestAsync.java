package it.richkmeli.richkware.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import it.richkmeli.richkware.network.model.ResponseError;
import it.richkmeli.richkware.util.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequestAsync extends AsyncTask<String, Void, String> {
    ProgressDialog pdLoading;

    Context context;
    OkHttpClient client;
    String url;
    Request request;
    Response response;
    Type type;
    RequestListener requestListener;

    public RequestAsync(Context context, RequestListener requestListener, Type type, String url) {
        this.context = context;
        client = new OkHttpClient();
        this.url = url;
        this.type = type;
        this.requestListener = requestListener;

        pdLoading = new ProgressDialog(context);
        request = new Request.Builder()
                .url(url)
                .get()
                .build();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        pdLoading.setMessage("\tLoading...");
        pdLoading.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String res = null;

        try {
            response = client.newCall(request).execute();
            res = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Gson gson = new Gson();
        //String json = gson.toJson(result);

        JSONObject s = null;
        try {
            JSONArray a = new JSONObject(result).getJSONArray("results");
            s = a.getJSONObject(0);

        } catch (JSONException e) {
            Logger.e("RequestAsync: 4xx ", e);

            try {
                // {"statusCode":404,"error":"Not Found"}
                s = new JSONObject(result);
                type = ResponseError.class;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            //e.printStackTrace();
        }

        //System.out.println("TEST_1: "+String.valueOf(s));

        requestListener.onResult(gson.fromJson(String.valueOf(s), type));

        pdLoading.dismiss();
    }

}
