package com.example.hexkey;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class Decode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);
    }

    public static String hexToBin(String s) {
        String temp = new BigInteger(s, 16).toString(2);
        return Strings.padStart(temp, 4, '0');
    }

    public static String binToHex(String s) {
        BigInteger temp = new BigInteger(s, 2);
        return temp.toString(16);
    }

    public String decodeHexWord(String s) {


        //Cut up each word in its characters:
        String[] splitHex = Iterables.toArray(Splitter.fixedLength(1).split(s), String.class);
        System.out.println("SplitBin: " + Arrays.toString(splitHex));

        //Convert each character into bin
        String[] splitBin = {
                hexToBin(splitHex[0]),
                hexToBin(splitHex[1]),
                hexToBin(splitHex[2]),
                hexToBin(splitHex[3])
        };

        System.out.println("Split binaries " + Arrays.toString(splitBin));

        //Reverse each binary:
        String[] reversedBin = {
                new StringBuilder(splitBin[0]).reverse().toString(),
                new StringBuilder(splitBin[1]).reverse().toString(),
                new StringBuilder(splitBin[2]).reverse().toString(),
                new StringBuilder(splitBin[3]).reverse().toString(),
        };

        System.out.println("Reversed binaries: " + Arrays.toString(reversedBin));

        //Permutate the values
        String[] permutatedBin = {
                reversedBin[1],
                reversedBin[0],
                reversedBin[3],
                reversedBin[2]
        };

        System.out.println("Permutated bin: " + Arrays.toString(permutatedBin));

        //Back to hex
        String[] back2Hex = {binToHex(permutatedBin[0]), binToHex(permutatedBin[1]), binToHex(permutatedBin[2]), binToHex(permutatedBin[3])};

        System.out.println("Back to hex: " + Arrays.toString(back2Hex));

        //I need to return back2Hex
        //Back to hex is just the WORD
        return TextUtils.join("", back2Hex).toUpperCase();

    }

    public String[] decodeArray(String string) {

        //uncodedWords will hold the 6 words in the correct order.
        String[] uncodedWords = new String[6];
        char[] charArrayUncoded = string.toCharArray();

        //Run through the array of chars and populate charArrayUncoded.
        for (int i = charArrayUncoded.length - 4, j = 0; i >= 0; i = i - 4, j++) {
            char[] fourCharWord = {
                    charArrayUncoded[i],
                    charArrayUncoded[i + 1],
                    charArrayUncoded[i + 2],
                    charArrayUncoded[i + 3]
            };
            uncodedWords[j] = new String(fourCharWord);
        }

        //Now, uncodedWords is correctly populated.
        //Take each word and do the binary operations.

        String[] decodedKeys = new String[6];

        for (int i = 0; i < 6; i++) {
            System.out.println("Decoding word " + i + ": " + uncodedWords[i]);

            //decodeHexWord returns a STRING - THE WORD DECODED
            decodedKeys[i] = decodeHexWord(uncodedWords[i]);


        }


        System.out.println(charArrayUncoded);

        System.out.println(Arrays.toString(uncodedWords));

        return decodedKeys;


    }

    public void showDecodedKeys(String[] decodedKeys) {

        //So this function will receive a string with the decoded keys
        //and will populate the text views accordingly.
        TextView tv9 = findViewById(R.id.Word9);
        tv9.setText(decodedKeys[5]);

        TextView tv8 = findViewById(R.id.Word8);
        tv8.setText(decodedKeys[4]);

        TextView tv7 = findViewById(R.id.Word7);
        tv7.setText(decodedKeys[3]);

        TextView tv6 = findViewById(R.id.Word6);
        tv6.setText(decodedKeys[2]);

        TextView tv5 = findViewById(R.id.Word5);
        tv5.setText(decodedKeys[1]);

        TextView tv4 = findViewById(R.id.Word4);
        tv4.setText(decodedKeys[0]);


    }


    public void onDecode(View view) {

        //Get the inserted string.

        EditText hexToDecode = findViewById(R.id.insertedHex);
        String insertedHex = hexToDecode.getText().toString();
        String formattedHex = insertedHex.replace(" - ", "");

//        String formattedHex = "2E683C28A13912BCF3BCAC4F";

        //Check if it's hex.
        if (formattedHex.matches("[0-9A-Fa-f]+")) {

            //It's hex, so decode.
            System.out.println("It's hex");


            if (formattedHex.toCharArray().length != 24) {
                Context context = getApplicationContext();
                CharSequence text = "Incomplete key. Insert 24 digits.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            String[] decodedKeys = decodeArray(formattedHex);
            showDecodedKeys(decodedKeys);


        } else {
            //It's not hex, print toast to fix.
            System.out.println("NOT HEX");
            Context context = getApplicationContext();
            CharSequence text = "Invalid input. Use only HEX characters.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }


        //Print out the string
        System.out.println("Formatted Hex: " + formattedHex);

        hideSoftKeyboard(this);


    }


//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
//    }

    public void hideSoftKeyboard(Activity activity) {
        View view = this.getCurrentFocus();
        if (view!=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }
}