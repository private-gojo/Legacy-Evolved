package com.csv.vpn.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csv.vpn.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONObject;
import android.widget.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.graphics.*;

public class SpinnerAdapter extends ArrayAdapter<JSONObject> {

    private int spinner_id;

    public SpinnerAdapter(Context context, int spinner_id, ArrayList<JSONObject> list) {
        super(context, R.layout.spinner_item, list);
        this.spinner_id = spinner_id;
    }

    @Override
    public JSONObject getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return view(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return view(position, convertView, parent);
    }

    private View view(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item, parent, false);
        TextView item = v.findViewById(R.id.itemName);
        TextView extra = v.findViewById(R.id.textExtra);
        TextView info = v.findViewById(R.id.info);
        ImageView flag = v.findViewById(R.id.itemImage);
        LinearLayout ok = v.findViewById(R.id.ok12);

        try {   

            ////Annimation Effect////
            Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.hsvpnpro);
            item.setAnimation(slideUp);
            item.startAnimation(slideUp);
            ////End////

            item.setText(getItem(position).getString("Name"));
            //   info.setText(getItem(position).getString("Info"));
            if (spinner_id == R.id.serverSpinner) {
                getServerIcon(position, flag, extra, info);
                ok.setVisibility(View.VISIBLE);
            } else if (spinner_id == R.id.payloadSpinner) {
                getPayloadIcon(position, flag, extra, info);
                ok.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void getServerIcon(int position, ImageView flag, TextView extra, TextView info) throws Exception {

        ////Annimation Effect////
        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.hsvpnpro);
        flag.setAnimation(slideUp);
        flag.startAnimation(slideUp);
        extra.setAnimation(slideUp);
        extra.startAnimation(slideUp);
        ////End////

        String uppercase = getItem(position).getString("FLAG").toUpperCase();     
        ///VIP Start///
        if (uppercase.contains("M1")){
            flag.setImageResource(R.drawable.mm);
            extra.setText("VIP");

        }else if(uppercase.contains("S1")){
            flag.setImageResource(R.drawable.sg);
            extra.setText("VIP");		
        }else if(uppercase.contains("A1")){
            flag.setImageResource(R.drawable.au);
            extra.setText("VIP");	
        }else if(uppercase.contains("B1")){
            flag.setImageResource(R.drawable.br);
            extra.setText("VIP");
        }else if(uppercase.contains("C1")){
            flag.setImageResource(R.drawable.ca);
            extra.setText("VIP");
        }else if(uppercase.contains("D1")){
            flag.setImageResource(R.drawable.de);
            extra.setText("VIP");
        }else if(uppercase.contains("F1")){
            flag.setImageResource(R.drawable.fr);
            extra.setText("VIP");
        }else if(uppercase.contains("H1")){
            flag.setImageResource(R.drawable.hk);
            extra.setText("VIP");
        }else if(uppercase.contains("I1")){
            flag.setImageResource(R.drawable.ie);
            extra.setText("VIP");
        }else if(uppercase.contains("J1")){
            flag.setImageResource(R.drawable.jp);
            extra.setText("VIP");
        }else if(uppercase.contains("K1")){
            flag.setImageResource(R.drawable.kr);
            extra.setText("VIP");
        }else if(uppercase.contains("L1")){
            flag.setImageResource(R.drawable.in);
            extra.setText("VIP");
        }else if(uppercase.contains("T1")){
            flag.setImageResource(R.drawable.th);
            extra.setText("VIP");
        }else if(uppercase.contains("U1")){
            flag.setImageResource(R.drawable.us);
            extra.setText("VIP");
            ///VIP END//

        }else if(uppercase.contains("MM")){
            flag.setImageResource(R.drawable.mm);
            extra.setText("FREE");
        }else if(uppercase.contains("SG")){
            flag.setImageResource(R.drawable.sg);
            extra.setText("FREE");
        }else if(uppercase.contains("AU")){
            flag.setImageResource(R.drawable.au);
            extra.setText("FREE");
        }else if(uppercase.contains("BR")){
            flag.setImageResource(R.drawable.br);
            extra.setText("FREE");
        }else if(uppercase.contains("CA")){
            flag.setImageResource(R.drawable.ca);
            extra.setText("FREE");
        }else if(uppercase.contains("DE")){
            flag.setImageResource(R.drawable.de);
            extra.setText("FREE");
        }else if(uppercase.contains("FR")){
            flag.setImageResource(R.drawable.fr);
            extra.setText("FREE");
        }else if(uppercase.contains("HK")){
            flag.setImageResource(R.drawable.hk);
            extra.setText("FREE");
        }else if(uppercase.contains("IE")){
            flag.setImageResource(R.drawable.ie);
            extra.setText("FREE");
        }else if(uppercase.contains("JP")){
            flag.setImageResource(R.drawable.jp);
            extra.setText("FREE");
        }else if(uppercase.contains("KR")){
            flag.setImageResource(R.drawable.kr);
            extra.setText("FREE");
        }else if(uppercase.contains("IN")){
            flag.setImageResource(R.drawable.in);
            extra.setText("FREE");
        }else if(uppercase.contains("TH")){
            flag.setImageResource(R.drawable.th);
            extra.setText("FREE");
        }else if(uppercase.contains("US")){
            flag.setImageResource(R.drawable.us);
            extra.setText("FREE");
        }else if(uppercase.contains("UK")){
            flag.setImageResource(R.drawable.uk);
            extra.setText("FREE");
        }else if(uppercase.contains("UP")){
            flag.setImageResource(R.drawable.hsvpn_error);
            extra.setText("Update");
            ///FREE END///
        }else{

            InputStream inputStream = getContext().getAssets().open("flags/" + getItem(position).getString("FLAG"));
            flag.setImageDrawable(Drawable.createFromStream(inputStream, getItem(position).getString("FLAG"))); 
            if (inputStream != null) {
                inputStream.close();
            }
        }

        String sInfo = getItem(position).getString("sInfo");

        info.setText(sInfo);

        ////Annimation Effect////
        info.setAnimation(slideUp);
        info.startAnimation(slideUp);
        ////End////
    }

