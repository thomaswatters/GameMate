package com.gameshare.luisman.gameshareapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

/**
 * Created by LuisMan on 7/2/2015.
 */
public class UserGameCard extends Card {

    protected ImageView imageView;
    protected TextView systemTitleTv;
    protected TextView dateCreatedTv;
    protected LinearLayout sellLayout;
    protected LinearLayout shareLayout;
    protected LinearLayout tradeLayout;
    public DummyUserGame userGame;

    public UserGameCard(final Context context, int innerLayout, DummyUserGame userGame) {
        super(context, innerLayout);

        this.userGame = userGame;
        CardHeader header = new CardHeader(context);
        header.setTitle(userGame.getTitle());

        header.setButtonOverflowVisible(true);
        header.setPopupMenuListener(new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                if(item.getTitle() == "Edit")
                {
                    UserGameCard currentGameCard = (UserGameCard) card;
                    int position = ViewUserGamesActivity.cards.indexOf(currentGameCard);

                    DummyUserGame currentUserGame = currentGameCard.userGame;
                    Intent intent = new Intent(context, AddUpdateGameActivity.class );
                    intent.putExtra("editGame", currentUserGame);
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
                if(item.getTitle() == "Delete")
                {
                    UserGameCard currentGameCard = (UserGameCard) card;
                    ViewUserGamesActivity.showDeleteConfirmation(context, currentGameCard);
                }
            }
        });

        header.setPopupMenuPrepareListener(new CardHeader.OnPrepareCardHeaderPopupMenuListener() {
            @Override
            public boolean onPreparePopupMenu(BaseCard card, PopupMenu popupMenu) {
                popupMenu.getMenu().add("Edit");
                popupMenu.getMenu().add("Delete");
                return true;
            }
        });

        addCardHeader(header);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        // TODO Auto-generated method stub
        imageView = (ImageView) parent.findViewById(R.id.imageView);
        systemTitleTv = (TextView) parent.findViewById(R.id.game_system);
        dateCreatedTv = (TextView) parent.findViewById(R.id.date);
        sellLayout = (LinearLayout) parent.findViewById(R.id.sell_layout);

        sellLayout.setVisibility(View.INVISIBLE);

        shareLayout = (LinearLayout) parent.findViewById(R.id.share_layout);

        shareLayout.setVisibility(View.INVISIBLE);

        tradeLayout = (LinearLayout) parent.findViewById(R.id.trade_layout);

        tradeLayout.setVisibility(View.INVISIBLE);

        new AsyncUploadImage(imageView, userGame.getImageUrl()).execute();

        systemTitleTv.setText(userGame.getSystem());

        dateCreatedTv.setText(userGame.getDate());

        Iterator it = userGame.getFlags().entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            if(pair.getKey().equals("Share") && (Boolean) pair.getValue())
            {
                shareLayout.setVisibility(View.VISIBLE);
            }

            if(pair.getKey().equals("Sell") && (Boolean) pair.getValue())
            {
                sellLayout.setVisibility(View.VISIBLE);
            }

            if(pair.getKey().equals("Trade") && (Boolean) pair.getValue())
            {
                tradeLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public class AsyncUploadImage extends AsyncTask<Object, Object, Object> {
        ImageView iv;
        private HttpURLConnection connection;
        private InputStream is;
        private Bitmap bitmap;
        private String url;

        public AsyncUploadImage(ImageView mImageView, String imageUrl) {
            iv = mImageView;
            url = imageUrl;
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                String urlStr = url;
                URL url = new URL(urlStr);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);

                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            if (null != result)
            {
                iv.setImageBitmap((Bitmap) result);
            }
        }
    }
}
