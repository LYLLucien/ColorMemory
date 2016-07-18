package com.lucien.colormemory.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucien.colormemory.R;
import com.lucien.colormemory.app.Config;
import com.lucien.colormemory.dao.DBDao;
import com.lucien.colormemory.model.Card;
import com.lucien.colormemory.model.CardPair;
import com.lucien.colormemory.util.anim.AnimUtils;
import com.lucien.colormemory.util.common.CommonUtils;
import com.lucien.colormemory.view.adapter.BoardAdapter;
import com.lucien.colormemory.view.adapter.OnItemClickListener;
import com.lucien.colormemory.view.widget.CleanableEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity
        implements OnItemClickListener, AnimUtils.OnCardReverseListener,
        AnimUtils.OnPairStatusListener, View.OnClickListener {

    private Context context;
    private long firstTime;
    private DBDao dbDao;
    private TextView tvScore;
    private Button btnRank;
    private RecyclerView rvBoard;
    private BoardAdapter adapter;
    private int[] cardsOrder;
    private int count = 0;
    private int score = 0;
    private List<Card> cardList;
    private Map<Integer, CardPair> pairMap;
    private List<CardPair> finishList;
    private AlertDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pairNum = msg.what;
            CardPair pair = pairMap.get(pairNum);
            if (pair != null) {
                if (!pair.isMatch()) {
                    reversePair(pairNum);
                } else {
                    removePair(pairNum);
                }
                countScore(pair.isMatch());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBoardConfig();
        initView();
        initGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initBoardConfig() {
        context = this;
        dbDao = DBDao.getDBDaoInstance(this);
        prepareGame();
    }

    private void prepareGame() {
        pairMap = new HashMap<>();
        finishList = new ArrayList<>();
        cardsOrder = CommonUtils.ShuffleArray(Config.CARDS);
        cardList = new ArrayList<>();
        System.out.println("array order: " + Arrays.toString(cardsOrder));
        for (int type : cardsOrder) {
            Card card = new Card(type);
            cardList.add(card);
        }
    }

    private void initView() {
        tvScore = (TextView) this.findViewById(R.id.tvScore);
        btnRank = (Button) this.findViewById(R.id.btnRank);
        btnRank.setOnClickListener(this);
        rvBoard = (RecyclerView) this.findViewById(R.id.rvBoard);
    }

    private void initGame() {
        resumeConfig();
        setRecyclerAdapter(rvBoard);
    }

    private void resumeConfig() {
        count = 0;
        score = 0;
        updateScore(score);
    }

    private void updateScore(int score) {
        tvScore.setText(score + "");
    }

    private void setRecyclerAdapter(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(this,
                Config.BOARD_COL, LinearLayoutManager.VERTICAL, false));
        adapter = new BoardAdapter(this, cardList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View v, int position) {
        System.out.println("position: " + position);
        Card card = cardList.get(position);
        card.setPosition(position);
        int pairNum = count / 2;

        if (card.getCardStatus() == Card.STATUS_BACK) {
            System.out.println("STATUS_BACK");
            count++;
            System.out.println("count: " + count);
            boolean isCallback = (count % 2 == 0);
            setPair(v, card, pairNum);
            turnCard(v, card, pairNum, isCallback);
        } else if (card.getCardStatus() == Card.STATUS_FRONT) {
            System.out.println("STATUS_FRONT");
            AnimUtils.ShakeAnimation(this, v);
        } else if (card.getCardStatus() == Card.STATUS_RUNNING) {
            System.out.println("running");
        }
    }

    /**
     * reverse card
     * @param v
     * @param card
     * @param pairNum
     * @param isCallback
     */
    private void turnCard(View v, Card card, int pairNum, boolean isCallback) {
        synchronized (new byte[0]) {
            if (card.getCardStatus() == Card.STATUS_BACK) {
                card.setCardStatus(Card.STATUS_FRONT);
            } else if (card.getCardStatus() == Card.STATUS_FRONT) {
                card.setCardStatus(Card.STATUS_BACK);
            }
            if (isCallback) {
                AnimUtils.TurnCardAnimation(this, (ImageView) v, card, pairNum, this);
            } else {
                AnimUtils.TurnCardAnimation(this, (ImageView) v, card, pairNum, null);
            }
        }
    }

    /**
     * set card to pair
     * @param view card view
     * @param card card object
     * @param pairNum pair number
     */
    private void setPair(View view, Card card, int pairNum) {
        CardPair pair;
        if (pairMap.get(pairNum) == null) {
            pair = new CardPair();
            pair.setPairNum(pairNum);
        } else {
            pair = pairMap.get(pairNum);
        }
        if (count % 2 == 0) {
            pair.setCurCard(card);
            pair.setCurView(view);
            System.out.println("is Match: " + pair.isMatch());
        } else {
            pair.setLastCard(card);
            pair.setLastView(view);
        }
        pairMap.put(pairNum, pair);
    }

    /**
     * calculate total score
     * @param isMatch is pair match
     */
    private void countScore(boolean isMatch) {
        if (isMatch) {
            score += 2;
        } else {
            score -= 1;
        }
        updateScore(score);
        AnimUtils.PulseAnimation(this, tvScore);
    }

    /**
     * start reverse pair animation by pair number
     * @param pairNum
     */
    private void reversePair(int pairNum) {
        AnimUtils.ReversePairAnimation(this, pairMap.get(pairNum), this);
    }

    /**
     * start remove pair animation by pair number
     * @param pairNum
     */
    private void removePair(int pairNum) {
        AnimUtils.RemovePairAnimation(this, pairMap.get(pairNum), this);
    }

    /**
     * reverse animation done
     *
     * @param pairNum
     */
    @Override
    public void onReverseDone(int pairNum) {
        System.out.println("reverse");
        if (pairNum >= 0) {
            handler.sendEmptyMessageDelayed(pairNum, 1000);
        }
    }

    /**
     * when pair mismatch, reverse both cards
     *
     * @param pair
     */
    @Override
    public void onReversePair(CardPair pair) {
        System.out.println("reverse pair: " + pair.getPairNum());
        pairMap.remove(pair.getPairNum());
        System.out.println("pair map count: " + pairMap.size());

    }

    /**
     * when pair match, remove pair
     *
     * @param pair
     */
    @Override
    public void onRemovePair(CardPair pair) {
        System.out.println("remove pair: " + pair.getPairNum());
        pairMap.remove(pair.getPairNum());
        finishList.add(pair);
        System.out.println("finish pairs count: " + finishList.size());
        if (finishList.size() == cardsOrder.length / 2) {
            CommonUtils.ShowToast(this, "Finished!");
            showInputDialog();
        }
    }

    /**
     * after finishing game, an input will popup, which can input user name
     */
    private void showInputDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.layout_input, null, false);
        final CleanableEditText et_username = (CleanableEditText) view.findViewById(R.id.et_username);
        Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_username.getText().toString();
                if (TextUtils.isEmpty(name) ||
                        TextUtils.isEmpty(name.trim())) {
                    AnimUtils.ShakeAnimation(context, et_username);
                    CommonUtils.ShowToast(context, "Empty name not allowed!");
                    return;
                } else if (name.startsWith(" ")) {
                    AnimUtils.ShakeAnimation(context, et_username);
                    CommonUtils.ShowToast(context, "Name cannot start with a space!");
                    return;
                } else if (name.length() > 15) {
                    AnimUtils.ShakeAnimation(context, et_username);
                    CommonUtils.ShowToast(context, "Cannot longer than 15 characters!");
                    return;
                } else {
                    boolean result = dbDao.addRecord(name, score);
                    notifyAddRecord(name, result);
                }
                dialog.cancel();
                restartGame();
            }
        });
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                restartGame();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    /**
     * restart game
     */
    private void restartGame() {
        resumeConfig();
        prepareGame();
        initGame();
        rvBoard.scheduleLayoutAnimation();
    }

    /**
     * notify record has been added to database
     *
     * @param name
     * @param result
     */
    private void notifyAddRecord(String name, boolean result) {
        if (result) {
            CommonUtils.NotifyMessageShort(getCurrentFocus(), "Add " + name + " Success!", "", null);
        } else {
            CommonUtils.NotifyMessageShort(getCurrentFocus(), "Add " + name + " Failed!", "", null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRank:
                startActivity(new Intent(MainActivity.this, RankActivity.class));
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        exitByDoubleClicked();
    }

    /**
     * double click back button within 3s will exit the game
     */
    private void exitByDoubleClicked() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            finish();
        } else {
            firstTime = System.currentTimeMillis();
            CommonUtils.ShowToast(this, getResources().getString(R.string.alert_exit));
        }
    }
}
