package com.olamide.findartt.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.olamide.findartt.R;
import com.olamide.findartt.models.Artwork;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ArtworkAdapterViewHolder> {




    private List<Artwork> artworkList;
    private Context context;

    private final ArtworkAdapterOnClickListener artworkClickListener;

    public ArtworkAdapter(List<Artwork> artworks, Context context, ArtworkAdapterOnClickListener artworkClickListener) {
        this.artworkList = artworks;
        this.context = context;
        this.artworkClickListener = artworkClickListener;
    }


    public void setArtworkList(List<Artwork> artworks){
        this.artworkList = artworks;
        notifyDataSetChanged();
    }


    public interface  ArtworkAdapterOnClickListener {
        void onClickListener(Artwork artwork);
    }


    public  class ArtworkAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.iv_art)
        ImageView artworkImage;

        @BindView(R.id.tv_title)
        TextView artTitle;


        public ArtworkAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            artworkClickListener.onClickListener(artworkList.get(adapterPosition));

        }
    }


    @NonNull
    @Override
    public ArtworkAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.art_item, viewGroup, false);

        return new ArtworkAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkAdapterViewHolder holder, int position) {

        Artwork currentArtwork = artworkList.get(position);
        String image_url = currentArtwork.getImageUrl();
        //image_url = "https://firebasestorage.googleapis.com/v0/b/maximus-163513.appspot.com/o/fifa-19-de-bruyne_due7v4yr8dly1j4det4we9efy.jpg?alt=media&token=bb6f428a-dfdc-40fd-8c56-09622410d170";
        Picasso.with(context)
                .load(image_url)
                .fit()
                .placeholder(R.drawable.img_load)
                .error(R.drawable.img_error)
                .into(holder.artworkImage);

        holder.artTitle.setText(currentArtwork.getName());



    }



    @Override
    public int getItemCount() {
        if(artworkList != null){
            return artworkList.size();
        }else {
            return 0;
        }
    }
}