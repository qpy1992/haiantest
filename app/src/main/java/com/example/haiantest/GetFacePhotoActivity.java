package com.example.haiantest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.hardware.Camera;

import com.example.haiantest.util.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GetFacePhotoActivity extends AppCompatActivity implements View.OnClickListener, Camera.PreviewCallback {
    private SurfaceView sfview;
    private ImageView img_back;
    private ImageView   img_sure;
    private Camera mCamera;
    private boolean     bfrontSwitch;
    private byte[]      mPicByte;//临时记录预览下的图片数据
    private Bitmap mBmp;
    private String      fileUrl;
    private int RESULT_FOR_FACE = 10088;//获取头像响应值


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_face_photo);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
        setView();
        setData();
    }

    private void setView() {
        sfview =  findViewById(R.id.sfview);
        img_back =  findViewById(R.id.img_back);
        img_sure =  findViewById(R.id.img_sure);
        img_sure.setVisibility(View.GONE);
    }

    private void setData() {
        img_back.setOnClickListener(this);
        img_sure.setOnClickListener(this);
        //获取前置摄像头，显示在SurfaceView上
        setSurFaceView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCameraPic();
            }
        }, 2000);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_sure:
                //拍摄,获取相机图片
                getCameraPic();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void setSurFaceView() {
        //判断是否有前置摄像头
        bfrontSwitch = isHasFrontCamera();
        if (mCamera == null) {
            if (bfrontSwitch) {
                mCamera = Camera.open(1);//前置
                //                rotation = 270;
            } else {
                mCamera = Camera.open(0);//后置
                //                rotation = 90;
            }
        }
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();// 得到摄像头的参数
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//        mCamera.setParameters(parameters);
        mCamera.startPreview();//开启预览
        mCamera.setPreviewCallback(this);//开启Camera预览回调，重写onPreviewFrame获取相机回调
        mCamera.cancelAutoFocus();//聚焦
        //已打开相机

        final SurfaceHolder mSurfaceHolder = sfview.getHolder();//获取holder参数
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {//设置holder的回调
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                startPreview(mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                stopPreview();
            }
        });

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        mPicByte = bytes;
    }

    private void getCameraPic() {
        mCamera.stopPreview();
        //保存图片
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        try {
            YuvImage image = new YuvImage(mPicByte, ImageFormat.NV21, previewSize.width, previewSize.height, null);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100, stream);
                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                //因为图片会放生旋转，因此要对图片进行旋转到和手机在一个方向上
                rotateMyBitmap(bmp);
                stream.close();
            }
        } catch (Exception ex) {
            Log.e("Sys", "Error:" + ex.getMessage());
        }

        if (null != mBmp) {
            //将bitmap保存，记录照片本地地址，留待之后上传
            boolean b = saveBitmap(mBmp);
            if (b) {
                ToastUtils.showToast(this, getIntent().getStringExtra("msg"));
                Intent intent = getIntent().putExtra("face_pic_url", fileUrl);
                setResult(RESULT_FOR_FACE, intent);
                finish();
            } else {
                ToastUtils.showToast(this, "人脸获取失败，请退出重新打开摄像机");
            }
        }
    }

    public void rotateMyBitmap(Bitmap bmp) {
        //*****旋转一下
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);

        //Bitmap bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);

        mBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
    }

    private void startPreview(SurfaceHolder mSurfaceHolder) {
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        try {
            finish();
        } catch (Exception e) {
        }
    }

    private boolean isHasFrontCamera() {
        boolean hasFrontCarmera = false;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                hasFrontCarmera = true;
            }
        }
        return hasFrontCarmera;
    }

    /**
     * 保存方法
     */
    public boolean saveBitmap(Bitmap bm) {
        long longTime = System.currentTimeMillis();
        fileUrl = "/sdcard/DCIM/Camera/" + longTime + "head001.png";
        //        Log.e(TAG, "保存图片");
        File file = new File("/sdcard/DCIM/Camera/", longTime + "head001.png");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            //保存图片后发送广播通知更新数据库
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            this.sendBroadcast(intent);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}