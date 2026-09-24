package br.edu.utfpr.despensaalerta.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

/**
 * Aplica as cores da identidade visual aos selos de status:
 * fundo suave com texto na cor forte correspondente.
 */
public final class Selos {

    private Selos() {
    }

    public static void colorir(TextView view, int corRes, int fundoRes) {
        Context context = view.getContext();
        view.setTextColor(ContextCompat.getColor(context, corRes));
        view.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, fundoRes)));
    }
}
