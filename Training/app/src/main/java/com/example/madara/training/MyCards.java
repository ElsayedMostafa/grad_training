package com.example.madara.training;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.madara.training.adapters.CardAdapter;
import com.example.madara.training.fragments.GetPassword;
import com.example.madara.training.models.Card;
import com.example.madara.training.models.MainResponse;
import com.example.madara.training.models.Rfid;
import com.example.madara.training.utils.Session;
import com.example.madara.training.webservices.WebService;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCards extends AppCompatActivity {
    private final String TAG = "MyCards";
    private Call<MainResponse> mBindCardCall;
    @BindView(R.id.btn_bindcard)
    FloatingActionButton _btn_bind;
    @BindView(R.id.recyclerView_card)
    RecyclerView _recyclerCardView;
    public static final int REQUEST_CODE = 100;
    public static final int PERMISSION_REQUEST = 200;
    private BroadcastReceiver broadcastReceiver;
    private String mBarcode;
    private String mUserPassword;
    private CardAdapter cardAdapter;
    private List<Rfid> cards;
    private Call<List<Rfid>> getCardsCall;
    private  int position;
    ItemTouchHelper.SimpleCallback swipChatRoomCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // get adapter position
             position = viewHolder.getAdapterPosition();
            // get chat room id from chat rooms list depedning on position
            //int cardID = Integer.parseInt(cards.get(position).getId());
            GetPassword getPassword = new GetPassword();
            Bundle args = new Bundle();
            args.putInt("num", 0);
            getPassword.setArguments(args);
            getPassword.show(getFragmentManager(),"GetPassword");
            //cards.remove(position);
            // notify adapter that chat room deleted so its delete it
            //cardAdapter.notifyItemRemoved(position);

            // start Retrofit call to delete chat room
//            WebService.getInstance().getApi().deleteCard(cardID).enqueue(new Callback<MainResponse>() {
//                @Override
//                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
//                    if (response.body().status == 1) {
//                        // toast message result
//                        Toast.makeText(MyCards.this, response.body().message, Toast.LENGTH_SHORT).show();
//                        // delete message from local chat room list which showed on adapter  now
//                        cards.remove(position);
//                        // notify adapter that chat room deleted so its delete it
//                        cardAdapter.notifyItemRemoved(position);
//
//                    } else {
//                        // toast message if status 0 it will be error
//                        Toast.makeText(MyCards.this, response.body().message, Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<MainResponse> call, Throwable t) {
//                    // toast message of fail
//                    Toast.makeText(MyCards.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//
//
//                }
//            });

        }


        // to color the background of swiped item red color
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            // if swiping now
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                // get item that swiped
                View itemView = viewHolder.itemView;
                // create new pain
                Paint p = new Paint();
                // if swiping to left dx will < 0 so we do what we want
                if (dX >= 0) {
                    //Log.e(TAG,"yes");
                    // set color for paint red color
                    p.setColor(Color.parseColor("#EBECED"));
                    // draw rectangle depending on the item view ends
                    c.drawRect((float) itemView.getLeft() , (float) itemView.getTop(),
                            (float) itemView.getLeft()+dX, (float) itemView.getBottom(), p);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cards);
        // show user cards
        ButterKnife.bind(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("onReceive","Logout in progress");
                //At this point you should start the login activity and finish this one
                finish();
            }
        };
        registerReceiver(broadcastReceiver, intentFilter);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
