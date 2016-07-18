package com.lucien.colormemory.model;

import com.lucien.colormemory.constant.KeyMap;

/**
 * Created by Lucien on 16/7/2016.
 */
public class Card {

    public static final int STATUS_BACK = 0;
    public static final int STATUS_FRONT = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_REMOVE = -1;

    private int cardNum;
    private int position;
    private int cardFront;
    private int cardBack;
    private int cardStatus;

    public Card(int cardNum) {
        this.cardNum = cardNum;
        cardFront = KeyMap.CARD_TYPE_MAP.get(cardNum).getCardFront();
        cardBack = KeyMap.CARD_TYPE_MAP.get(cardNum).getCardBack();
    }

    public Card(int cardNum, int cardFront, int cardBack) {
        this.cardNum = cardNum;
        this.cardFront = cardFront;
        this.cardBack = cardBack;
    }

    public int getCardNum() {
        return cardNum;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCardFront() {
        return cardFront;
    }

    public int getCardBack() {
        return cardBack;
    }

    public int getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(int cardStatus) {
        this.cardStatus = cardStatus;
    }

    public int loadCardImg() {
        if (getCardStatus() == STATUS_BACK) {
            return getCardBack();
        } else if (getCardStatus() == STATUS_FRONT) {
            return getCardFront();
        } else {
            return -1;
        }
    }

    public boolean match(Card card) {
        return this.cardNum == card.getCardNum()
                && this.position != card.getPosition();
    }
}
