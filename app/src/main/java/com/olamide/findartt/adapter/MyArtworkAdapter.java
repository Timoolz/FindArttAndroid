package com.olamide.findartt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.olamide.findartt.R;
import com.olamide.findartt.models.ArtworkSummary;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyArtworkAdapter extends RecyclerView.Adapter<MyArtworkAdapter.ArtworkAdapterViewHolder> {

    private List<ArtworkSummary> artworkList;
    private Context context;


    public MyArtworkAdapter(List<ArtworkSummary> artworks, Context context) {
        this.artworkList = artworks;
        this.context = context;
    }

    public void setArtworkList(List<ArtworkSummary> artworks) {
        this.artworkList = artworks;
        notifyDataSetChanged();
    }


    public class ArtworkAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.iv_art)
        ImageView artworkImage;

        @BindView(R.id.tv_title)
        TextView artTitle;

        @BindView(R.id.tv_description)
        TextView artDesc;

        @BindView(R.id.tv_bid_buy)
        TextView artBidBuy;


        public ArtworkAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();


        }
    }


    @NonNull
    @Override
    public ArtworkAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_artwork_item, parent, false);

        return new MyArtworkAdapter.ArtworkAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtworkAdapterViewHolder holder, int position) {

        ArtworkSummary currentArtwork = artworkList.get(position);
        String image_url = currentArtwork.getImageUrl();
        Picasso.with(context)
                .load(image_url)
                .fit()
                .placeholder(R.drawable.img_area_24dp)
                .error(R.drawable.img_off_24dp)
                .into(holder.artworkImage);

        holder.artTitle.setText(currentArtwork.getName());
        holder.artDesc.setText(currentArtwork.getDescription());


        holder.artBidBuy.setText(getBigBuyText(currentArtwork));

    }


    private String getBigBuyText(ArtworkSummary currentArtwork) {

        String bidBuyText = "";

        switch (currentArtwork.getPurchaseType()) {
            case BUY:
                if (currentArtwork.getCurrentBuy() != null) {
                    bidBuyText = "This Artwork Has been Sold";
                } else {
                    bidBuyText = "This Artwork is yet to be Sold";
                }

                return bidBuyText;

            case BID:

                if (currentArtwork.getAcceptedBid() != null) {
                    bidBuyText = "You have Accepted a bid for this Artwork";
                }
                if (currentArtwork.getBidCount() != null && currentArtwork.getBidCount() > 0) {
                    bidBuyText = "There are currently " + currentArtwork.getBidCount() + " bid(s) for this artwork";
                } else {
                    bidBuyText = "No bid has been made for this Artwork";
                }

                return bidBuyText;

            default:
                return bidBuyText;
        }

    }

    @Override
    public int getItemCount() {
        if (artworkList != null) {
            return artworkList.size();
        } else {
            return 0;
        }
    }
}
