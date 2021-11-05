package com.stockchain.cosmos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class Tools {
    public static void showDialog(Context ctx, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton("확인", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
