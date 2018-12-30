package com.oleaf.eighthours;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

class SpanAdapter extends RecyclerView.Adapter<SpanAdapter.SpanViewHolder>{
    Span[] spans;

    SpanAdapter(Span[] spans){
        this.spans = spans;
    }

    @Override
    public SpanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false); //a
        SpanViewHolder viewHolder = new SpanViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( SpanViewHolder spanViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class SpanViewHolder extends RecyclerView.ViewHolder{
        TextView t;

        public SpanViewHolder(TextView text){
            super(text);
            t = text;
        }
    }
}