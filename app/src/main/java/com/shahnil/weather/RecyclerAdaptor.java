package com.shahnil.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecyclerAdaptor extends RecyclerView.Adapter<RecyclerAdaptor.ViewHolder> {

    private ArrayList<WeekData> weeklist;
    private Context mcontext;
    private Onitemclicklistener mlistener;

    public interface Onitemclicklistener{
        void onitemclick(int position);
    }

    public void setOnitemclicklistener(Onitemclicklistener listener){
        mlistener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mDate;
        public TextView mTemp;
        public TextView mHumidity;
        public TextView mWind;
        public TextView mWeather;

        public ViewHolder(@NonNull View itemView, final Onitemclicklistener listener) {
            super(itemView);

            mTemp = itemView.findViewById(R.id.textViewtemp);
            mDate = itemView.findViewById(R.id.textViewDate);
            mWind = itemView.findViewById(R.id.textViewWind);
            mWeather = itemView.findViewById(R.id.textViewWeather);
            mHumidity = itemView.findViewById(R.id.textViewHumidity);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onitemclick(position);
                        }
                    }

                }
            });
        }
    }

    public RecyclerAdaptor(Context context, ArrayList<WeekData> userlist) {
        mcontext = context;
        this.weeklist = userlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_data_layout, parent ,false);
        ViewHolder vh = new ViewHolder(view,mlistener);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeekData data = weeklist.get(position);


        holder.mDate.setText(data.getMdate());
        holder.mTemp.setText(data.getMtemp());
        holder.mWeather.setText(data.getmStringWeather());
        holder.mWind.setText(data.getMwind());
        holder.mHumidity.setText(data.getMhumidity());



    }

    @Override
    public int getItemCount() {
        return weeklist.size();
    }
}
