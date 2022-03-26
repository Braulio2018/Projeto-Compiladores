package com.gutoveronezi.compiler.controllers;

import com.gutoveronezi.compiler.compiler.LexicalAnalyzer;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.views.EditorView;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.StringUtils;

public class EditorController {

    private final EditorView view;
    private final ConsoleUtils console;

    public EditorController(EditorView view) {
        this.view = view;
        this.console = view.getConsole();
    }

    public void newFile() {
        String editorText = view.getEditorPane().getText();

        int answer = 0;
        if (StringUtils.isNotBlank(editorText)) {
            answer = JOptionPane.showConfirmDialog(null, "Do you want to discard the changes in the editor?", "New file",  JOptionPane.OK_CANCEL_OPTION);
        }

        if (answer == 0) {
            console.logInDebug("Cleaning the editor.");
            view.getEditorPane().setText("");
        }
    }

    public void runCompiler(String text) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(console);
        LinkedList<Token> tokens = lexicalAnalyzer.analyze(text);
        addTokensToTable(tokens);
    }
    
    private void addTokensToTable(LinkedList<Token> tokens) {
        ((DefaultTableModel) view.getTokenTable().getModel()).setRowCount(0);
        for (Token token : tokens) {
            Object[] object = {token.getType().toString(), token.getContent(),
                token.getLine(), token.getStartIndex(), token.getEndIndex()};
            ((DefaultTableModel) view.getTokenTable().getModel()).addRow(object);
        }
    }
}
