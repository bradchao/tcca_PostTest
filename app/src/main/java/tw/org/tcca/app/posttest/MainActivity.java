package tw.org.tcca.app.posttest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private EditText cname, tel, addr;
    private UIHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new UIHandler();

        cname = findViewById(R.id.inputCName);
        tel = findViewById(R.id.inputTel);
        addr = findViewById(R.id.inputAddr);

    }

    public void addeCust(View view) {
        new Thread(){
            @Override
            public void run() {
                addRemote();
            }
        }.start();
    }

    private void addRemote(){
        try{
            URL url = new URL("https://www.bradchao.com/autumn/addCust.php");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(3*1000);
            conn.setConnectTimeout(3*1000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            ContentValues values = new ContentValues();
            values.put("cname", cname.getText().toString());
            values.put("tel", tel.getText().toString());
            values.put("addr", addr.getText().toString());
            String query = queryString(values);
            Log.v("brad", query);

            OutputStream out =  conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.write(query);
            writer.flush();
            out.close();

            conn.connect();

            int rcode = conn.getResponseCode();
            String rmesg = conn.getResponseMessage();
            Log.v("brad", rcode + ":" + rmesg);

            if (rcode == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String json = reader.readLine();
                Log.v("brad", "json:" + json);
                parseAddJSON(json);
            }

        }catch (Exception e){

        }
    }

    private void parseAddJSON(String json){
        try {
            JSONObject root = new JSONObject(json);
            int status = root.getInt("status");
            handler.sendEmptyMessage(status);
        }catch (Exception e){
            handler.sendEmptyMessage(-1);
        }
    }

    public void fetchCust(View view) {
        new Thread(){
            @Override
            public void run() {
                queryRemote();
            }
        }.start();
    }

    private void queryRemote(){
        try{
            URL url = new URL("https://www.bradchao.com/autumn/fetchCust.php");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(3*1000);
            conn.setConnectTimeout(3*1000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            ContentValues values = new ContentValues();
            values.put("t", "a");
            String query = queryString(values);
            Log.v("brad", query);

            OutputStream out =  conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
            writer.write(query);
            writer.flush();
            out.close();

            conn.connect();

            int rcode = conn.getResponseCode();
            String rmesg = conn.getResponseMessage();
            Log.v("brad", rcode + ":" + rmesg);

            if (rcode == 200){
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String json = reader.readLine();
                Log.v("brad", "json:" + json);
                //parseAddJSON(json);
            }

        }catch (Exception e){

        }
    }

    


    private class UIHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this, msg.what==0?"Add Success!":"Add Failure!", Toast.LENGTH_SHORT).show();
        }
    }


    private String queryString(ContentValues data){
        Set<String> keys = data.keySet();
        StringBuffer sb = new StringBuffer();

        try {
            for (String key : keys) {
                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(data.getAsString(key), "UTF-8"));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length()-1);
        }catch (Exception e){
            return null;
        }

        return  sb.toString();
    }




}
