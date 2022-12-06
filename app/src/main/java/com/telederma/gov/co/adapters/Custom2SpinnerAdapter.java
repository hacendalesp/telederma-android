package com.telederma.gov.co.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.telederma.gov.co.R;
import com.telederma.gov.co.modelo.BaseEntity;
import com.telederma.gov.co.modelo.ControlEnfermeria;
import com.telederma.gov.co.modelo.ControlMedico;
import com.telederma.gov.co.modelo.Parametro;

import java.util.List;

public class Custom2SpinnerAdapter<E extends BaseEntity> extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private List<E> data;
    private final Class<E> clazz;

    public Custom2SpinnerAdapter(Context context, List<E> data, Class<E> entityClass) {
        // Se añade elemento en la primera posición para mostrar la etiqueta Seleccionar
        data.add(0, null);
        activity = context;
        this.data = data;
        this.clazz = entityClass;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int i) {
        return data.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(8, 15, 8, 15);
        txt.setTextSize(18);
        txt.setSingleLine();
        txt.setGravity(Gravity.LEFT);
        txt.setPadding(40, 10, 10, 10);
        // txt.setDropDownViewResource(R.drawable.spinner_item_divider);
        GradientDrawable gd = new GradientDrawable();

        //gd.setCornerRadius(5);
        gd.setStroke(1, activity.getResources().getColor(R.color.gray));
        txt.setBackground(gd);
        txt.setText(String.format("- %s", obtenerTexto(data.get(position))));

        return txt;
    }

    private String obtenerTexto(E e) {
        if (e == null) {
            if (getCount() == 0) {
                return activity.getResources().getString(R.string.label_sin_seleccionar);
            }else{
//                if (getCount() > 1) {
                return activity.getResources().getString(R.string.label_seleccionar);
//                }else {
//                    return activity.getResources().getString(R.string.label_sin_seleccionar);
//                }
            }
        }
        if (e instanceof Parametro)
            return ((Parametro) e).getNombre();
        if (e instanceof ControlMedico || e instanceof ControlEnfermeria) {
            if(e.getCreated_at() == null){
                return "Control sincronizando...";
            }else{
                return String.format("%s %s", "Control", (e.getCreated_at() == null ? "sincronizando..." : e.getCreated_at()));
            }
        }

        return String.valueOf(e.getIdServidor());
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down, 0);
        txt.setCompoundDrawableTintList(activity.getResources().getColorStateList(R.color.grayDark));

        txt.setGravity(Gravity.CENTER);
        txt.setTextSize(15);
        txt.setText(obtenerTexto(data.get(i)));

        return txt;
    }

    public int getPosition(int id) {
        if (Parametro.class.equals(clazz))
            for (int i = 1; i <= data.size(); i++) {
                if (((Parametro) data.get(i)).getValor().equals(id))
                    return i;
            }
        else
            for (int i = 1; i <= data.size(); i++) {
                if (data.get(i).getIdServidor().equals(id))
                    return i;
            }

        return 0;
    }

    public int getIndex(int id) {

        for (int i = 1; i <= data.size(); i++) {
            if ((data.get(i)).getId() == id)
                return i;
        }

        return 0;
    }

}