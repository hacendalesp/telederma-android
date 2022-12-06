package com.telederma.gov.co;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class SliderCard extends RecyclerView.ViewHolder {

    private static int viewWidth = 0;
    private static int viewHeight = 0;

    private final ImageView imageView;
    private final ImageView iv_icono_dermatoscopia;


    public SliderCard(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        iv_icono_dermatoscopia = (ImageView) itemView.findViewById(R.id.iv_icono_dermatoscopia);
    }

    public void setImage(byte[] data, Context contexto) {
        //Glide.get(contexto).getBitmapPool().clearMemory();
        Glide.with(contexto).asBitmap()
                .load(data)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.cargando)
                        .error(R.drawable.error_cargando)
                ).into(imageView);
    }

    public void setDermatoscopiaMostrar() {
        iv_icono_dermatoscopia.setVisibility(View.VISIBLE);
    }

}