package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.view.animation.AnimationUtils;
import android.widget.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class drawActivity extends AppCompatActivity {
    public static byte[] dates;
    private View lastSelectpen=null;
    public	static	final	int	TAKE_PHOTO	=	1;
    public  int imageId,number;
    drawPaint drawpaint;
    private long exitTime = 0;
    private Bitmap photo ;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private ImageView	picture;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        picture	=	(ImageView)	findViewById(R.id.picture);
        ImageButton bt_home=(ImageButton)findViewById(R.id.bt_home);
        bt_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public	void onClick(View v)	{
                Intent intent = new	Intent(drawActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        imageId = getIntent().getIntExtra("image_id", -1);
        number=getIntent().getIntExtra("number", -1);

        if (MainActivity.flag==1){
        drawpaint.setImId(number);
        MainActivity.flag=0;}
        else {
            drawpaint.setImId(imageId);
        }

        drawpaint=(drawPaint)findViewById(R.id.myPaint);

        lastSelectpen=findViewById(R.id.bt_purple);
        lastSelectpen.startAnimation(AnimationUtils.loadAnimation(drawActivity.this,R.anim.zoom_out));

        if	(ContextCompat.checkSelfPermission(drawActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=	PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(drawActivity.this,	new	String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},	1);								
        }else{
            initMediaPlayer();	//	初始化MediaPlayer								}	
        }

        ImageButton bt_pause=(ImageButton)findViewById(R.id.bt_pause);
        bt_pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public	void onClick(View v)	{
                if	(mediaPlayer.isPlaying())	{
                    mediaPlayer.pause();
                }else	if(!mediaPlayer.isPlaying())	{
                    mediaPlayer.start();
                }
            }
        });

        ImageButton bt_camera=(ImageButton)findViewById(R.id.bt_camera);
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage	= new	File(getExternalCacheDir(),	"output_image.jpg");
                try	{
                    if(outputImage.exists())	{
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }	catch (IOException e)	{
                    e.printStackTrace();
                }
                if	(Build.VERSION.SDK_INT	>=	24)	{
                    imageUri = FileProvider.getUriForFile(drawActivity.this,"com.example.myapplication.fileprovider", outputImage);
                }	else	{
                    imageUri	=	Uri.fromFile(outputImage);
                }
                Intent	intent	=	new	Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,	TAKE_PHOTO);
            }
        });
    }

    private void initMediaPlayer()	{
        try	{
            //File file =	new	File(Environment.getExternalStorageDirectory(),	"music.mp3");
            //mediaPlayer.setDataSource(file.getPath());	//	指定音频文件的路径
            mediaPlayer = MediaPlayer.create(drawActivity.this, R.raw.music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
             }
             catch	(Exception	e)	{
             e.printStackTrace();
             }
    }

    private void playAnim(View v){
        if(lastSelectpen!=null){
            lastSelectpen.startAnimation(AnimationUtils.loadAnimation(drawActivity.this,R.anim.zoom_in));
            v.startAnimation(AnimationUtils.loadAnimation(drawActivity.this,R.anim.zoom_out));
        }
        lastSelectpen=v;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            SharedPreferences.Editor editor	= getSharedPreferences("data",MODE_PRIVATE).edit();
            editor.putInt("number",imageId);
            editor.apply();
            Log.i("drawActivity","number	is	"+ imageId);
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                String msg = "再按一次返回键退出" ;
                Toast.makeText(drawActivity.this, msg, Toast.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                Intent home = new Intent(Intent.ACTION_MAIN);
                 home.addCategory(Intent.CATEGORY_HOME);
                 startActivity(home);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.bt_purple:  playAnim(v); drawpaint.setPurplePaint();break;
            case R.id.bt_Black:  playAnim(v);drawpaint.setBlackPaint(); break;
            case R.id.bt_blue:  playAnim(v); drawpaint.setBluePaint();break;
            case R.id.bt_green:  playAnim(v); drawpaint.setGreenPaint();break;
            case R.id.bt_yellow:  playAnim(v);drawpaint.setYellowPaint(); break;
            case R.id.bt_orange:  playAnim(v);drawpaint.setOrangePaint(); break;
            case R.id.bt_red:  playAnim(v); drawpaint.setRedPaint(); break;
            case R.id.bt_gray: playAnim(v); drawpaint.setgrayPaint(); break;
            case R.id.bt_brown: playAnim(v); drawpaint.setbrownPaint(); break;
            case R.id.bt_delete: drawpaint.clean(); break;
        }
    }

    public	void onRequestPermissionsResult(int requestCode, String[]permissions,int[]grantResults)	{
        switch	(requestCode)	{
            case 1:
                if	(grantResults.length>0	&& grantResults[0]	==	PackageManager.	PERMISSION_GRANTED){
                    initMediaPlayer();
                }
                else{
                    Toast.makeText(this,"拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                default:
        }
    }

    protected	void onDestroy()	{
        super.onDestroy();
        if	(mediaPlayer	!=	null)	{
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    protected	void onActivityResult(int requestCode,	int	resultCode,	Intent	data){
        switch	(requestCode)	{
            case	TAKE_PHOTO:
                if	(resultCode	== RESULT_OK)	{
                    try	{
                         Bitmap	bitmap	= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                    }	catch	(FileNotFoundException e)	{
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    break;
        }
    }


































}
