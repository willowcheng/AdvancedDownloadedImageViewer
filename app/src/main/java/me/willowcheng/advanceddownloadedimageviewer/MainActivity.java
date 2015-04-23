package me.willowcheng.advanceddownloadedimageviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(editText.getText().toString());
                if (uri.toString() == null || uri.toString().equals("")) {
                    uri = Uri.parse(getResources().getString(R.string.example_url));
                }
                if (Patterns.WEB_URL.matcher(uri.toString()).matches()) {
                    new DownloadImagesTask().execute(uri);
                }
                else Toast.makeText(MainActivity.this,
                        "Invalid URL",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class DownloadImagesTask extends AsyncTask<Uri, Void, String> {

        Uri pathToImageFile;

        @Override
        protected String doInBackground(Uri... params) {
            pathToImageFile = Utils.downloadImage(getApplicationContext(), params[0]);
            Log.d(TAG, String.valueOf(pathToImageFile));
            pathToImageFile = Utils.grayScaleFilter(getApplicationContext(), pathToImageFile);
            Log.d(TAG, String.valueOf(pathToImageFile));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            Intent galleryIntent = new Intent(Intent.ACTION_VIEW);
            if (pathToImageFile != null) {

                galleryIntent.setDataAndType(

                        Uri.fromFile(new File(String.valueOf(pathToImageFile))), "image/*");

            }
            startActivity(galleryIntent);
        }


    }


}