//        _recyclerCardView.setLayoutManager(new LinearLayoutManager(this));
        cards = new ArrayList<Rfid>();
        cards.add(new Rfid("126578963214532"));
        cards.add(new Rfid("336698587544221"));
        cards.add(new Rfid("569874123655879"));
        _recyclerCardView.setLayoutManager(new LinearLayoutManager(this));
        cardAdapter = new CardAdapter(cards,MyCards.this);
        _recyclerCardView.setAdapter(cardAdapter);
        //getCards();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipChatRoomCallBack);
        itemTouchHelper.attachToRecyclerView(_recyclerCardView);


        _btn_bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCards.this, QRScanner.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if(data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
//                _qrresult.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        _qrresult.setText(barcode.displayValue);
//                    }
//                });
                //Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                mBarcode = barcode.displayValue;
                GetPassword getPassword = new GetPassword();
                Bundle args = new Bundle();
                args.putInt("num", 1);
                getPassword.setArguments(args);
                getPassword.show(getFragmentManager(),"GetPassword");

            }
        }
    }
    private void bindNewCard(String password){
        if(password.isEmpty()){
            cardAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Empty Password !",Toast.LENGTH_SHORT).show();
            return;

        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(MyCards.this);
            mUserPassword = password;
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Binding...");
            progressDialog.show();
            Toast.makeText(this, mUserPassword + " " + mBarcode, Toast.LENGTH_LONG).show();
            final Card card = new Card();
            card.id = Session.getInstance().getUser().id;
            card.password = mUserPassword;
            card.qrcode = mBarcode;
            mBindCardCall = WebService.getInstance().getApi().bindCard(card);
            mBindCardCall.enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    try{
                        if (response.body().status == 0) {
                            Toast.makeText(MyCards.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        } else if (response.body().status == 1) {
                            Toast.makeText(MyCards.this, response.body().message, Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                            getCards();

                        } else {
                            progressDialog.cancel();
                            Toast.makeText(MyCards.this, response.body().message, Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (Exception e){
                        Toast.makeText(MyCards.this, "Failed", Toast.LENGTH_SHORT).show();
                        progressDialog.cancel();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    progressDialog.cancel();
                    Toast.makeText(getBaseContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
                }
            });


        }
    }
    private void getCards(){
        final ProgressDialog progressDialog = new ProgressDialog(MyCards.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Geting cards...");
        progressDialog.show();
        getCardsCall = WebService.getInstance().getApi().getCards(Session.getInstance().getUser().id);
        //Log.e(TAG,getCardsCall.request().body().toString());
        getCardsCall.enqueue(new Callback<List<Rfid>>() {
            @Override
            public void onResponse(Call<List<Rfid>> call, Response<List<Rfid>> response) {
                //Log.e(TAG,response.body().toString());
                cards = response.body();
                //Log.e(TAG,cards.toString());
                Log.e(TAG,cards.toString());
                cardAdapter = new CardAdapter(cards,MyCards.this);
                _recyclerCardView.setAdapter(cardAdapter);
                progressDialog.cancel();
            }

            @Override
            public void onFailure(Call<List<Rfid>> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Check Network Connection", Toast.LENGTH_LONG).show();
                progressDialog.cancel();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mBindCardCall.cancel();
        unregisterReceiver(broadcastReceiver);
    }
public void sendPassword(String pass ,boolean c, boolean cancel){
        if(cancel){
            cardAdapter.notifyDataSetChanged();
        }
        else {
            if (c) {
                bindNewCard(pass);
            } else {
                removeCard(pass);
            }
        }

}
private void removeCard(String password){
    if(password.isEmpty()){
        Toast.makeText(this,"Empty Password !",Toast.LENGTH_SHORT).show();
        cardAdapter.notifyDataSetChanged();

        //_recyclerCardView.setAdapter(cardAdapter);
        return;
    }
    else {
        final ProgressDialog progressDialog = new ProgressDialog(MyCards.this);
        mUserPassword = password;
        final Card card = new Card();
        card.id = Session.getInstance().getUser().id;
        card.password = mUserPassword;
        card.qrcode = cards.get(position).getId();
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Unbinding...");
        progressDialog.show();
        //cards.remove(position);
        //cardAdapter.notifyItemRemoved(position);
        //Toast.makeText(MyCards.this, "remove"+card.qrcode, Toast.LENGTH_LONG).show();

        // start Retrofit call to delete chat room
            WebService.getInstance().getApi().deleteCard(card).enqueue(new Callback<MainResponse>() {
                @Override
                public void onResponse(Call<MainResponse> call, Response<MainResponse> response) {
                    if (response.body().status == 1) {
                        // toast message result
                        progressDialog.cancel();
                        Toast.makeText(MyCards.this, response.body().message, Toast.LENGTH_SHORT).show();
                        // delete message from local chat room list which showed on adapter  now
                        cards.remove(position);
                        // notify adapter that chat room deleted so its delete it
                        cardAdapter.notifyItemRemoved(position);

                    } else {
                        // toast message if status 0 it will be error
                        progressDialog.cancel();
                        cardAdapter.notifyDataSetChanged();
                        Toast.makeText(MyCards.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MainResponse> call, Throwable t) {
                    // toast message of fail
                    progressDialog.cancel();
                    cardAdapter.notifyDataSetChanged();
                    Toast.makeText(MyCards.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                }
            });
    }
    }
}
