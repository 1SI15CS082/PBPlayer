package com.example.puru.pbplayer;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> arrayList;
    final  int MY_PERMISSION= 1;
    ArrayList<String> fileArrayList=new ArrayList<>();
    ArrayList<File> myvideos=findvideos(Environment.getExternalStorageDirectory());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION);

            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION);

            }
        }
        else {
            DoStuff();
        }
    }
    public void DoStuff(){

        lv=(ListView)findViewById(R.id.lv);
        arrayList=new ArrayList<>();

        getvideo();
        ArrayAdapter<String> adopter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        lv.setAdapter(adopter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,"Selected"+parent.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
                Intent i1 = new Intent(MainActivity.this,VedioActivity.class);

                String st= fileArrayList.get(position);
                Toast.makeText(MainActivity.this,"path:"+st,Toast.LENGTH_LONG).show();

                //i1.putExtra("uri",st);
                i1.putExtra("position",position);
                i1.putExtra("list",fileArrayList);
                startActivity(i1);

            }
        });

    }

    public  void  getvideo(){
        for (int i=0;i<myvideos.size();i++)
        {
            //toast(mysongs.get(i).getName().toString());

            //Toast.makeText(MainActivity.this," "+myvideos.get(i).getName().toString(),Toast.LENGTH_SHORT).show();
            arrayList.add(myvideos.get(i).getName().toString());
            fileArrayList.add(myvideos.get(i).toString());


        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {

        switch (requestCode){
            case   MY_PERMISSION:
                if(grantResults.length >0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(MainActivity.this,"Permission granted",Toast.LENGTH_LONG).show();
                        DoStuff();

                    }
                    else {
                        Toast.makeText(MainActivity.this,"NO Permission granted",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
        }return;
    }

    public ArrayList<File> findvideos (File file)
    {
        ArrayList<File> al = new ArrayList<File>();
        File [] files =file.listFiles();
        for(File singleFile: files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                al.addAll(findvideos(singleFile));
                //adopter.addAll(findsongs(singleFile).toString());
            }
            else
            {
                if(singleFile.getName().endsWith(".mp4"))
                {
                    al.add(singleFile);
                    //adopter.add(singleFile.toString());
                }
            }
        }
        return  al;
    }

}
