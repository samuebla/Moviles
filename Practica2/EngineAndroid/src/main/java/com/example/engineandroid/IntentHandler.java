package com.example.engineandroid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

enum IntentType{ URL }

public class IntentHandler {

    Context context;

    IntentHandler(Context contextAux){
        context = contextAux;
    }

    public void sendIntent(int sendType, String url, String message){
        Uri builtURI = Uri. parse(url ).buildUpon()
                .appendQueryParameter( "text", message)
                .build() ; //Genera la URl https://twitter.com/intent/tweet?text=Este%20es%20mi%20texto%20a%20tweettear
        Intent intent = new Intent(Intent. ACTION_VIEW, builtURI);
        context.startActivity(intent) ; // inicializa el intent
    }

    //Para comprobar la lista de aplicaciones que pueden abrir el intent
    public void checkResolver(){
        Intent share = new Intent(android.content.Intent. ACTION_SEND);
        share.setType( "image/jpeg" );
//        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share , 0);
//        if (!resInfo.isEmpty()){
//            for (ResolveInfo info : resInfo) {
//                if (info.activityInfo .packageName .toLowerCase().contains(nameApp) ||
//                        info. activityInfo .name.toLowerCase().contains(nameApp)) {
//                    share.setPackage(info.activityInfo.packageName);
//                // add other info if necessary
//                }
//            }
//            context.startActivity(share) ;
//        }
    }
}
