package com.firecrew.firecrew.activities;

//copied data from inventory to match up
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.content.pm.PackageManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.hardware.Camera;
import android.widget.TextView;
import com.firecrew.firecrew.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.firebase.database.ValueEventListener;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.integration.android.IntentResult;

@SuppressWarnings("ALL")
public class UserActivity extends AppCompatActivity {
    Button b1, b2, b3;
    final Activity activity = this;
    EditText et, et7, et8, et9, et10, et11;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth fauth;

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ImageView iv;
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    private ActionBarDrawerToggle abt;

    Button AppCompatButton;
    TextView result;
    String q;
    EditText ed;

    //assigns buttons and deals with permissions of the camera//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        result = (TextView) findViewById(R.id.EditText7);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

       et7 = (EditText) findViewById(R.id.EditText7);
       et8 = (EditText) findViewById(R.id.TextInputEditText8);
        et9 = (EditText) findViewById(R.id.TextInputEditText9);
        et10 = (EditText) findViewById(R.id.TextInputEditText10);
        et11 = (EditText) findViewById(R.id.TextInputEditText11);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("All Barcodes");
        fauth = FirebaseAuth.getInstance();
        et = (EditText) findViewById(R.id.EditText7);

        b1 = (Button) findViewById(R.id.AppCompatButton);
        b2 = (Button) findViewById(R.id.AppCompatButton7);
        b3 = (Button) findViewById(R.id.AppCompatButton14);
        ed=(EditText)findViewById(R.id.EditText7);


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r=new Intent(UserActivity .this,LoginActivity.class);
                startActivity(r);
                finish();
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String B=et7.getText().toString();
                String PN=et8.getText().toString();
                String D=et9.getText().toString();
                String T=et10.getText().toString();
                String PD=et11.getText().toString();

                // String G=et4.getText().toString();
                Product PO=new Product(B,PN,PD,T,D);
                reference.child(B).setValue(PO);
                Toast.makeText(getApplicationContext(),"Data Successfully Saved",Toast.LENGTH_LONG).show();;
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator in = new IntentIntegrator(activity);
                in.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                in.setPrompt("scan");
                in.setCameraId(0);
                in.setBeepEnabled(false);
                //in.setBarcodeImageEnabled(false);
                in.initiateScan();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Log.d("UserActivity","Cancelled scan");
                Toast.makeText(this,"Cancelled",Toast.LENGTH_LONG).show();;
            }
            else
            {
                Log.d("UserActivity","Scanned");
                reference.child(result.getContents()).addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
//                Product p= dataSnapshot.getValue(Product.class);
                        //              Log.e("Name",p.Pname);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });
                ed.setText(result.getContents());
                Toast.makeText(this, "Scanned", Toast.LENGTH_SHORT).show();
                // tv.setText(result.getContents());
                q=ed.getText().toString();
            }
        }
        else
        {
            super.onActivityResult(requestCode,resultCode,data);
        }
        // super.onActivityResult(requestCode, resultCode, data);
    }



                    //deals with pressing back button//
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

       builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                finish();
                ///dialog.dismiss();
            }
             });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                /// Do nothing
               dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        }










}











