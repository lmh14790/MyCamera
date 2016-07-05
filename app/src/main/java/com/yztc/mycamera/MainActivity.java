package com.yztc.mycamera;

import android.Manifest;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    @InjectView(R.id.takePhoto)
    Button takePhoto;
    @InjectView(R.id.picture)
    SurfaceView picture;
    @InjectView(R.id.take)
    Button take;

    private Camera camera;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        picture.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                surfaceHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {


    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        EasyPermissions.requestPermissions(this, "要使用摄像头", 0x11, Manifest.permission.CAMERA);

    }


    /**
     * 打开摄像头
     */
    @AfterPermissionGranted(0x11)
    public void open() {
        //判断是否具有权限
        if (EasyPermissions.hasPermissions(this, new String[]{Manifest.permission.CAMERA})) {
            camera = Camera.open();
            try {
                camera.setPreviewDisplay(surfaceHolder);
                Camera.Parameters parameters = camera.getParameters();
                camera.setDisplayOrientation(90);
                camera.setParameters(parameters);
            } catch (IOException e) {
                e.printStackTrace();
            }

            camera.startPreview();

        } else {
            //再次申请权限
            EasyPermissions.requestPermissions(this, "要使用摄像头", 0x11, Manifest.permission.CAMERA);
        }

    }


    @OnClick({R.id.takePhoto, R.id.take,R.id.flash,R.id.foucs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takePhoto:
                open();
                break;
            case R.id.take:
                takePicture();
                break;
            case R.id.flash:
                flash();
                break;
            case R.id.foucs:
                foucs();
                break;
        }
    }

    /**
     * 拍照并保存
     */
    public void takePicture(){
        flash();
        foucs();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        camera.takePicture(null, null, new Camera.PictureCallback() {
          @Override
          public void onPictureTaken(byte[] data, Camera camera) {

              File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + System.currentTimeMillis() + ".jpg");
              FileOutputStream outputStream = null;
              try {
                  outputStream = new FileOutputStream(file);
                  outputStream.write(data, 0, data.length);
                  outputStream.flush();
                  camera.startPreview();
              } catch (FileNotFoundException e) {
                  e.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              } finally {
                  if (outputStream != null) {
                      try {
                          outputStream.close();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                  }

              }
          }
      });

    }

    public void  foucs(){
    camera.autoFocus(new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
                if(success){

                }
        }
    });


    }

    public void flash(){
        Camera.Parameters parameters=camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        camera.setParameters(parameters);
    }
}
