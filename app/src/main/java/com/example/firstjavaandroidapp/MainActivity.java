package com.example.firstjavaandroidapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private StringBuilder currentExpression = new StringBuilder();
    private StringBuilder currentNumber = new StringBuilder();
    private boolean lastInputIsOperator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);

        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString("result_text");
            currentExpression.append(savedText);
            result.setText(savedText);
        }

        int[] numberButtons = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
                R.id.btnDuoZero
        };


        for (int id : numberButtons) {
            findViewById(id).setOnClickListener(v -> {
                Button b = (Button) v;
                String input = b.getText().toString();

                if (input.equals("00")) {
                    if (currentNumber.length() == 0) {
                        currentNumber.append("0");
                    } else {
                        currentNumber.append("00");
                    }
                } else {
                    currentNumber.append(input);
                }

                lastInputIsOperator = false;
                updateDisplay();
            });
        }


        int[] opButtons = {
                R.id.btnPlus, R.id.btnMinus, R.id.btnX, R.id.btnDel, R.id.btnPercentage
        };

        for (int id : opButtons) {
            findViewById(id).setOnClickListener(v -> {
                Button b = (Button) v;
                String op = b.getText().toString();

                if (op.equals("x")) op = "*";
                if (op.equals("÷")) op = "/";

                if (currentNumber.length() > 0) {
                    currentExpression.append(currentNumber.toString());
                    currentNumber.setLength(0);
                }

                if (!lastInputIsOperator) {
                    currentExpression.append(" ").append(op).append(" ");
                    lastInputIsOperator = true;
                }

                updateDisplay();
            });
        }


        findViewById(R.id.btnDot).setOnClickListener(v -> {
            if (!currentNumber.toString().contains(".")) {
                if (currentNumber.length() == 0) currentNumber.append("0");
                currentNumber.append(".");
                updateDisplay();
            }
        });


        findViewById(R.id.btnPercentage).setOnClickListener(v -> {
            if (currentNumber.length() > 0 && currentExpression.length() > 0) {
                String expr = currentExpression.toString().trim();
                String[] tokens = expr.split(" ");
                double base = Double.parseDouble(tokens[tokens.length - 1]);
                double percent = Double.parseDouble(currentNumber.toString());
                double value = base * percent / 100.0;

                currentNumber.setLength(0);
                currentNumber.append(value);
                updateDisplay();
            } else if (currentNumber.length() > 0) {
                double value = Double.parseDouble(currentNumber.toString()) / 100.0;
                currentNumber.setLength(0);
                currentNumber.append(value);
                updateDisplay();
            }
        });


        findViewById(R.id.btnAC).setOnClickListener(v -> {
            currentExpression.setLength(0);
            currentNumber.setLength(0);
            lastInputIsOperator = false;
            updateDisplay();
        });


        findViewById(R.id.btnC).setOnClickListener(v -> {
            if (currentNumber.length() > 0) {
                currentNumber.setLength(currentNumber.length() - 1);
            } else if (currentExpression.length() > 0) {
                currentExpression.setLength(currentExpression.length() - 1);
            }
            updateDisplay();
        });


        findViewById(R.id.btnEqual).setOnClickListener(v -> {
            if (currentNumber.length() > 0) {
                currentExpression.append(currentNumber.toString());
                currentNumber.setLength(0);
            }

            String expr = currentExpression.toString();
            expr = expr.replace("x", "*").replace("÷", "/");

            double res = evaluate(expr);

            if (res == (long) res) {
                currentExpression.setLength(0);
                currentExpression.append((long) res);
                result.setText(String.valueOf((long) res));
            } else {
                currentExpression.setLength(0);
                currentExpression.append(res);
                result.setText(String.valueOf(res));
            }

            lastInputIsOperator = false;
        });
    }

    private void updateDisplay() {
        result.setText(currentExpression.toString() + currentNumber.toString());
    }

    private double evaluate(String expr) {
        expr = expr.replace("x", "*").replace("÷", "/");

        List<String> tokens = new ArrayList<>(Arrays.asList(expr.trim().split(" ")));
        for (int i = 0; i < tokens.size(); i++) {
            String op = tokens.get(i);
            if (op.equals("*") || op.equals("/")) {
                double a = Double.parseDouble(tokens.get(i - 1));
                double b = Double.parseDouble(tokens.get(i + 1));
                double res = op.equals("*") ? a * b : a / b;
                tokens.set(i - 1, String.valueOf(res));
                tokens.remove(i);
                tokens.remove(i);
                i--;
            }
        }

        double result = Double.parseDouble(tokens.get(0));
        for (int i = 1; i < tokens.size(); i += 2) {
            String op = tokens.get(i);
            double next = Double.parseDouble(tokens.get(i + 1));
            if (op.equals("+")) result += next;
            if (op.equals("-")) result -= next;
        }

        return result;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("result_text", currentExpression.toString() + currentNumber.toString());
    }
}
