package com.telederma.gov.co.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.telederma.gov.co.R;
import com.telederma.gov.co.adapters.ShowImagesAdapter;
import com.telederma.gov.co.utils.Constantes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.glidebitmappool.GlideBitmapFactory;
import com.glidebitmappool.GlideBitmapPool;


import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by Administrator on 2017/5/3.
 * 嵌套了viewpager的图片浏览
 */

public class ShowImagesDialog extends Dialog  {

    private View mView ;
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
    private int posision;
    public String nombreParte = "";
    public ShowImagesDialog(@NonNull Context context, List<String> imgUrls,int pos) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.mContext = context;
        this.mImgUrls = imgUrls;
        this.posision = pos;
        initView();
        initData();

    }
    public ShowImagesDialog(@NonNull Context context, List<String> imgUrls, List<String> imgDescriptions, int pos, String nombreParte) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.mContext = context;
        this.mImgUrls = imgUrls;
        this.descriptions = imgDescriptions;
        this.posision = pos;
        this.nombreParte = nombreParte;
        initView();
        initData();

    }
    //NotaSebasP: Se creó este metodo para abrir el dialog en la posición señalada.
    public void showImage(int pos) {
        this.posision = pos;
        mViewPager.setCurrentItem(this.posision);

    }

    private void initView() {
        dialog = this;
        mView = View.inflate(mContext, R.layout.dialog_images_brower, null);

        mViewPager = (ShowImagesViewPager) mView.findViewById(R.id.vp_images);
        mIndexText = (TextView) mView.findViewById(R.id.tv_image_index);
        ll_content_image = (LinearLayout) mView.findViewById(R.id.ll_content_image);
        tv_desc = (TextView) mView.findViewById(R.id.tv_desc);
        tv_desc.setMovementMethod(new ScrollingMovementMethod());
        mTitles = new ArrayList<>();
        mViews = new ArrayList<>();
        btnClose = mView.findViewById(R.id.btn_close_dialog);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mView);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.height = Constantes.EXACT_SCREEN_HEIGHT;
        wl.width = Constantes.EXACT_SCREEN_WIDTH;
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);
    }

    private void initData() {
        PhotoViewAttacher.OnPhotoTapListener listener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                dismiss();
            }
        };
        for (int i = 0; i < mImgUrls.size(); i++) {
            final PhotoView photoView = new uk.co.senab.photoview.PhotoView(mContext);
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

        mAdapter = new ShowImagesAdapter(mViews, mTitles);
        mViewPager.setAdapter(mAdapter);
        mIndexText.setText(1 + "/" + mImgUrls.size() + "    " + nombreParte);

        if(descriptions != null) {
            if(descriptions.get(0) != null) {
                tv_desc.setText(descriptions.get(0));
            }
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndexText.setText(position + 1 + "/" + mImgUrls.size() + "    " + nombreParte);
                String description = null;
                if(descriptions != null) {
                    if(descriptions.get(position) != null) {
                        description = descriptions.get(position);
                    }
                }
                if(description != null) {
                    tv_desc.setText(description);
                }else{
                    tv_desc.setText("");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(this.posision);

    }
    @Override
    public void show() {
        super.show();

        getWindow().setBackgroundDrawableResource(android.R.color.black);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height =  WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }

    @Override
    public void dismiss() {
        Log.d("ShoeImagesDialogOndismiss()", "clearing_memory==========>");
        //Glide.get(mContext).getBitmapPool().clearMemory();
        super.dismiss();
    }
}
