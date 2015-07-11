package com.gameshare.luisman.gameshareapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by LuisMan on 7/9/2015.
 */
public class PublicGameAdapter extends ArrayAdapter<DummyUserGame> {

    protected ImageView imageView;
    protected TextView systemTitleTv;
    protected TextView dateCreatedTv;
    protected TextView titleTv;
    protected LinearLayout sellLayout;
    protected LinearLayout shareLayout;
    protected LinearLayout tradeLayout;
    public DummyUserGame userGame;
    ArrayList<DummyUserGame> data;
    Context context;
    int layout;

    public PublicGameAdapter(Context context, int resource, ArrayList<DummyUserGame> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.layout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.userGame = data.get(position);

        View rowView = inflater.inflate(R.layout.public_game_layout, parent, false);

        imageView = (ImageView) rowView.findViewById(R.id.imageView);
        systemTitleTv = (TextView) rowView.findViewById(R.id.game_system);
        dateCreatedTv = (TextView) rowView.findViewById(R.id.date);
        sellLayout = (LinearLayout) rowView.findViewById(R.id.sell_layout);
        titleTv = (TextView) rowView.findViewById(R.id.game_title);

        sellLayout.setVisibility(View.INVISIBLE);

        shareLayout = (LinearLayout) rowView.findViewById(R.id.share_layout);

        shareLayout.setVisibility(View.INVISIBLE);

        tradeLayout = (LinearLayout) rowView.findViewById(R.id.trade_layout);

        tradeLayout.setVisibility(View.INVISIBLE);

        new AsyncUploadImage(imageView, userGame.getImageUrl()).execute();

        systemTitleTv.setText(userGame.getSystem());
        dateCreatedTv.setText(userGame.getDate());
        titleTv.setText(userGame.getTitle());

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

        return rowView;

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
