package com.lannasoftware.somehelp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lannasoftware.somehelp.Helper.CEnum;
import com.lannasoftware.somehelp.R;

public class CreateAdvertisementChildFragment1 extends Fragment implements View.OnClickListener {

    String sAnnonceType;

    //TextView txt_intro;
    EditText mEdit_contenu_annonce;

    EditText mEdit_hashtag;
    Spannable mspanable;
    int hashTagIsComing = 0;

    private OnFragmentInteractionListener mListener;

    public CreateAdvertisementChildFragment1() {
        // Required empty public constructor
    }

    public static CreateAdvertisementChildFragment1 newInstance(String param1, String param2) {
        CreateAdvertisementChildFragment1 fragment = new CreateAdvertisementChildFragment1();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if (getArguments() != null) {
            sAnnonceType = args.getString("sAnnonceType", null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_child_fragment1_create_advertisement, container, false);

        mEdit_contenu_annonce = (EditText) view.findViewById(R.id.edit_contenu_annonce);
        mEdit_hashtag = (EditText) view.findViewById(R.id.edit_hastag);
        mEdit_hashtag.setText("#");
        //txt_intro = (TextView) view.findViewById(R.id.txt_intro);

        if(!sAnnonceType.equals(CEnum.AnnonceType.None.name()))
        {
            if(sAnnonceType.equals(CEnum.AnnonceType.DemanderService.name()))
            {
                //txt_intro.setText("Vous cherchez quelqu'un pour poser vos nouvelles lampes, vous aider à déménager ou encore faire votre ménage ? Indiquez à la communauté ce dont vous avez besoin !");
                mEdit_contenu_annonce.setHint("Dites-en plus sur ce dont vous avez besoin ..");
            }
            else if(sAnnonceType.equals(CEnum.AnnonceType.ProposerService.name()))
            {
                //txt_intro.setText("Vous savez changer les pneus d'une voiture ou remplir une déclaration d'impôts ? Offrez vos services au tarif que vous souhaitez !");
                mEdit_contenu_annonce.setHint("Dites-en plus sur ce que vous pouvez faire ..");
            }
            else if(sAnnonceType.equals(CEnum.AnnonceType.VendreArticle.name()))
            {
                //txt_intro.setText("Des vêtements que vous ne mettez plus ou une télévsion à changer.. Vendez ce que vous souhaitez !");
                mEdit_contenu_annonce.setHint("Dites-en plus à propos de vos articles ..");
            }
        }

        mspanable = mEdit_hashtag.getText();

        mEdit_hashtag.addTextChangedListener(new TextWatcher() {

            boolean bSpace = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //if (mEdit_hashtag.length() == 0) {
                //    mEdit_hashtag.setText("#");
                //}

                String startChar = null;

                try{
                    startChar = Character.toString(s.charAt(start));
                    Log.i(getClass().getSimpleName(), "CHARACTER OF NEW WORD: " + startChar);
                }
                catch(Exception ex){
                    startChar = "";
                }

               /* if(startChar.equals(" "))
                    bSpace = true;
                else bSpace = false;*/

                if (startChar.equals("#")) {
                    changeTheColor(s.toString().substring(start), start, start + count);
                    hashTagIsComing++;
                }

                if(startChar.equals(" ")){
                    hashTagIsComing = 0;
                }

                if(hashTagIsComing != 0) {
                    changeTheColor(s.toString().substring(start), start, start + count);
                    hashTagIsComing++;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                /*if (bSpace) {
                    text.append(" #");
                }*/
                if (text.charAt(text.length() -1) == ' ')
                    text.append('#');
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    private void changeTheColor(String s, int start, int end) {
        mspanable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String uri);
    }
}
