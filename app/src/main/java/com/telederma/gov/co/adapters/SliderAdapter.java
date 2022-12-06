package com.telederma.gov.co.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telederma.gov.co.R;
import com.telederma.gov.co.SliderCard;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderCard> {

    private int count;
    private List<byte[]> content;
    private List<Boolean> sonImagenesDermatoscopicas;
    private final View.OnClickListener listener;
    private Context context;

    public SliderAdapter(List<byte[]> content, int count, View.OnClickListener listener, Context context) {
        this.content = content;
        this.count = count;
        this.listener = listener;
        this.context = context;
    }

    public SliderAdapter(List<byte[]> content, int count, View.OnClickListener listener, Context context, List<Boolean> sonImagenesDermatoscopicas) {
        this.content = content;
        this.count = count;
        this.listener = listener;
        this.context = context;
        this.sonImagenesDermatoscopicas = sonImagenesDermatoscopicas;
    }

    public void setContent(ArrayList<byte[]> content) {
        this.content = content;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_slider_card, parent, false);

        if (listener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);
                }
            });
        }

        return new SliderCard(view);
    }

    @Override
    public void onBindViewHolder(SliderCard holder, int position) {
        holder.setImage(content.get(position % content.size()), context);

        try{
            if(sonImagenesDermatoscopicas != null){
                if(sonImagenesDermatoscopicas.get(position)){
                    holder.setDermatoscopiaMostrar();
                }
            }
        } catch (Exception e){

        }



    }

    @Override
    public void onViewRecycled(SliderCard holder) {

    }

    @Override
    public int getItemCount() {
        return count;
    }

}
