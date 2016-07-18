package com.lucien.colormemory.util.anim;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.lucien.colormemory.R;
import com.lucien.colormemory.model.Card;
import com.lucien.colormemory.model.CardPair;

/**
 * Created by Lucien on 17/7/2016.
 */
public class AnimUtils {

    public interface OnCardReverseListener {
        void onReverseDone(int pairNum);
    }

    public interface OnPairStatusListener {
        void onReversePair(CardPair pair);

        void onRemovePair(CardPair pair);
    }

    public static void TurnCardAnimation(Context context, final ImageView view,
                                         final Card card, final int pairNum,
                                         final OnCardReverseListener listener) {
        final int cardStatus = card.getCardStatus();
        final Animation animation0 = AnimationUtils.loadAnimation(context,
                R.anim.turn_card_0);
        final Animation animation1 = AnimationUtils.loadAnimation(context,
                R.anim.turn_card_1);
        animation0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                card.setCardStatus(Card.STATUS_RUNNING);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                card.setCardStatus(cardStatus);
                if (card != null) {
                    view.setImageResource(card.loadCardImg());
                }
                view.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                card.setCardStatus(Card.STATUS_RUNNING);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                card.setCardStatus(cardStatus);
                if (listener != null) {
                    listener.onReverseDone(pairNum);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animation0);
    }

    public static void RemoveCardAnimation(Context context, final ImageView view,
                                           final Card card, final int pairNum,
                                           final OnCardReverseListener listener) {
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.remove);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.onReverseDone(pairNum);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public static void ReversePairAnimation(Context context, final CardPair pair,
                                            final OnPairStatusListener listener) {
        synchronized (new byte[0]) {
            if (pair != null) {
                Card curCard = pair.getCurCard();
                Card lastCard = pair.getLastCard();
                ImageView curView = (ImageView) pair.getCurView();
                ImageView lastView = (ImageView) pair.getLastView();

                curCard.setCardStatus(Card.STATUS_BACK);
                lastCard.setCardStatus(Card.STATUS_BACK);
                TurnCardAnimation(context, curView,
                        curCard, pair.getPairNum(), null);
                TurnCardAnimation(context, lastView,
                        lastCard, pair.getPairNum(), new OnCardReverseListener() {
                            @Override
                            public void onReverseDone(int pairNum) {
                                if (listener != null) {
                                    listener.onReversePair(pair);
                                }
                            }
                        });
            }
        }
    }

    public static void RemovePairAnimation(Context context, final CardPair pair,
                                           final OnPairStatusListener listener) {
        synchronized (new byte[0]) {
            if (pair != null) {
                Card curCard = pair.getCurCard();
                Card lastCard = pair.getLastCard();
                curCard.setCardStatus(Card.STATUS_REMOVE);
                lastCard.setCardStatus(Card.STATUS_REMOVE);
                RemoveCardAnimation(context, (ImageView) pair.getCurView(),
                        pair.getCurCard(), pair.getPairNum(), new OnCardReverseListener() {
                            @Override
                            public void onReverseDone(int pairNum) {
                                pair.getCurView().setVisibility(View.GONE);
                            }
                        });
                RemoveCardAnimation(context, (ImageView) pair.getLastView(),
                        pair.getLastCard(), pair.getPairNum(), new OnCardReverseListener() {
                            @Override
                            public void onReverseDone(int pairNum) {
                                pair.getLastView().setVisibility(View.GONE);
                                if (listener != null) {
                                    listener.onRemovePair(pair);
                                }
                            }
                        });
            }
        }
    }

    public static void PulseAnimation(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.pulse);
        view.startAnimation(animation);
    }

    public static void ShakeAnimation(Context context, View view) {
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.shake);
        view.startAnimation(animation);
    }
}
