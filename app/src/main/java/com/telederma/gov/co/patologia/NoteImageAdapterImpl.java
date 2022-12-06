package com.telederma.gov.co.patologia;


import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v4.app.FragmentActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NoteImageAdapterImpl extends NoteImageAdapter<AnatomyItem> {
    private final List<AnatomyItem> items;
    private final FragmentActivity context;
    private Paint labelPaintUnselected;
    private Paint labelPaintSelected;
    private Set<AnatomyItem> selected;

    public NoteImageAdapterImpl(List<AnatomyItem> items, FragmentActivity context, ItemClickListener<AnatomyItem> listener) {
        this.context = context;
        this.items = items;
        this.selected = new HashSet<>();

        /*  Define unselected label paint */
       /* labelPaintUnselected = new Paint();
        labelPaintUnselected.setAntiAlias(true);
        labelPaintUnselected.setStrokeWidth(5);
        labelPaintUnselected.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        labelPaintUnselected.setTextSize(50);

        /*Define selected item paint for labels */
       /* labelPaintSelected = new Paint();
        labelPaintSelected.setAntiAlias(true);
        labelPaintSelected.setFakeBoldText(true);
        labelPaintSelected.setTextSize(50);
        labelPaintSelected.setStrokeWidth(5);
        labelPaintSelected.setColor(ContextCompat.getColor(context, R.color.red));*/

        setItemClickListener(listener);

    }

    public Set<AnatomyItem> get_selected() {
        return selected;
    }

    /*
        Tell the adapter how to get an item coordinate
     */
    @Override
    public PointF getItemCoordinates(AnatomyItem item) {
        return item.getCoordinate();
    }

    /*
        Tell adapter how to retrieve an item based on its position
     */
    @Override
    public AnatomyItem getItemAtPosition(int position) {
        return items.get(position);
    }

    /*
       Tell the adapter the number of total item
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /*
    here we give the adapter the text to show in the sides label lists
     */
    @Override
    public String getLabel(AnatomyItem item) {
        return item.getText();
    }

    /**
     * here we give the function to define the way the adapter retrieve an item bitmap
     */
    @Override
    public Bitmap getItemBitmap(AnatomyItem item) {

        if (selected.contains(item)) {
            return item.getBitmapSelected();
        }
        return item.getBitmapUnselected();
    }

    /**
     * here we define how the paint to use depending on weather an item is "selected" or not
     */
    @Override
    public Paint getLabelPaint(AnatomyItem item) {
        if (selected.contains(item)) {
            return labelPaintSelected;
        }
        return labelPaintUnselected;
    }

    /**
     * If some item label should not link to ce center of the item location you can use getAnchor
     * to define a custom location for the line to link to.
     * PointF(0,0) will link the label to the top left of an item image on the map
     * PointF(0.5,0.5) will link the label to the center. By default the anchor is  PointF(0.5,0.5)
     * so here we define only those which not link to center
     */
    @Override
    public PointF getAnchor(AnatomyItem item) {

        return super.getAnchor(item);
    }


}
