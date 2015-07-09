package br.com.fmproc.panki.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.fmproc.panki.R;
import br.com.fmproc.panki.R.id;
import br.com.fmproc.panki.R.layout;

public class LoadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_load);
        setTitle("Escolha o Deck");

        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/panki");
        final ListView lv = (ListView)findViewById(id.listDecks);
        List<String> files = new ArrayList<String>();
        for(String s: dir.list()){
            if (!s.contains(".history.txt")) {
                files.add(s.replaceAll(".txt", ""));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, layout.simplerow, files);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  itemValue    = (String) lv.getItemAtPosition(position);

                Intent intent = new Intent(LoadActivity.this, MainActivity.class);
                intent.putExtra("currentDeck", itemValue);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.load, menu);
        return true;
    }

}
