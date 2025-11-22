package com.example.firstjavaandroidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);

        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString("result_text");
            result.setText(savedText);
        }

        int[] numberButtons = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        View.OnClickListener numberListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                String current = result.getText().toString();
                String next = b.getText().toString();
                if (current.equals("0")) {
                    result.setText(next);
                } else {
                    result.setText(current + next);
                }
            }
        };

        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(numberListener);
        }


        int[] opButtons = {
                R.id.btnPlus, R.id.btnMinus, R.id.btnAC, R.id.btnDel, R.id.btnDot,
                R.id.btnDuoZero, R.id.btnPercentage, R.id.btnEqual, R.id.btnC,
                R.id.btnX
        };

        View.OnClickListener operatorListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                result.setText(result.getText().toString() + " " + b.getText().toString() + " ");
            }
        };

        for (int id : opButtons) {
            findViewById(id).setOnClickListener(operatorListener);
        }

        findViewById(R.id.btnAC).setOnClickListener(v -> result.setText("0"));

        findViewById(R.id.btnC).setOnClickListener(v -> {
            String current = result.getText().toString();

            if (current.length() > 1) {
                result.setText(current.substring(0, current.length() - 1));
            } else {
                result.setText("0");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("result_text", result.getText().toString());
    }

}
