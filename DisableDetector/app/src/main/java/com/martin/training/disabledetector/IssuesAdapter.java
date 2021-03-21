package com.martin.training.disabledetector;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.ViewHolder>{

    private ArrayList<String[]> elements;

    public IssuesAdapter(Context context, ArrayList<String[]> e){
        elements = e;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setTag(elements.get(position));
        holder.loc.setText(elements.get(position)[1]);
        holder.date.setText(elements.get(position)[2]);
        holder.cond.setText(elements.get(position)[3]);


        if(holder.cond.getText().toString().equals("Critical")){
            holder.ivPrev.setImageResource(R.mipmap.ic_critical_filled_foreground);
        } else if(holder.cond.getText().toString().equals("Unusable")){
            holder.ivPrev.setImageResource(R.mipmap.ic_unusable_filled_foreground);
        } else{
            holder.ivPrev.setImageResource(R.mipmap.ic_check_filled_foreground);
        }


        //holder.ivPrev.setImageURI(Uri.parse(elements.get(position)[4]));
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cond, loc, date;
        ImageView ivPrev;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cond = itemView.findViewById(R.id.TVcondition);
            loc = itemView.findViewById(R.id.TVlocation);
            date = itemView.findViewById(R.id.TVdate);
            ivPrev = itemView.findViewById(R.id.IVincident);

        }
    }
}
