package com.example.parcial_v2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parcial_v2.Data.Music;

import java.util.ArrayList;

public class Adapter_music extends RecyclerView.Adapter<Adapter_music.ViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, duration, autor;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            autor = (TextView) itemView.findViewById(R.id.tvAutor);
            duration = (TextView) itemView.findViewById(R.id.tvDuration);
        }
    }

    public ArrayList<Music> Musics;

    public Adapter_music(ArrayList<Music> Music) {
        this.Musics = Music;
    }

    @Override
    public Adapter_music.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);

        Adapter_music.ViewHolder viewHolder = new Adapter_music.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter_music.ViewHolder holder, int position) {
        holder.title.setText(Musics.get(position).title);
        holder.autor.setText(Musics.get(position).autor);
        holder.duration.setText(Musics.get(position).duration);
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }


    @Override
    public int getItemCount() {
        return Musics.size();
    }
}
