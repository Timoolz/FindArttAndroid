package com.olamide.findartt.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olamide.findartt.R;
import com.olamide.findartt.enums.BidStatus;
import com.olamide.findartt.models.Bid;
import com.olamide.findartt.utils.Converters;
import com.olamide.findartt.utils.GeneralUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidAdapterViewHolder> {


    private List<Bid> bidList;
    private Context context;


    public BidAdapter(List<Bid> bids, Context context) {
        this.bidList = bids;
        this.context = context;


    }


    public void setBidList(List<Bid> bids) {
        this.bidList = bids;
        notifyDataSetChanged();
    }


    public interface BidAdapterOnClickListener {
        void onClickListener(Bid bid);
    }


    public class BidAdapterViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_date)
        TextView tvDate;

        @BindView(R.id.tv_amount)
        TextView tvAmount;

//        @BindView(R.id.tv_user)
//        TextView tvUser;

        @BindView(R.id.cv_bid_item)
        CardView cvBidItem;


        public BidAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }


    }


    @NonNull
    @Override
    public BidAdapter.BidAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.bid_item, viewGroup, false);

        return new BidAdapter.BidAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidAdapter.BidAdapterViewHolder holder, int position) {

        Bid currentBid = bidList.get(position);
//        String image_url = currentBid.getMadeBy();
//        //image_url = "https://firebasestorage.googleapis.com/v0/b/maximus-163513.appspot.com/o/fifa-19-de-bruyne_due7v4yr8dly1j4det4we9efy.jpg?alt=media&token=bb6f428a-dfdc-40fd-8c56-09622410d170";
//        Picasso.with(context)
//                .load(image_url)
//                .fit()
//                .placeholder(R.drawable.img_area_24dp)
//                .error(R.drawable.img_off_24dp)
//                .into(holder.bidImage);

        holder.tvAmount.setText(currentBid.getAmount().toString());

//        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM-d-yyyy", Locale.ENGLISH);
//        Date date = Converters.toDate(currentBid.getCreatedDateEpoch());
//        holder.tvDate.setText(outputFormat.format(date));
        holder.tvDate.setText(GeneralUtils.dateFromNowFormat(currentBid.getCreatedDateEpoch()));

        holder.cvBidItem.setBackgroundColor(ContextCompat.getColor(context, R.color.success));
        if (currentBid.getBidStatus().equals(BidStatus.COUNTERED)) {

            holder.cvBidItem.setAlpha(.5f);
        }


    }


    @Override
    public int getItemCount() {
        if (bidList != null) {
            return bidList.size();
        } else {
            return 0;
        }
    }
}
