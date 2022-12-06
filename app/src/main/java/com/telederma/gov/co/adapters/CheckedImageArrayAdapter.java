package com.telederma.gov.co.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.dialogs.ShowImagesDialog;
import com.telederma.gov.co.modelo.CheckedImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.ObjectKey;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CheckedImageArrayAdapter extends ArrayAdapter<CheckedImage> implements View.OnClickListener {
    ShowImagesDialog dialog = null;

    @Override
    public void onClick(View view) {

        if (listener != null) {

            listener.onClick(view);
        } else {
            View parent = (View) view.getParent();
            ViewHolder holder = (ViewHolder) parent.getTag();
            //NotaSebas:  Se comentó esta linea evitando la sobrecarga de la clase previamente instanciada.
            //Dialog dialog = new ShowImagesDialog(getContext(),rutas ,holder.position);

            if(list_images.get(holder.position).getUrl().equals("captura_imagen_dermatoscopia")){
                ((Activity) mContext).finish();
                return;
            }
            if (dialog == null) {
                dialog = new ShowImagesDialog(getContext(), rutas, holder.position);
            } else {
                dialog.showImage(holder.position);
            }
            dialog.show();
        }


    }

    public static class ViewHolder {
        public int position;
        public ImageView imageview = null;
        public CheckBox checkbox = null;
        public Button button = null;
        public String url = "";
    }

    private final static int LAYOUT_ID = R.layout.grid_item_cam;


    private Context mContext;
    private LayoutInflater mInflater;
    private List<CheckedImage> list_images;
    private List<String> rutas;
    private JSONObject imagenesDermatoscopicas;
    private final View.OnClickListener listener;


    public CheckedImageArrayAdapter(Context context, List<CheckedImage> objects) {
        super(context, LAYOUT_ID, objects);
        mContext = context;
        list_images = objects;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rutas = getImagenes();
        this.listener = null;
    }

    public CheckedImageArrayAdapter(Context context, List<CheckedImage> objects, JSONObject imagenesDermatoscopicas, View.OnClickListener listener) {
        super(context, LAYOUT_ID, objects);
        mContext = context;
        list_images = objects;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rutas = getImagenes();
        this.imagenesDermatoscopicas = imagenesDermatoscopicas;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(LAYOUT_ID, null);
            holder = new ViewHolder();
            holder.imageview = (ImageView) convertView.findViewById(R.id.imageView1);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.button = (Button) convertView.findViewById(R.id.button1);
            holder.checkbox.setOnCheckedChangeListener(CheckBox1_OnCheckedChangeListener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CheckedImage item = getItem(position);
        holder.position = position;

        Object urlImagen = "file://" + item.getUrl().split("___")[0];
        holder.url = item.getUrl();
        if(item.getUrl().split("___")[0].equals("captura_imagen_dermatoscopia")){
            urlImagen = mContext.getResources().getIdentifier("anadir_img_dermatoscopicas", "drawable", mContext.getPackageName());
            holder.checkbox.setVisibility(View.GONE);
        } else if(item.getUrl().split("___")[0].equals("captura_imagen")){
            urlImagen = mContext.getResources().getIdentifier("anadir_img", "drawable", mContext.getPackageName());
            holder.checkbox.setVisibility(View.GONE);
        }

        //Glide.get(mContext).getBitmapPool().clearMemory();
        Glide.with(mContext).asBitmap()
                .load(urlImagen)
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

        holder.imageview.setOnClickListener(this);
        holder.checkbox.setChecked(item.getChecked());

        try {
            if (imagenesDermatoscopicas != null) {
                int numImagenes = obtenerCantidadImagenesDermatoscopicas(item.getUrl());
                if (numImagenes > 0) {
                    holder.button.setVisibility(View.VISIBLE);
                    holder.button.setText("Imágenes\n dermatoscópicas - " + numImagenes);
                }
            }
        } catch (Exception e) {

        }

        return convertView;

    }

    public int obtenerCantidadImagenesDermatoscopicas(String url) {
        try {
            JSONArray imagenes = new JSONArray(imagenesDermatoscopicas.getString(url));
            return imagenes.length();

        } catch (Exception e) {
            return 0;
        }

    }

    public List<CheckedImage> getCheckedItem() {
        List<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        for (int i = 0; i < getCount(); i++)
            if (getItem(i).getChecked())
                lstItem.add(getItem(i));

        return lstItem;
    }

    public List<CheckedImage> getNotCheckedItem() {
        List<CheckedImage> lstItem = new ArrayList<CheckedImage>();
        for (int i = 0; i < getCount(); i++)
            if (!getItem(i).getChecked())
                lstItem.add(getItem(i));

        return lstItem;
    }

    private CompoundButton.OnCheckedChangeListener CheckBox1_OnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            View view = (View) buttonView.getParent();
            ViewHolder holder = (ViewHolder) view.getTag();
            CheckedImage item = CheckedImageArrayAdapter.this.getItem(holder.position);
            item.setChecked(isChecked);
        }
    };

    private List<String> getImagenes() {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < list_images.size(); i++) {
            if(!list_images.get(i).getUrl().split("___")[0].equals("captura_imagen_dermatoscopia") && !list_images.get(i).getUrl().split("___")[0].equals("captura_imagen")) {
                images.add(list_images.get(i).getUrl());
            }
        }
        return images;
    }


}