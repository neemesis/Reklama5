package com.toshevski.android.reklama5.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.toshevski.android.reklama5.R;
import com.toshevski.android.reklama5.pojos.OglasOsnovno;

import java.util.ArrayList;

public class OglasOsnovnoAdapter extends RecyclerView.Adapter<OglasOsnovnoAdapter.MyViewHolder> {
    private ArrayList<OglasOsnovno> oglasi;
    private Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ad_title, ad_location, ad_price, ad_time;
        public ImageView ad_image;

        public MyViewHolder(View view) {
            super(view);
            ad_title = (TextView) view.findViewById(R.id.ad_title);
            ad_location = (TextView) view.findViewById(R.id.ad_location);
            ad_price = (TextView) view.findViewById(R.id.ad_price);
            ad_time = (TextView) view.findViewById(R.id.ad_time);
            ad_image = (ImageView) view.findViewById(R.id.ad_image);
        }
    }

    public void setList(ArrayList<OglasOsnovno> oglasi) {
        this.oglasi = oglasi;
    }

    public OglasOsnovnoAdapter(Context ctx, ArrayList<OglasOsnovno> oglasi) {
        this.oglasi = oglasi;
        this.ctx = ctx;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public OglasOsnovnoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.oglas_osnovno_eden, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        OglasOsnovno o = oglasi.get(position);
        holder.ad_price.setText(o.getCena());
        holder.ad_location.setText(o.getLokacija());
        holder.ad_time.setText(o.getVreme());
        holder.ad_title.setText(o.getIme());
        Picasso.with(ctx).load(o.getSlika()).resize(0, 75).into(holder.ad_image);
    }

    public int getItemCount() {
        return oglasi.size();
    }
}
