package edu.wt.w02c;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    public final String FILENAME = "settings.txt";
    public int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        String savedString;

        if (isExternalStorageReadable())
            try {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(path, FILENAME);

                BufferedReader br = new BufferedReader(new FileReader(file));
                savedString = br.readLine();
                br.close();
            } catch (FileNotFoundException e) {
                Log.e("Pliki", "Brak pliku do odczytu");
                savedString = "brak";
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Pliki", "IOException przy odczycie");
                savedString = "brak";
                e.printStackTrace();
            }
        else
            savedString = "brak";

        TextView tv = (TextView) findViewById(R.id.savedString);
        tv.setText(savedString);
        // Od API 23 trzeba sprawdzać czy apliacja ma prawo do ryzykownych czynności
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Jeśli nie - prosimy użytkownika o pozwolenie
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        TextView tv = (TextView) findViewById(R.id.savedString);
        String str = tv.getText().toString();

        if(isExternalStorageWritable())
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path, FILENAME);

            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(str);
            bw.close();
        } catch (IOException e) {
            Log.e("Pliki", "IOException przy zapisie");
        }
        else
            Log.e("Pliki", "Nie mogę zapisywać na karcie!");
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
