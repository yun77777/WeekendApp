package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final static int REQUEST_IMAGE_CAPTURE = 672;
    private String imageFilePath;
    private Uri photoUri;

    private Button btn_move;
    private Button btn_shared;
    private Button btn_wv;
    private Button btn_open;
    private Button btn_capture;

    private Button btn_start;
    private Button btn_stop;

    Thread thread;
    boolean isThread = false;

    private EditText et_test;
    private String str;
    private ImageView iv_result;

    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_stop = (Button)findViewById(R.id.btn_stop);

        // thread is started
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isThread = true;
                thread = new Thread() {
                  public void run() {
                      while(isThread) {
                          try {
                              sleep(5000);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                        handler.sendEmptyMessage(0);
                      }
                  }
                };

                thread.start();
            }
        });

        // thread is stopped
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isThread = false;
            }
        });



        // check permission
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("you need the permission for camera")
                .setDeniedMessage("you rejected")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        btn_move = (Button)findViewById(R.id.btn_move);
        btn_shared = (Button)findViewById(R.id.btn_shared);
        btn_wv = (Button)findViewById(R.id.btn_wv);
        btn_open = (Button)findViewById(R.id.btn_open);
        btn_capture = (Button)findViewById(R.id.btn_capture);

        et_test = (EditText)findViewById(R.id.et_test);
        iv_result = (ImageView)findViewById(R.id.iv_result);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {

                    }

                    if(photoFile != null) {
                        photoUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }

                }
            }
        });

        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        Button btn_close = (Button)findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });

        btn_wv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("extra","wv");
                startActivity(intent);
            }
        });

        btn_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SharedActivity.class);
                intent.putExtra("extra","hello");
                startActivity(intent);
            }
        });

        iv_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
            }
        });

        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str = et_test.getText().toString();
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("str", str);
                Log.d("et_test ",str);
                startActivity(intent);
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath =image.getAbsolutePath();
        return image;
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface((imageFilePath));
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegress(exifOrientation);
            } else {
                exifDegree = 0;
            }

            ((ImageView) findViewById(R.id.iv_result)).setImageBitmap(rotate(bitmap, exifDegree));
        }
    }

    private int exifOrientationToDegress(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation ==ExifInterface.ORIENTATION_ROTATE_180){
            return 180;
        } else if (exifOrientation ==ExifInterface.ORIENTATION_ROTATE_270){
            return 270;
        }
        return  0;
    };

    private Bitmap rotate(Bitmap bitmap,float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    PermissionListener permissionListener = new PermissionListener() {

        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), "the permission is allowed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), "the permission is denied", Toast.LENGTH_SHORT).show();

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Toast.makeText(getApplicationContext(), "thread", Toast.LENGTH_SHORT).show();
        }
    };
}