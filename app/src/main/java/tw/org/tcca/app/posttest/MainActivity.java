package tw.org.tcca.app.posttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            values.put("cname", "brad123");
            values.put("tel", "9487945123");
            values.put("addr", "abcdefg");
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
            }





        }catch (Exception e){

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
