package com.example.kimhobin;

import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {

    private ArrayList<Item> data = null;
    Activity activity;

    public RecyclerAdapter(ArrayList<Item> item, Activity activity)
    {
        data = item;
        this.activity = activity;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imgView;
        protected TextView txtTitle;
        protected TextView txtPeople;
        protected TextView txtGenre;
        protected RatingBar ratingBar;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.imgView = (ImageView)itemView.findViewById(R.id.imgView);
            this.txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            this.txtPeople = (TextView)itemView.findViewById(R.id.txtPeople);
            this.txtGenre = (TextView)itemView.findViewById(R.id.txtGenre);
            this.ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);

        }
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        int resId = getResId(data.get(position).getImgName(),R.drawable.class);
        viewholder.imgView.setImageResource(resId);
        viewholder.txtTitle.setText(data.get(position).getTitle());
        viewholder.txtPeople.setText(data.get(position).getPeople());
        viewholder.txtGenre.setText(toGenre(data.get(position).getGenre()));
        viewholder.ratingBar.setRating((float) data.get(position).rating);
    }
    public static int getResId(String resName, Class<?> c){
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void filterList(ArrayList<Item> filteredList){
        data = filteredList;
        notifyDataSetChanged();
    }
    public String toGenre(int genreId){
        String genre;
        switch (genreId){
            case 1:
                genre = "#전략";
                break;
            case 2:
                genre = "#추리";
                break;
            case 3:
                genre = "#순발력";
                break;
            case 4:
                genre = "#매니아";
                break;
            case 5:
                genre = "#두뇌 싸움";
                break;
            case 6:
                genre = "#운빨";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + genreId);
        }
        return genre;
    }
}
