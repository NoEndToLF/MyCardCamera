package com.aicecloud.cardcamera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aicecloud.cardcamera.util.ImageVpShowUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.ruffian.library.RTextView;
import com.wildma.idcardcamera.camera.CameraActivity;
import com.wxystatic.permissionmanagerlibrary.GetPermissionListener;
import com.wxystatic.permissionmanagerlibrary.PermissionManager;
import com.yanzhenjie.permission.Permission;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.iv_one)
    ImageView ivOne;
    @BindView(R.id.btn_select_front)
    RTextView btnSelectFront;
    @BindView(R.id.btn_save_front)
    RTextView btnSaveFront;
    @BindView(R.id.iv_two)
    ImageView ivTwo;
    @BindView(R.id.btn_select_back)
    RTextView btnSelectBack;
    @BindView(R.id.btn_back_save)
    RTextView btnBackSave;
    private String path_front,path_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ImmersionBar.with(this).statusBarColor(R.color.blue).statusBarDarkFont(false, 0.2f)
                .navigationBarColor(R.color.white).flymeOSStatusBarFontColor(R.color.black).fitsSystemWindows(true).keyboardEnable(true).init();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == CameraActivity.RESULT_CODE) {
            //获取图片路径，显示图片
              path_front = CameraActivity.getImagePath(data);
            if (!TextUtils.isEmpty(path_front)) {
                ivOne.setImageBitmap(BitmapFactory.decodeFile(path_front));
            }
        } else {
              path_back = CameraActivity.getImagePath(data);
            if (!TextUtils.isEmpty(path_back)) {
                ivTwo.setImageBitmap(BitmapFactory.decodeFile(path_back));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }

    @OnClick({R.id.btn_select_front, R.id.btn_save_front, R.id.btn_select_back, R.id.btn_back_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_select_front:
                PermissionManager.getPermission(this, Permission.STORAGE, 111, new GetPermissionListener() {
                    @Override
                    public void onSuccess() {
                        PermissionManager.getPermission(MainActivity.this, Permission.CAMERA, 222, new GetPermissionListener() {
                            @Override
                            public void onSuccess() {
                                Intent intent_front = new Intent(MainActivity.this, CameraActivity.class);
                                intent_front.putExtra("take_type", CameraActivity.TYPE_IDCARD_FRONT);
                                startActivityForResult(intent_front, 1);
                            }
                            @Override
                            public void onFailed(String s) {
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    @Override
                    public void onFailed(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.btn_save_front:
                if (TextUtils.isEmpty(path_front)){
                    Toast.makeText(this, "请拍摄正面照", Toast.LENGTH_SHORT).show();
                    return;
                }
                ImageVpShowUtil.savePicture(this,"身份证","正面",path_front);
                break;
            case R.id.btn_select_back:
                PermissionManager.getPermission(this, Permission.STORAGE, 111, new GetPermissionListener() {
                    @Override
                    public void onSuccess() {
                        PermissionManager.getPermission(MainActivity.this, Permission.CAMERA, 222, new GetPermissionListener() {
                            @Override
                            public void onSuccess() {
                                Intent intent_back = new Intent(MainActivity.this, CameraActivity.class);
                                intent_back.putExtra("take_type", CameraActivity.TYPE_IDCARD_BACK);
                                startActivityForResult(intent_back, 2);
                            }
                            @Override
                            public void onFailed(String s) {
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    @Override
                    public void onFailed(String message) {
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            case R.id.btn_back_save:
                if (TextUtils.isEmpty(path_back)){
                    Toast.makeText(this, "请拍摄背面照", Toast.LENGTH_SHORT).show();
                    return;
                }
                ImageVpShowUtil.savePicture(this,"身份证","背面",path_back);
                break;
        }
    }

}
