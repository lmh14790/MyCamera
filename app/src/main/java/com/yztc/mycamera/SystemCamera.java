package com.yztc.mycamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SystemCamera extends AppCompatActivity {

    @InjectView(R.id.start)
    Button start;
    @InjectView(R.id.myPicture)
    ImageView myPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_camera);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.start)
    public void onClick() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0x111);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case 0x111:
                    Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                   myPicture.setImageBitmap(bitmap);
                    break;

            }

        }
    }
}
