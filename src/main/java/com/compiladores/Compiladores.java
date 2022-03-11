package com.compiladores;

import com.compiladores.views.EditorView;
import javax.swing.JFrame;

public class Compiladores {

    public static void main(String[] args) {
        EditorView view = new EditorView();
        view.setVisible(true);
        view.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
