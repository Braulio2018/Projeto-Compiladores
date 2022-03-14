package com.gutoveronezi.compiler;

import com.gutoveronezi.compiler.views.EditorView;
import javax.swing.JFrame;

public class Compiler {

    public static void main(String[] args) {
        EditorView view = new EditorView();
        view.setVisible(true);
        view.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
