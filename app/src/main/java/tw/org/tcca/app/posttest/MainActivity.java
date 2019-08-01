package tw.org.tcca.app.posttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.net.URL;

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






        }catch (Exception e){

        }
    }


}
