package com.kumalalatif.getwebpagesourcecode;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    TextView hasil_sourceCode;
    Spinner spin;
    EditText link_URL;
    ArrayAdapter<CharSequence> get_Spinner;
    ProgressBar loading_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spin=(Spinner)findViewById(R.id.spinner);
        link_URL=(EditText)findViewById(R.id.editText);
        hasil_sourceCode=(TextView)findViewById(R.id.SourceCode);
        loading_URL=(ProgressBar)findViewById(R.id.ProgressBar);

        get_Spinner=ArrayAdapter.createFromResource(this,R.array.jenis,android.R.layout.simple_spinner_item);
        get_Spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(get_Spinner);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread paranThread, Throwable paranThrowable) {
                loading_URL.setVisibility(View.GONE);
                Log.e("Error" + Thread.currentThread().getStackTrace()[2],paranThrowable.getLocalizedMessage());
            }
        });
        if (getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Really exit").setMessage("Are you sure ?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.finish();

            }
        })
                .setNegativeButton("No",null);
        AlertDialog alert=builder.create();
        alert.show();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new GetSourceCode(this,args.getString("url_link"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        loading_URL.setVisibility(View.GONE);
        hasil_sourceCode.setText(data);

}
    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public void getSourceCode(View view) {
        String link_url, protokol, url;
        protokol=spin.getSelectedItem().toString();
        url=link_URL.getText().toString();

        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(),inputMethodManager.HIDE_NOT_ALWAYS);

        if(!url.isEmpty()){
            if(url.contains(".")){
                if(checkConnection()){
                    hasil_sourceCode.setText("");
                    loading_URL.setVisibility(view.VISIBLE);
                    link_url=protokol+url;

                    Bundle bundle =new Bundle();
                    bundle.putString("url_link",link_url);
                    getSupportLoaderManager().restartLoader(0,bundle,this);
                }
                else{
                    Toast.makeText(this,"Check your internet connection",Toast.LENGTH_SHORT).show();
                    hasil_sourceCode.setText("No Internet Connection");
                }
            }else{
                hasil_sourceCode.setText("Invalid URL");
            }
        }
        else{
            hasil_sourceCode.setText("URL cannot empty");
        }
    }

    boolean checkConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo != null &&networkInfo.isConnected();
    }
}
