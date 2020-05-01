package com.example.hexkey;

import com.example.hexkey.Decode.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

import org.w3c.dom.Text;

public class Encode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);
    }

    public void hideSoftKeyboard(Activity activity) {
        View view = this.getCurrentFocus();
        if (view!=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }
    }

    public void onEncode(View view) {

        //Let's get the text from the Edit Texts

        EditText [] hexFromScreen = {
                findViewById(R.id.Word9),
                findViewById(R.id.Word8),
                findViewById(R.id.Word7),
                findViewById(R.id.Word6),
                findViewById(R.id.Word5),
                findViewById(R.id.Word4)
        };

        //Let's get the Array of Strings

//        String [] insertedWords = {
//                "35f2","cf3d","483d","859c","3c14","7416"
//        };

        String [] insertedWords = new String [6];
        boolean flag = false;

        for (int i=0; i<6; i++){
            insertedWords[i] = hexFromScreen[i].getText().toString();
            if(!insertedWords[i].matches("[0-9A-Fa-f]+")){
                //It's not hex, let's print toast and break.
                flag = true;
                System.out.println("NOT HEX: "+insertedWords[i]);
                Context context =getApplicationContext();
                CharSequence text = "Invalid input. Use only HEX characters.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                break;
            } else if (insertedWords[i].toCharArray().length!=4) {
                //It's not complete, let's print toast and break.
                flag = true;
                Context context =getApplicationContext();
                CharSequence text = "Incomplete key. Insert 4 digits per key.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                break;
            }
        }

        if (!flag) {
            String encodedKey = encodeArray(insertedWords);

            //Now let's make encodedKey appear in the screen.

            TextView etv = findViewById(R.id.encodedKey);
            etv.setText(encodedKey);

            hideSoftKeyboard(this);

        }








    }

    public String encodeArray(String[] insertedWords) {

        //Comes in insertedWords: An array from the screen. It comes in ordered.

        String[] encodedWords = new String[6];


        for (int i=0; i<6;i++){
            //Loop through each word.

            //First, separate each word in its individual letters.
            String[] splitHex = Iterables.toArray(Splitter.fixedLength(1).split(insertedWords[i]), String.class);

            //Convert each character into bin
            String[] splitBin = {
                    Decode.hexToBin(splitHex[0]),
                    Decode.hexToBin(splitHex[1]),
                    Decode.hexToBin(splitHex[2]),
                    Decode.hexToBin(splitHex[3])
            };

            //Permutate the values
            String[] permutatedBin = {
                    splitBin[1],
                    splitBin[0],
                    splitBin[3],
                    splitBin[2]
            };

            //Reverse the binaries
            String[] reversedBin = {
                    new StringBuilder(permutatedBin[0]).reverse().toString(),
                    new StringBuilder(permutatedBin[1]).reverse().toString(),
                    new StringBuilder(permutatedBin[2]).reverse().toString(),
                    new StringBuilder(permutatedBin[3]).reverse().toString(),
            };

            //Word goes back to hex
            String[] backToHexEncoded = {
                    Decode.binToHex(reversedBin[0]),
                    Decode.binToHex(reversedBin[1]),
                    Decode.binToHex(reversedBin[2]),
                    Decode.binToHex(reversedBin[3])
            };

            // backToHexEncoded is a string comprised of the 4 characters of a word.
            encodedWords[i] = TextUtils.join("",backToHexEncoded).toUpperCase();

        }

        String encodedKey = TextUtils.join(" - ",encodedWords);

        return encodedKey;




    }
}
