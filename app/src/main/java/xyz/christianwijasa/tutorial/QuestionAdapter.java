package xyz.christianwijasa.tutorial;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Richard Firdaus on 06/12/2016.
 */

public class QuestionAdapter extends BaseAdapter {

    Context context;
    Integer jumlah_soal;
    LayoutInflater inflater;
    public static ArrayList<String> selectedAnswers;


    public QuestionAdapter(Context applicationContext, Integer jumlah_soal){
        this.context = context;
        this.jumlah_soal = jumlah_soal;
        //initialize arraylist and add static string for all the questions
        selectedAnswers = new ArrayList<>();
        for(int i = 0;i<jumlah_soal; i++){
            selectedAnswers.add("Not Attempted");
        }

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount(){
        return jumlah_soal;
    }

    @Override
    public Object getItem(int i){
        return null;
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.list_item, null);
        //get the reference of TextView and Button's

        TextView question = (TextView) view.findViewById(R.id.soal);
        RadioButton jawaban_1 = (RadioButton) view.findViewById(R.id.jawaban_1);
        RadioButton jawaban_2 = (RadioButton) view.findViewById(R.id.jawaban_2);
        RadioButton jawaban_3 = (RadioButton) view.findViewById(R.id.jawaban_3);

        jawaban_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) selectedAnswers.set(i,"A");
                Log.d("Masuk ",selectedAnswers.toString());
            }
        });

        jawaban_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) selectedAnswers.set(i,"B");
            }
        });

        jawaban_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) selectedAnswers.set(i,"C");
            }
        });

//        question.setText(questionList);
        return view;


    }
}
