package com.example.shop.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
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
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.R;
import com.example.shop.adapter.GoodsTypeAdapter;
import com.example.shop.bean.Goods;
import com.example.shop.service.GoodsService;
import com.example.shop.service.SearcherService;
import com.example.shop.service.StoreService;
import com.example.shop.service.UserService;
import com.example.shop.utils.BitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PublishGoodsActivity extends AppCompatActivity {

    private EditText goodsSimpleNameInput;
    private ImageView photoAdd;
    private EditText goodsPriceInput;
    private EditText goodsNameInput;
    private EditText goodsDescriptionInput;
    private EditText keyWordInput;
    private Spinner goodsTypeChoose;
    private TextView error;
    private TextView cancel;
    private TextView sure;
    private LinearLayout empty;
    private LinearLayout zhengti;
    private TextView addStore;

    private List<String> typeList;
    private GoodsTypeAdapter goodsTypeAdapter;
    private ArrayAdapter<String> adapter;

    private String type;

    private PopupWindow pop;

    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    public static final int CHOOSE_PHOTO = 2;

    private String url;
    private AlertDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_goods);

        goodsSimpleNameInput = findViewById(R.id.goods_simple_name_input);
        photoAdd = findViewById(R.id.photo_add);
        goodsPriceInput = findViewById(R.id.goods_price_input);
        goodsNameInput = findViewById(R.id.goods_name_input);
        goodsDescriptionInput = findViewById(R.id.goods_description_input);
        keyWordInput = findViewById(R.id.goods_keyword_input);
        goodsTypeChoose = findViewById(R.id.goods_type_choose);
        cancel = findViewById(R.id.cancel);
        sure = findViewById(R.id.sure);
        error = findViewById(R.id.error);
        empty = findViewById(R.id.lv_add_store);
        zhengti = findViewById(R.id.zhengti);
        addStore = findViewById(R.id.add_store);

        if (UserService.getStore() == null){
            empty.setVisibility(View.VISIBLE);
            zhengti.setVisibility(View.GONE);
        }else {
            empty.setVisibility(View.GONE);
            zhengti.setVisibility(View.VISIBLE);
        }

        typeList = GoodsService.getTypeNames();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goodsTypeChoose.setAdapter(adapter);

        goodsPriceInput.setInputType(InputType.TYPE_CLASS_NUMBER);

        goodsTypeChoose.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sure.setOnClickListener(v -> addGoods());
        photoAdd.setOnClickListener(v -> showPop());
        cancel.setOnClickListener(v -> finish());
        addStore.setOnClickListener(v -> startActivity(new Intent(PublishGoodsActivity.this, CreateStoreActivity.class)));
    }

    private void addGoods(){
        if (goodsSimpleNameInput.getText().toString().length() == 0){
            error.setText("商品名不能为空");
            error.setVisibility(View.VISIBLE);
        }else if (goodsPriceInput.getText().toString().length() == 0){
            error.setText("商品价格不能为空");
            error.setVisibility(View.VISIBLE);
        }else if (type == null){
            error.setText("请选择商品分类");
            error.setVisibility(View.VISIBLE);
        }else if (url == null){
            error.setText("请选择商品照片");
            error.setVisibility(View.VISIBLE);
        }else {
            error.setVisibility(View.GONE);
            String store = UserService.getStore();
            GoodsService.insert(goodsNameInput.getText().toString(), Double.parseDouble(goodsPriceInput.getText().toString()), type, url, store,goodsDescriptionInput.getText().toString(), goodsSimpleNameInput.getText().toString());
            if (keyWordInput.getText().toString().length() != 0) {
                List<Goods> goodsList = GoodsService.getGoodsByType(type);
                Goods goods = goodsList.get(goodsList.size() - 1);
                String[] split = keyWordInput.getText().toString().split(",");
                for (String s : split){
                    SearcherService.insertKeyWord(s, goods.getId());
                }
            }
            dialog = new AlertDialog.Builder(this)
                    .setMessage("发布成功")
                    .setPositiveButton("确定", (dialog, which) ->   startActivity(new Intent(this, PublishGoodsActivity.class)))
                    .create();
            dialog.show();
        }
    }

    private void showPop() {
        View bottomView = View.inflate(PublishGoodsActivity.this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = bottomView.findViewById(R.id.tv_album);
        TextView mCamera = bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = PublishGoodsActivity.this.getWindow().getAttributes();
        lp.alpha = 0.5f;
        PublishGoodsActivity.this.getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = PublishGoodsActivity.this.getWindow().getAttributes();
                lp.alpha = 1f;
                PublishGoodsActivity.this.getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(PublishGoodsActivity.this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        if (ContextCompat.checkSelfPermission(PublishGoodsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                            //相册中的照片都是存储在SD卡上的，需要申请运行时权限，WRITE_EXTERNAL_STORAGE是危险权限，表示同时授予程序对SD卡的读和写的能力
                            ActivityCompat.requestPermissions(PublishGoodsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }else {
                            openAlbum();

                        }
                        break;
                    case R.id.tv_camera:
                        //拍照
                        File outputImage = new File(PublishGoodsActivity.this.getExternalCacheDir(),"output_image.jpg");
                        try{
                            if(outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        if(Build.VERSION.SDK_INT >=24){
                            imageUri = FileProvider.getUriForFile(PublishGoodsActivity.this,
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
                        Bitmap bitmap = BitmapFactory.decodeStream(PublishGoodsActivity.this.getContentResolver().openInputStream(imageUri));
                        url = BitmapUtils.bitmapToString(bitmap);
                        photoAdd.setImageBitmap(bitmap);
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
        if(DocumentsContract.isDocumentUri(PublishGoodsActivity.this, uri)) {
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
        Cursor cursor = PublishGoodsActivity.this.getContentResolver().query(
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
            url = BitmapUtils.bitmapToString(bitmap);
            photoAdd.setImageBitmap(bitmap);
        }else {
            Toast.makeText(PublishGoodsActivity.this, "Load Failed", Toast.LENGTH_LONG).show();
        }
    }

}
