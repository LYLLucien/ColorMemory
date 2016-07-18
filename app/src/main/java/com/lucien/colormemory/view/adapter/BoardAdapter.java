package com.lucien.colormemory.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lucien.colormemory.R;
import com.lucien.colormemory.app.Config;
import com.lucien.colormemory.model.Card;

import java.util.List;

/**
 * Created by Lucien on 16/7/2016.
 */
public class BoardAdapter extends RecyclerView.Adapter {

    private Context context;
    private OnItemClickListener listener;
    private int itemHeight;
    private int itemWidth;
    private List<Card> cardList;

    private class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivCard;
        private CardView cvCard;

        public ViewHolder(View root) {
            super(root);
            cvCard = (CardView) root.findViewById(R.id.cvCard);
            ivCard = (ImageView) root.findViewById(R.id.ivCard);
        }


    }

    public BoardAdapter(Context context, List<Card> cardList, OnItemClickListener listener) {
        this.context = context;
        this.cardList = cardList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemHeight = parent.getHeight() / Config.BOARD_ROW;
        itemWidth = parent.getWidth() / Config.BOARD_COL;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder item = (ViewHolder) holder;
        final int num = position;
        ViewGroup.LayoutParams lp = item.cvCard.getLayoutParams();
        lp.height = itemHeight;
        lp.width = itemWidth;
        item.cvCard.setLayoutParams(lp);

        Card card = cardList.get(position);
        card.setPosition(position);

        if (card.getCardStatus() == Card.STATUS_REMOVE) {
            item.ivCard.setVisibility(View.GONE);
        } else {
            item.ivCard.setImageResource(card.loadCardImg());
        }
        if (listener != null) {
            item.ivCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(view, num);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Config.BOARD_ROW * Config.BOARD_COL;
    }
}
