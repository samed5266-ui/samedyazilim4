package com.samed.panel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        // Cihazın açılış (Boot) veya kilit açma (Reboot) sinyallerini yakala
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || 
            "android.intent.action.QUICKBOOT_POWERON".equals(intent.getAction())) {
            
            // TV sisteminin tamamen kendine gelmesi için 2 saniye güvenli gecikme veriyoruz
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent i = new Intent(context, MainActivity.class);
                        // Aktiviteyi arka plandan güvenli bir şekilde ön plana fırlatmak için gerekli bayraklar
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
                                 | Intent.FLAG_ACTIVITY_CLEAR_TOP 
                                 | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 2000); // 2000 milisaniye = 2 Saniye
        }
    }
}
