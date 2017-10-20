package com.kumalalatif.getwebpagesourcecode;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by User on 20/10/2017.
 */

public class GetSourceCode extends AsyncTaskLoader<String> {
    private String url_link;

    public GetSourceCode(Context context, String url_link) {
        super(context);
        this.url_link=url_link;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        InputStream inputStream;

        try{
            URL url=new URL(url_link);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(20000);
            connection.setRequestMethod("GET");
            connection.connect();

            inputStream=connection.getInputStream();

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder=new StringBuilder();
            String line="";

            while((line=bufferedReader.readLine()) !=null){
                stringBuilder.append(line +"\n");
            }

            bufferedReader.close();
            inputStream.close();
            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Errors, try change protocol to http or https";
    }
}
