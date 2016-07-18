package com.lucien.colormemory.constant;

import com.lucien.colormemory.R;
import com.lucien.colormemory.model.Card;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lucien on 17/7/2016.
 */
public class KeyMap {

    public static final Map<Integer, Card> CARD_TYPE_MAP
            = Collections.unmodifiableMap(new HashMap<Integer, Card>() {
        {
            put(-1, null);
            put(1, new Card(1, R.drawable.colour1, R.drawable.card_bg));
            put(2, new Card(2, R.drawable.colour2, R.drawable.card_bg));
            put(3, new Card(3, R.drawable.colour3, R.drawable.card_bg));
            put(4, new Card(4, R.drawable.colour4, R.drawable.card_bg));
            put(5, new Card(5, R.drawable.colour5, R.drawable.card_bg));
            put(6, new Card(6, R.drawable.colour6, R.drawable.card_bg));
            put(7, new Card(7, R.drawable.colour7, R.drawable.card_bg));
            put(8, new Card(8, R.drawable.colour8, R.drawable.card_bg));
        }
    });
}
