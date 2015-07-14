package com.gameshare.luisman.gameshareapp;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;

/**
 * Created by LuisMan on 7/13/2015.
 */
public class MessageCard extends Card {

    protected TextView messageTv;
    public Message message;

    public MessageCard(final Context context, int innerLayout, final Message message) {
        super(context, innerLayout);

        this.message = message;
        CardHeader header = new CardHeader(context);
        header.setTitle(message.getUsername() + " on " + message.getDate());


        setSwipeable(true);
        setId(message.getMessage());

        addCardHeader(header);

        addPartialOnClickListener(Card.CLICK_LISTENER_HEADER_VIEW, new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                MarkAsRead((MessageCard) card, view);
            }
        });

        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {

                MarkAsRead((MessageCard) card, view);

                boolean wrapInScrollView = true;

                new MaterialDialog.Builder(context)
                        .title(context.getString(R.string.reply))
                        .customView(R.layout.reply_message_layout, wrapInScrollView)
                        .positiveText(context.getString(R.string.ok))
                        .negativeText(context.getString(R.string.cancel))
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                EditText usernameTv = (EditText) dialog.findViewById(R.id.username);
                                EditText messageTv = (EditText) dialog.findViewById(R.id.message);
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                            }
                        })
                        .show();
            }
        });

        setOnSwipeListener(new Card.OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {

            }
        });


        setOnUndoSwipeListListener(new OnUndoSwipeListListener() {
            @Override
            public void onUndoSwipe(Card card) {

            }
        });
    }

    private void MarkAsRead(MessageCard card, View view) {
        messageTv = (TextView) view.findViewById(R.id.message);
        card.message.setIsRead(true);

        messageTv.setTextColor(Color.parseColor("#33999999"));

        notifyDataSetChanged();
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        // TODO Auto-generated method stub
        messageTv = (TextView) parent.findViewById(R.id.message);
        messageTv.setText(message.getMessage());

        if(message.isRead())
        {
            messageTv.setTextColor(Color.parseColor("#33999999"));
        }
    }

}
