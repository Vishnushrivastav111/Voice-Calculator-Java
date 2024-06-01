package com.example.khudvivek;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final int RESULT_SPEECH = 1;
    public TextView resultTv;
    private TextToSpeech textToSpeech;
    TextView solutionTv;
    private String input, output, newOutput;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonC, buttonAC, buttonDot, btnSpeak;
    MaterialButton buttonpower, buttonpercent, buttonfact, buttonsqrt;


    @SuppressLint({"MissingInflatedId", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv = findViewById(R.id.result_tv);
        solutionTv = findViewById(R.id.solution_tv);
        btnSpeak = findViewById(R.id.btnSpeak);
        Button button = findViewById(R.id.button);
        assignId(buttonC, R.id.button_c);
        assignId(buttonDivide, R.id.button_divide);
        assignId(buttonMultiply, R.id.button_multiply);
        assignId(buttonPlus, R.id.button_plus);
        assignId(buttonMinus, R.id.button_minus);
        assignId(buttonpower, R.id.button_power);
        assignId(buttonpercent, R.id.button_percent);
        assignId(buttonfact, R.id.button_fact);
        assignId(buttonsqrt, R.id.button_sqrt);
        assignId(buttonEquals, R.id.button_equals);
        assignId(button0, R.id.button_0);
        assignId(button1, R.id.button_1);
        assignId(button2, R.id.button_2);
        assignId(button3, R.id.button_3);
        assignId(button4, R.id.button_4);
        assignId(button5, R.id.button_5);
        assignId(button6, R.id.button_6);
        assignId(button7, R.id.button_7);
        assignId(button8, R.id.button_8);
        assignId(button9, R.id.button_9);
        assignId(buttonAC, R.id.button_ac);
        assignId(buttonDot, R.id.button_dot);
        btnSpeak.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick (View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        try {
            startActivityForResult(intent, RESULT_SPEECH);
            resultTv.setText("");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Your device doesn't support Speech to Text", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    });
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS){
                    int language = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = resultTv.getText().toString();
                int speech = textToSpeech.speak(string,TextToSpeech.QUEUE_FLUSH,null);
            }
        });
}
        void assignId (MaterialButton btn,int id){
            btn = findViewById(id);
            btn.setOnClickListener(this);
        }
        @Override
        public void onClick (View view){
            MaterialButton button = (MaterialButton) view;
            String data = button.getText().toString();
            switch (data) {
                case "AC":
                    input = null;
                    output = null;
                    solutionTv.setText("");
                    break;
                case "C":
                   clearLastCharacter();
                    break;
                case "*":
                    input += "*";
                    solve();
                    break;
                case "=":
                    solve();
                    break;
                case "%":
                    calculatePercentage();
                    break;
                case "!":
                    if (input != null) {
                        int number = Integer.parseInt(input);
                        int factorial = calculatefactorial(number);
                        solutionTv.setText(String.valueOf(factorial));
                        input = String.valueOf(factorial);
                    }
                    break;
                case "√":
                    if (input != null && !input.isEmpty()) {
                        try {
                            double number = Double.parseDouble(input);
                            double squareRoot = Math.sqrt(number);
                            input = String.valueOf(squareRoot);
                            resultTv.setText(input);
                        } catch (NumberFormatException e) {
                        }
                    }
                    break;
                default:
                    if (input == null) {
                        input = "";
                    }

                    if (data.equals("+") || data.equals("/") || data.equals("-")) {
                        solve();
                    }
                    input += data;
            }
            resultTv.setText(input);
        }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    StringBuilder finalResult = new StringBuilder();
                    for (String spokenText : text) {
                        String[] expression = spokenText.split("(?<=[+\\-*/x])|(?=[+\\-*/x])");
                        double result = Double.parseDouble(expression[0].trim());
                        for (int i = 1; i < expression.length; i += 2) {
                            String operator = expression[i].trim();
                            double nextNum = Double.parseDouble(expression[i + 1].trim());
                            switch (operator) {
                                case "+":
                                    result += nextNum;
                                    break;
                                case "-":
                                    result -= nextNum;
                                    break;
                                case "*":
                                    result *= nextNum;
                                    break;
                                case "x":
                                    result *= nextNum;
                                    break;
                                case "/":
                                    if (nextNum != 0) {
                                        result /= nextNum;
                                    } else {
                                        resultTv.setText("Invalid Expression:Division by zero");
                                        return;
                                    }
                                    break;
                                default:
                                    throw new IllegalArgumentException("Unsupported operator");
                            }
                        }
                        finalResult.append(spokenText).append("=").append(result).append("\n");
                    }
                    resultTv.setText(finalResult.toString());
                }
                break;
        }
    }
    private void solve() {
        String expression1 = input;
        if (input.split("\\+").length == 2) {
            String numbers[] = input.split("\\+");
            try {
                double d = Double.parseDouble(numbers[0]) + Double.parseDouble(numbers[1]);
                output = Double.toString(d);
                newOutput = cutDecimal(output);
                solutionTv.setText(newOutput);
                input = d + "";
            } catch (Exception e) {
                solutionTv.setText(e.getMessage().toString());
            }
        }
        if (input.split("\\*").length == 2) {
            String numbers[] = input.split("\\*");
            try {
                double d = Double.parseDouble(numbers[0]) * Double.parseDouble(numbers[1]);
                output = Double.toString(d);
                newOutput = cutDecimal(output);
                solutionTv.setText(newOutput);
                input = d + "";
            } catch (Exception e) {
                solutionTv.setText(e.getMessage().toString());
            }
        }
        if (input.split("\\/").length == 2) {
            String numbers[] = input.split("\\/");
            try {
                double d = Double.parseDouble(numbers[0]) / Double.parseDouble(numbers[1]);
                output = Double.toString(d);
                newOutput = cutDecimal(output);
                solutionTv.setText(newOutput);
                input = d + "";
            } catch (Exception e) {
                solutionTv.setText(e.getMessage().toString());
            }
        }
        if (input.split("\\-").length == 2) {
            String numbers[] = input.split("\\-");
            try {
                if (Double.parseDouble(numbers[0]) < Double.parseDouble(numbers[1])) {
                    double d = Double.parseDouble(numbers[1]) - Double.parseDouble(numbers[0]);
                    output = Double.toString(d);
                    newOutput = cutDecimal(output);
                    solutionTv.setText("-" + newOutput);
                    input = d + "";
                } else {
                    double d = Double.parseDouble(numbers[0]) - Double.parseDouble(numbers[1]);
                    output = Double.toString(d);
                    newOutput = cutDecimal(output);
                    solutionTv.setText(newOutput);
                    input = d + "";
                }
            } catch (Exception e) {
                solutionTv.setText(e.getMessage().toString());
            }
        }
        if (input.split("\\^").length == 2) {
            String numbers[] = input.split("\\^");
            try {
                double d = Math.pow(Double.parseDouble(numbers[0]), Double.parseDouble(numbers[1]));
                output = Double.toString(d);
                newOutput = cutDecimal(output);
                solutionTv.setText(newOutput);
                input = d + "";
            } catch (Exception e) {
                solutionTv.setText(e.getMessage().toString());
            }
        }
        solutionTv.setText(expression1);
        resultTv.setText(newOutput);
    }

    private void clearLastCharacter() {
        String val = resultTv.getText().toString();
        if (!val.isEmpty()) {
            val = val.substring(0, val.length() - 1);
            resultTv.setText(val);
        }
    }

    private String cutDecimal(String number) {
        if (number.endsWith(".0")) {
            return number.substring(0, number.length() - 2);
        } else {
            return number;
        }
    }

    private int calculatefactorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        int factorial = 1;
        for (int i = 2; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }

    private void calculatePercentage() {
        if (input != null && !input.isEmpty()) {
            try {
                double userInput = Double.parseDouble(input);
                double percentValue = userInput / 100.0;
                solutionTv.setText(String.valueOf(percentValue));
                input = String.valueOf(percentValue);
            } catch (NumberFormatException e) {
                solutionTv.setText("Error: Invalid Input");
            }
        }
    }

    }