    private void getPayloadIcon(int position, ImageView flag, TextView extra, TextView info) throws Exception {

        String flagname = getItem(position).getString("Name").toLowerCase();
        info.setText(getItem(position).getString("Info"));
        String flagname2 = getItem(position).getString("Info").toLowerCase();

        ////Annimation Effect////
        Animation slideUp = AnimationUtils.loadAnimation(getContext(), R.anim.hsvpnpro);
        info.setAnimation(slideUp);
        info.startAnimation(slideUp);  
        //HlaMyoMin
        extra.setAnimation(slideUp);
        extra.startAnimation(slideUp); 
        //HS VPN @Developed
        flag.setAnimation(slideUp);
        flag.startAnimation(slideUp);
        ////End////

        boolean sslType = getItem(position).getBoolean("isSSL");
        if (sslType) {
            extra.setText("SSL/TLS");
        } else{
            extra.setText("SSH/WS");
        }
        if (flagname2.contains("direct")) {
            extra.setText("Direct");
        } else if (flagname2.contains("update")) {
            extra.setText("Update");

        }

        if (flagname.contains("myanmar")) {
            flag.setImageResource(R.drawable.ic_myanmar);
        } else if (flagname.contains("mectel")) {
            flag.setImageResource(R.drawable.ic_mectel);
        } else if (flagname.contains("direct")) {
            flag.setImageResource(R.drawable.hsvpn_logo);
        } else if (flagname.contains("ooredoo")) {
            flag.setImageResource(R.drawable.ic_ooredoo);
        } else if (flagname.contains("telenor")) {
            flag.setImageResource(R.drawable.ic_telenor);
        } else if (flagname.contains("mpt")) {
            flag.setImageResource(R.drawable.ic_mpt);
        }else if(flagname.contains("mytel")) {
            flag.setImageResource(R.drawable.ic_mytel);
		}else if(flagname.contains("dtac")) {
            flag.setImageResource(R.drawable.ic_dtac);
        }else if(flagname.contains("truemove")) {
            flag.setImageResource(R.drawable.ic_truemove);
		}else if(flagname.contains("tiktok")) {
            flag.setImageResource(R.drawable.ic_tiktok);
		}else if(flagname.contains("youtube")) {
            flag.setImageResource(R.drawable.ic_youtube);
		}else if(flagname.contains("facebook")) {
            flag.setImageResource(R.drawable.ic_facebook);
		}else if(flagname.contains("pubg")) {
            flag.setImageResource(R.drawable.ic_pubg);
        }else if(flagname.contains("mobile legend")) {
            flag.setImageResource(R.drawable.ic_ml);
        }else if(flagname.contains("netflix")) {
            flag.setImageResource(R.drawable.ic_netflix);
        }else if(flagname.contains("torrent")) {
            flag.setImageResource(R.drawable.ic_torrent);
        }else if(flagname.contains("gaming")) {
            flag.setImageResource(R.drawable.ic_game);
        }else if(flagname.contains("voip")) {
            flag.setImageResource(R.drawable.voip);
        }else if(flagname.contains("update server")) {
            flag.setImageResource(R.drawable.hsvpn_error);

        }
    }

}
