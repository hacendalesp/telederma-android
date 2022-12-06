package com.telederma.gov.co;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.telederma.gov.co.adapters.ShowImagesAdapter;
import com.telederma.gov.co.dialogs.ShowImagesDialog;
import com.telederma.gov.co.dialogs.ShowImagesViewPager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by Administrator on 2017/5/3.
 * 嵌套了viewpager的图片浏览
 */

public class ImagenDermatoscopia extends BaseActivity  {

    private View mView;
    private Context mContext;
    private ShowImagesViewPager mViewPager;
    private TextView mIndexText;
    private TextView tv_desc;
    private List<String> mImgUrls, descriptions;
    private List<String> mTitles;
    LinearLayout ll_content_image;
    private List<View> mViews;
    private Dialog dialog;
    private ShowImagesAdapter mAdapter;
    private Button btnClose;
    private View.OnClickListener listener;
    private int posision;

    private ImageView ivDeleteImage;
    private ImageView ivAddImage;
    private Button btnShowImageDer;
    private LinearLayout llShowImageDer;
    public static final String ACTION_IMAGEN_DERMATOSCOPIA = "com.telederma.gov.co.ImagenDermatoscopia";
    public static final String ARG_URL_IMAGEN ="url_imagen";
    public static final String ARG_POS_IMAGEN ="pos_imagen";
    public static final int RESULT_CODE_ELIMINAR_IMAGEN = 201;
    public static final int REQUEST_CODE_CAMARA = 300;
    public static final int RESULT_CODE_EXITO_AGRGANDO_IMAGENES = 401;
    private JSONObject imagenesDermatoscopicas;
    private Activity mySelf;
    private Boolean showButtonsDermoscopy;
    private JSONObject JDescripciones;
    private String nombreCuerpo = "";
    public static final String ARG_IMAGEN_NOMBRE_PARTE = "imagen_nombre_parte";
    private JSONObject imagenesNombreParte;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mUIFlag = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
        setContentView(R.layout.imagen_dermatoscopia_brower);



        //Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));

