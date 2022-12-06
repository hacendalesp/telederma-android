package com.telederma.gov.co.patologia;

import android.graphics.Bitmap;
import android.graphics.PointF;


public abstract class MapAdapter<T> {

    /**
     * Listener used to communicate from adapter to view
     */
    public AdapterListener listener;

    /**
     * The listener of item click on map
     */
    public ItemClickListener<T> itemClickListener;

    /**
     * @param itemClickListener to set as new listener for item click
     */
    public void setItemClickListener(ItemClickListener<T> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * Define this function to target where the item is positioned on the image
     *
     * @param item Item to get position
     * @return PointF(x, y) x and y are float 0 to 1
     * x is ratio of image width (e. x = 0.5f  item is centered horizontally)
     * y is ratio of image height (e. y =  1f  item will be at bottom)
     */
    public abstract PointF getItemCoordinates(T item);

    /**
     * Define this function to indicate which item the given index correspond to
     * (for iterating purpose)
     *
     * @param position int, index of item
     * @return the item corresponding to the position index
     */
    public abstract T getItemAtPosition(int position);

    /**
     * define this function to specifie the number of item you want to display
     *
     * @return int, the number of item to display
     */

    public abstract int getCount();

    /**
     * Override this method to a custom bitmap to draw for each element
     *
     * @param item the item of which the bitmap will correspond
     * @return the Bitmap to draw for given item
     */
    public abstract Bitmap getItemBitmap(T item);


    /**
     * Wrap adapter function to handle null bitmap
     *
     * @param item the item of which the bitmap will correspond
     * @return the Bitmap to draw for given item or an empty bitmap
     */
    public Bitmap getNotNullBitmap(T item) {
        Bitmap itemBitmap = getItemBitmap(item);
        if (itemBitmap == null) {
            itemBitmap = BitmapUtils.getEmptyBitmap();
        }
        return itemBitmap;
    }

    /**
     * Call this method to notify a change occurred on data
     */
    public void notifyDataSetHasChanged() {
        if (listener != null) {
            listener.notifyDataSetHasChanged();
        }
    }
}