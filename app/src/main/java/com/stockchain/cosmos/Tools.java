package com.stockchain.cosmos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Tools {
    public static void showDialog(Context ctx, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(ctx, "확인", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
