package com.lucien.colormemory.model;

import android.view.View;

/**
 * Created by Lucien on 17/7/2016.
 */
public class CardPair {

    private int pairNum;
    private Card curCard;
    private View curView;
    private Card lastCard;
    private View lastView;
    private boolean isMatch;

    public int getPairNum() {
        return pairNum;
    }

    public void setPairNum(int pairNum) {
        this.pairNum = pairNum;
    }

    public Card getCurCard() {
        return curCard;
    }

    public void setCurCard(Card curCard) {
        this.curCard = curCard;
    }

    public View getCurView() {
        return curView;
    }

    public void setCurView(View curView) {
        this.curView = curView;
    }

    public Card getLastCard() {
        return lastCard;
    }

    public void setLastCard(Card lastCard) {
        this.lastCard = lastCard;
    }

    public View getLastView() {
        return lastView;
    }

    public void setLastView(View lastView) {
        this.lastView = lastView;
    }

    public boolean isMatch() {
        if (curCard != null && lastCard != null) {
            isMatch = curCard.match(lastCard);
        }
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }
}