        initView();
        initData();

    }



    private void initView() {

        this.imagenesDermatoscopicas = new JSONObject();
        this.descriptions = new ArrayList<>();
        this.JDescripciones = new JSONObject();

        try{
            if(getIntent().hasExtra("imagenes_dermatoscopia")){
                this.imagenesDermatoscopicas = new JSONObject(getIntent().getStringExtra("imagenes_dermatoscopia"));
            }
        } catch (Exception e){

        }
        this.showButtonsDermoscopy = getIntent().getBooleanExtra("showButtonsDermoscopy", false);
        if(showButtonsDermoscopy){
            ((LinearLayout) findViewById(R.id.ll_del_image)).setVisibility(View.VISIBLE);
            ((LinearLayout) findViewById(R.id.ll_add_image)).setVisibility(View.VISIBLE);
        }

        try{
            if(getIntent().hasExtra("descripciones")){
                this.JDescripciones = new JSONObject(getIntent().getStringExtra("descripciones"));
            }
        }catch (Exception e){

        }


        if(getIntent().hasExtra("nombreCuerpo")){
            this.nombreCuerpo = getIntent().getStringExtra("nombreCuerpo");
        }

        imagenesNombreParte = new JSONObject();
        if(getIntent().hasExtra(ARG_IMAGEN_NOMBRE_PARTE)){
            try{
                this.imagenesNombreParte = new JSONObject(getIntent().getStringExtra(ARG_IMAGEN_NOMBRE_PARTE));
            }catch (Exception e){

            }


        }

        this.mySelf = this;
        this.mImgUrls =  getIntent().getStringArrayListExtra(ARG_URL_IMAGEN);
        this.posision =  getIntent().getIntExtra(ARG_POS_IMAGEN, 0);
        this.mContext = getApplicationContext();
        mViewPager = (ShowImagesViewPager) findViewById(R.id.vp_images);
        mIndexText = (TextView) findViewById(R.id.tv_image_index);
        ll_content_image = (LinearLayout) findViewById(R.id.ll_content_image);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        tv_desc.setMovementMethod(new ScrollingMovementMethod());
        mTitles = new ArrayList<>();
        mViews = new ArrayList<>();
        btnClose = findViewById(R.id.btn_close_dialog);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(-1);
            }
        });


        ivDeleteImage = findViewById(R.id.iv_delete_image);
        ivDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(RESULT_CODE_ELIMINAR_IMAGEN);

            }
        });


        ivAddImage = findViewById(R.id.iv_add_image);
        ivAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(CamaraDermatoscopia.ACTION_CAMARA_SLIDER);
                intent.putExtra(ARG_URL_IMAGEN, mImgUrls.get(posision));
                intent.putExtra(ARG_IMAGEN_NOMBRE_PARTE, nombreCuerpo);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_CODE_CAMARA);
            }
        });

        llShowImageDer = findViewById(R.id.ll_show_image_der);
        btnShowImageDer = findViewById(R.id.btn_show_image_der);
        btnShowImageDer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray imagenes = obtenerImagenesDermatoscopicas(mImgUrls.get(posision));
                List<String> descripciones = new ArrayList<>();
                List<String> imgUrls = new ArrayList<>();

                for(int i = 0; i < imagenes.length(); i++){
                    try{
                        imgUrls.add(imagenes.getString(i));

                        if(JDescripciones.has(imagenes.getString(i))){
                            descripciones.add(JDescripciones.getString(imagenes.getString(i)));
                        }else{
                            descripciones.add(null);
                        }
                    }catch (Exception e){

                    }

                }

                dialog = new ShowImagesDialog(mySelf, imgUrls, descripciones, 0, nombreCuerpo);
                dialog.show();


            }
        });
        incializarDescripciones();
        showImage(this.posision);


    }

    public void incializarDescripciones(){
        try{
            for(int i = 0; i < mImgUrls.size(); i++){
                if(JDescripciones.has(mImgUrls.get(i))){
                    descriptions.add(JDescripciones.getString(mImgUrls.get(i)));
                }else{
                    descriptions.add(null);
                }
            }

        }catch (Exception e){

        }
    }



    //NotaSebasP: Se creó este metodo para abrir el dialog en la posición señalada.
    public void showImage(int pos) {
        this.posision = pos;
        mViewPager.setCurrentItem(this.posision);

    }


    private void initData() {
        PhotoViewAttacher.OnPhotoTapListener listener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss(-1);
            }
        };

        for (int i = 0; i < mImgUrls.size(); i++) {
            final PhotoView photoView = new PhotoView(mContext);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(layoutParams);
            photoView.setOnPhotoTapListener(listener);

            SimpleTarget target = new SimpleTarget<Drawable>() {

                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    photoView.setImageDrawable(resource);
                }
            };

            Glide.with(mContext)
                    .load(mImgUrls.get(i))
                    .apply(new RequestOptions().disallowHardwareConfig().centerCrop()
                            .placeholder(R.drawable.cargando)
                            .error(R.drawable.error_cargando)
                            .format(DecodeFormat.PREFER_RGB_565))


                    .into(target);

            mViews.add(photoView);

            mTitles.add(i + "");

        }

        try{
            nombreCuerpo = imagenesNombreParte.getString(mImgUrls.get(this.posision));
        }catch (Exception e){
            nombreCuerpo = "";
        }


        mAdapter = new ShowImagesAdapter(mViews, mTitles);
        mViewPager.setAdapter(mAdapter);
        mIndexText.setText(1 + "/" + mImgUrls.size() + "    " + nombreCuerpo);

        ((LinearLayout) findViewById(R.id.ll_content_image)).setVisibility(View.GONE);
        if(descriptions != null) {
            if(descriptions.get(0) != null) {
                ((LinearLayout) findViewById(R.id.ll_content_image)).setVisibility(View.VISIBLE);
                tv_desc.setText(descriptions.get(0));
            }
        }


        mostrarBotonVerImagenesDermatoscopicas(0);



        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                try {
                    nombreCuerpo = imagenesNombreParte.getString(mImgUrls.get(position));
                }catch (Exception e){
                    nombreCuerpo = "";
                }

                mIndexText.setText(position + 1 + "/" + mImgUrls.size() + "    " + nombreCuerpo);
                posision = position;
                mostrarBotonVerImagenesDermatoscopicas(position);

                ((LinearLayout) findViewById(R.id.ll_content_image)).setVisibility(View.GONE);
                String description = null;
                if(descriptions != null) {
                    if(descriptions.get(position) != null) {
                        description = descriptions.get(position);
                    }
                }
                if(description != null) {
                    ((LinearLayout) findViewById(R.id.ll_content_image)).setVisibility(View.VISIBLE);
                    tv_desc.setText(description);
                }else{
                    tv_desc.setText("");
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //mViewPager.setCurrentItem(this.posision);
        mViewPager.setCurrentItem(this.posision);

    }

    public void mostrarBotonVerImagenesDermatoscopicas(int position){
        try {
            if (imagenesDermatoscopicas != null) {
                JSONArray imagenes = obtenerImagenesDermatoscopicas(mImgUrls.get(position));
                if (imagenes.length() > 0) {
                    llShowImageDer.setVisibility(View.VISIBLE);
                } else {
                    llShowImageDer.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {

        }
    }

    public JSONArray obtenerImagenesDermatoscopicas(String url) {
        try {
            JSONArray imagenes = new JSONArray(imagenesDermatoscopicas.getString(url));
            return imagenes;

        } catch (Exception e) {
            return new JSONArray();
        }

    }



    public void dismiss(int resultCode) {


        if(resultCode == RESULT_CODE_ELIMINAR_IMAGEN){
            Intent data = new Intent();
            data.putExtra("url_imagen", this.mImgUrls.get(this.posision));
            setResult(resultCode, data);
        }


        //Glide.get(mContext).getBitmapPool().clearMemory();
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMARA) {
            if (resultCode == RESULT_CODE_EXITO_AGRGANDO_IMAGENES) {

                Intent intent = new Intent();
                intent.putExtra("url_imagen", this.mImgUrls.get(this.posision));
                ArrayList<String> imagenes_dermatoscopia = data.getStringArrayListExtra("imagenes_dermatoscopia");
                intent.putExtra("imagenes_dermatoscopia", imagenes_dermatoscopia);
                setResult(RESULT_CODE_EXITO_AGRGANDO_IMAGENES, intent);
                finish();


            }
        }
    }


}
