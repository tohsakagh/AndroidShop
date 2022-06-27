package com.example.shop.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.shop.R;
import com.example.shop.activity.CollectionActivity;
import com.example.shop.activity.MyStoreActivity;
import com.example.shop.activity.OrderActivity;
import com.example.shop.activity.PublishGoodsActivity;
import com.example.shop.activity.SettingActivity;
import com.example.shop.bean.Msg;
import com.example.shop.service.UserService;
import com.example.shop.utils.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class IsloginMineFragment extends BasePageTitleFragment implements View.OnClickListener{

    private TextView loginUsername;
    private TextView loginUserId;
    private RelativeLayout setting;
    private RelativeLayout logoff;
    private RelativeLayout toCollection;
    private RelativeLayout toOrder;
    private RelativeLayout toPublish;
    private RelativeLayout toMyStore;

    private EditText passwordEditext;
    private TextView errortip;
    private View view;

    public static final int TAKE_PHOTO = 1;
    private ImageView picture;
    private Uri imageUri;
    public static final int CHOOSE_PHOTO = 2;
    private PopupWindow pop;


    @SuppressLint("SetTextI18n")
    @Override
    protected View initView() {
        setTitleIcon("个人中心",true, true, false);//标题栏的显示
        View isloginMineFragment = View.inflate(getContext(), R.layout.fragment_islogin_mine, null);

        isloginMineFragment.findViewById(R.id.lofin_head).setOnClickListener(this);

        loginUsername = isloginMineFragment.findViewById(R.id.username);
        loginUserId = isloginMineFragment.findViewById(R.id.userid);
        setting = isloginMineFragment.findViewById(R.id.re_setting);
        logoff = isloginMineFragment.findViewById(R.id.re_logoff) ;
        toCollection = isloginMineFragment.findViewById(R.id.re_collection);
        toOrder = isloginMineFragment.findViewById(R.id.re_order);
        toPublish = isloginMineFragment.findViewById(R.id.re_publish);
        toMyStore = isloginMineFragment.findViewById(R.id.re_store);
        picture = isloginMineFragment.findViewById(R.id.iv_avatar);

        if (UserService.getPath() != null){
            picture.setImageBitmap(BitmapUtils.stringToBitmap(UserService.getPath()));
        }else {
            picture.setImageDrawable(getContext().getDrawable(R.drawable.defaulthead));
        }

        loginUserId.setText(Msg.loginUserId.toString());
        loginUsername.setText(Msg.loginUsername);

        setting.setOnClickListener(this);
        logoff.setOnClickListener(this);
        toCollection.setOnClickListener(this);
        toOrder.setOnClickListener(this);
        toPublish.setOnClickListener(this);
        toMyStore.setOnClickListener(this);

        view = getLayoutInflater().inflate(R.layout.passwordinput, null);
        passwordEditext = (EditText) view.findViewById(R.id.editText_password);
        errortip = (TextView) view.findViewById(R.id.errortip);
        errortip.setVisibility(View.GONE);
        return isloginMineFragment ;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.re_setting){
            intent = new Intent(getActivity(), SettingActivity.class);
        }else if (v.getId() == R.id.re_logoff){
            Msg.clear();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_fragment_content, new NologinMineFragment(), null)
                    .addToBackStack(null)
                    .commit();
        }else if (v.getId() == R.id.re_collection){
            intent = new Intent(getActivity(), CollectionActivity.class);
        }else if (v.getId() == R.id.re_order){
            intent = new Intent(getActivity(), OrderActivity.class);
        }else if (v.getId() == R.id.re_store){
            intent = new Intent(getActivity(), MyStoreActivity.class);
        }else if (v.getId() == R.id.re_publish){
            intent = new Intent(getActivity(), PublishGoodsActivity.class);
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        picture = view.findViewById(R.id.iv_avatar);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
            }
        });

    }

    private void showPop() {
        View bottomView = View.inflate(getActivity(), R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.5f;
        getActivity().getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            //相册中的照片都是存储在SD卡上的，需要申请运行时权限，WRITE_EXTERNAL_STORAGE是危险权限，表示同时授予程序对SD卡的读和写的能力
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }else {
                            openAlbum();

                        }
                        break;
                    case R.id.tv_camera:
                        //拍照
                        File outputImage = new File(getActivity().getExternalCacheDir(),"output_image.jpg");
                        try{
                            if(outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT >=24){
                            imageUri = FileProvider.getUriForFile(getActivity(),
                                    "com.example.cameraalbumtest.fileprovider",outputImage);
                        }else{
                            imageUri = Uri.fromFile(outputImage);
                        }

                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        startActivityForResult(intent,TAKE_PHOTO);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };
        mCamera.setOnClickListener(clickListener);
        mAlbum.setOnClickListener(clickListener);

        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        String s = BitmapUtils.bitmapToString(bitmap);
                        UserService.updatePath(s);
                        picture.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
                    //因为sdk19以后返回的数据不同，所以要根据手机系统版本进行不同的操作
                    //判断手机系统版本
                    if(Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKiKai(data);
                    }else {
                        handleImageBeforeKiKai(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    //>=19的操作
    @TargetApi(19)
    private void handleImageOnKiKai(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是Document类型的Uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }else if("content".equalsIgnoreCase(uri.getScheme())) {
                //不是document类型的Uri，普通方法处理
                imagePath = getImagePath(uri, null);
            }
            displayImage(imagePath);
        }
    }

    //<19的操作
    private void handleImageBeforeKiKai(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri 和selection获取真正的图片路径
        Cursor cursor = getActivity().getContentResolver().query(
                uri, null, selection, null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(
                        cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String path) {
        if(path != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            UserService.updatePath(BitmapUtils.bitmapToString(bitmap));
            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(getActivity(), "Load Failed", Toast.LENGTH_LONG).show();
        }
    }

}
