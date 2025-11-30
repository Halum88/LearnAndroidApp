package com.example.firstjavaandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.firstjavaandroidapp.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private StringBuilder currentExpression = new StringBuilder();
    private StringBuilder currentNumber = new StringBuilder();
    private boolean lastInputIsOperator = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.nav_about) {
                startActivity(new Intent(MainActivity.this, AboutAPP.class));
                return true;
            }
            return false;
        });


        if (savedInstanceState != null) {
            String savedText = savedInstanceState.getString("result_text");
            currentExpression.append(savedText);
            binding.result.setText(savedText);
        }

        Button[] numberButtons = new Button[]{
                binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
                binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9,
                binding.btnDuoZero
        };


        for (Button btn : numberButtons) {
            btn.setOnClickListener(v -> {
                String input = btn.getText().toString();

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


        Button[] opButtons = new Button[]{
                binding.btnPlus, binding.btnMinus, binding.btnX, binding.btnDel, binding.btnPercentage
        };

        for (Button btn : opButtons) {
            btn.setOnClickListener(v -> {
                String op = btn.getText().toString();

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


        binding.btnDot.setOnClickListener(v -> {
            if (!currentNumber.toString().contains(".")) {
                if (currentNumber.length() == 0) currentNumber.append("0");
                currentNumber.append(".");
                updateDisplay();
            }
        });


        binding.btnPercentage.setOnClickListener(v -> {
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


        binding.btnAC.setOnClickListener(v -> {
            currentExpression.setLength(0);
            currentNumber.setLength(0);
            lastInputIsOperator = false;
            updateDisplay();
        });


        binding.btnC.setOnClickListener(v -> {
            if (currentNumber.length() > 0) {
                currentNumber.setLength(currentNumber.length() - 1);
            } else if (currentExpression.length() > 0) {
                currentExpression.setLength(currentExpression.length() - 1);
            }
            updateDisplay();
        });


        binding.btnEqual.setOnClickListener(v -> {
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
                binding.result.setText(String.valueOf((long) res));
            } else {
                currentExpression.setLength(0);
                currentExpression.append(res);
                binding.result.setText(String.valueOf(res));
            }

            lastInputIsOperator = false;
        });
    }

    private void updateDisplay() {
        binding.result.setText(currentExpression.toString() + currentNumber.toString());
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
