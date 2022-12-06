package com.telederma.gov.co.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.cognito.internal.util.StringUtils;
import com.telederma.gov.co.R;
import com.telederma.gov.co.dialogs.ShowImagesDialog;
import com.telederma.gov.co.dialogs.VerImagenDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.telederma.gov.co.modelo.ImagenLesion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Daniel Hernández on 17/07/2018.
 */

public class ImageArrayAdapter extends ArrayAdapter<String> implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater mInflater;
    List<String> images_url;
    List<String> descriptions;
    JSONObject imagenesDermatoscopia;
    private final View.OnClickListener listener;

    private static final int LAYOUT_ID = R.layout.grid_item_image;

    public ImageArrayAdapter(@NonNull Context context, @NonNull List<String> imagesURL) {
        super(context, LAYOUT_ID, imagesURL);
        mContext = context;
        images_url = imagesURL;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = null;
    }

    public ImageArrayAdapter(@NonNull Context context, @NonNull List<String> imagesURL, @NonNull List<String> descriptionImages) {
        super(context, LAYOUT_ID, imagesURL);
        mContext = context;
        images_url = imagesURL;
        descriptions = descriptionImages;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = null;
    }

    public ImageArrayAdapter(@NonNull Context context, @NonNull List<String> imagesURL, @NonNull List<String> descriptionImages, JSONObject imagenesDermatoscopia, View.OnClickListener listener) {
        super(context, LAYOUT_ID, imagesURL);
        mContext = context;
        images_url = imagesURL;
        descriptions = descriptionImages;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imagenesDermatoscopia = imagenesDermatoscopia;
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {

            listener.onClick(view);
        } else {
            ViewHolder holder = (ViewHolder) view.getTag();
            Dialog dialog = null;
            if (descriptions == null) {
                dialog = new ShowImagesDialog(getContext(), images_url, holder.position);
            } else {
                dialog = new ShowImagesDialog(getContext(), images_url, descriptions, holder.position, "");
            }
            dialog.show();
        }
    }

    public static class ViewHolder {
        public int position;
        ImageView imageview = null;
        Button btn_edited = null;
        Button btn_dermatoscopia = null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(LAYOUT_ID, null);
            holder = new ViewHolder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.img_imagen);
            holder.btn_edited = (Button) convertView.findViewById(R.id.btn_edited);
            holder.btn_dermatoscopia = (Button) convertView.findViewById(R.id.btn_dermatoscopia);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        holder.position = position;

        String description = null;
        if (descriptions != null) {
            if (descriptions.get(position) != null) {
                description = descriptions.get(position);
            }
        }

        if (StringUtils.isEmpty(description)) {
            holder.btn_edited.setVisibility(View.GONE);
        } else {
            holder.btn_edited.setVisibility(View.VISIBLE);
            holder.btn_edited.setOnClickListener(this);
        }


        int numImagenes = obtenerCantidadImagenesDermatoscopicas(item);
        if (numImagenes > 0) {
            holder.btn_dermatoscopia.setVisibility(View.VISIBLE);
            holder.btn_dermatoscopia.setText("Imágenes\n dermatoscópicas - " + numImagenes);
        }




        //Glide.get(mContext).getBitmapPool().clearMemory();
        Glide.with(mContext).asBitmap()
                .load(item)
                .apply(new RequestOptions().centerCrop()
                        .placeholder(R.drawable.cargando)
                        .error(R.drawable.error_cargando)
                        .format(DecodeFormat.PREFER_RGB_565))
                .into(new BitmapImageViewTarget(holder.imageview) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(20);
                        holder.imageview.setImageDrawable(circularBitmapDrawable);
                    }
                });


        convertView.setOnClickListener(this);

        return convertView;
    }

    public int obtenerCantidadImagenesDermatoscopicas(String url) {
        try {
            JSONArray imagenes = new JSONArray(imagenesDermatoscopia.getString(url));
            return imagenes.length();

        } catch (Exception e) {
            return 0;
        }

    }





}
