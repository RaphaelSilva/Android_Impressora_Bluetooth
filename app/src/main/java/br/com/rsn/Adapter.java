package br.com.rsn;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Raphael on 27/02/2015.
 */
public abstract class Adapter<T> extends BaseAdapter {

    protected Context context;
    protected int layout;
    private List list;

    public Adapter(Context context, int layout, List<T> list) {
        this.context = context;
        this.layout = layout;
        this.list = new ArrayList(list);
    }

    protected View inflater(){
        return inflater(null);
    }

    protected View inflater(ViewGroup viewGroup){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(layout, viewGroup);
    }

    protected boolean contains(T a){
        return list.contains(a);
    }

    public void add(T a){
        list.add(a);
        notifyDataSetChanged();
    }

    public void addAll(List<? extends T> list_new){
        list = new ArrayList(list_new);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public T getItem(int position) {
        return (T) list.get(position);
    }

    @Override
    public long getItemId(int position) {
        T obj = (T) list.get(position);
        if(obj instanceof RSNObj){
            return ((RSNObj) obj).getId();
        }
        return position;
    }

    @Override
    public boolean isEmpty() { return list.isEmpty(); }

}
