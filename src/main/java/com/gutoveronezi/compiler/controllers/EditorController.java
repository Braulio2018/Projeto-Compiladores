package com.gutoveronezi.compiler.controllers;

import com.gutoveronezi.compiler.compiler.LexicalAnalyzer;
import com.gutoveronezi.compiler.compiler.SyntacticAnalyzer;
import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.models.Token;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import com.gutoveronezi.compiler.views.EditorView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.math.NumberUtils;

public class EditorController {

    private final EditorView view;
    private final ConsoleUtils console;

    public EditorController(EditorView view) {
        this.view = view;
        this.console = view.getConsole();
    }

    public int newFile() {
        int answer = -1;
        if (view.isEditorTouched()) {
            answer = JOptionPane.showConfirmDialog(null, "Do you want to save the changes in the editor?", "New file", JOptionPane.YES_NO_CANCEL_OPTION);
        }

        if (answer == 2) {
            return answer;
        }

        if (answer == 0) {
            saveFile();
        }

        if (view.getCurrentFile() != null) {
            console.logInDebug(String.format("Closing file [%s].", view.getCurrentFile().getAbsolutePath()));
        }

        view.getEditorPane().setText("");
        view.setIsEditorTouched(false);
        view.setCurrentFile(null);
        return answer;
    }

    public void openFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File("."));

        console.logInInfo("Opening file chooser.");
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (newFile() != 2) {
                console.logInInfo(String.format("File [%s] was chosen.", selectedFile.getAbsolutePath()));
                view.setCurrentFile(selectedFile);
                String loadedContent = loadFile(selectedFile);
                if (loadedContent != null) {
                    view.getEditorPane().setText(loadedContent);
                }
                return;
            }
        }
        console.logInInfo("No file was opened.");
    }

    private String loadFile(File file) {
        StringBuilder fullData = new StringBuilder();
        try (Scanner reader = new Scanner(file)) {
            console.logInInfo(String.format("Reading file [%s].", file.getAbsolutePath()));
            while (reader.hasNextLine()) {
                fullData.append(reader.nextLine());
                fullData.append("\n");
            }
            if (fullData.length() > 0) {
                fullData.setLength(fullData.length() - 1);
            }
            console.logInInfo(String.format("File [%s] was loaded.", file.getAbsolutePath()));
        } catch (FileNotFoundException e) {
            console.logInError(String.format("Failed to load file [%s] due to [%s].", file.getAbsolutePath(), e.getMessage()));
            return null;
        }

        return fullData.toString();
    }

    public void saveFile() {
        String editorText = view.getEditorPane().getText();

        if (view.getCurrentFile() != null) {
            saveContentToFile(view.getCurrentFile(), editorText);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            view.setCurrentFile(file);
            saveContentToFile(file, editorText);
        }
    }

    private void saveContentToFile(File file, String editorText) {
        console.logInInfo(String.format("Saving content to file [%s].", file.getAbsolutePath()));
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] strToBytes = editorText.getBytes();
            outputStream.write(strToBytes);
            view.setIsEditorTouched(false);
        } catch (IOException ex) {
            console.logInError(String.format("Failed to save content to file [%s] due to [%s].", file.getAbsolutePath(), ex.getMessage()));
        }
    }

    public void runCompiler(String text) {
        LinkedList<Token> userTokens = runLexicalAnalysis(text);
        runSyntacticAnalysis(userTokens);
    }

    private LinkedList<Token> runLexicalAnalysis(String text) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(console);
        LinkedList<Token> tokens = lexicalAnalyzer.analyze(text);
        addTokensToTokensTable(tokens);
        return tokens;
    }

    private void runSyntacticAnalysis(LinkedList<Token> userTokens) {
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(console, userTokens, setFirstParserToken());
        syntacticAnalyzer.processNextSystemToken();
    }
   
    private void addTokensToTokensTable(LinkedList<Token> tokens) {
        DefaultTableModel model = (DefaultTableModel) view.getTokenTable().getModel();
        model.setRowCount(0);

        if (tokens == null) {
            return;
        }

        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            Object[] object = {i + 1, token.getType().toString(), token.getContent(),
                token.getLine(), token.getStartIndex(), token.getEndIndex()};
            model.addRow(object);
        }
    }

    private Stack<Token> parseParserTableAsTokenStack() {
        DefaultTableModel model = getParserTableModel();

        Stack<Token> tokens = new Stack<>();

        for (int i = tokens.size() - 1; i == 0; i--) {
            String parserTokenAsString = (String) model.getValueAt(i, 0);
            int tokenId = NumberUtils.toInt(parserTokenAsString.split("-")[0]);
            TokenType tokenTypeFromId = TokenType.getTokenTypeFromId(tokenId);
            tokens.add(new Token(tokenTypeFromId));
        }

        return tokens;
    }

    private void parseTokenStackToParserTable(Stack<Token> tokenStack) {
        DefaultTableModel model = getParserTableModel();

        for (int i = 0; i < tokenStack.size(); i++) {
            Token token = tokenStack.get(i);
            Object[] object = {i + 1, token.getType().toString()};
            model.addRow(object);
        }
    }

    private Stack<Token> setFirstParserToken() {
        getParserTableModel().setRowCount(0);
        Stack<Token> tokens = new Stack<>();
        tokens.add(new Token(TokenType.PROGRAMA));
        parseTokenStackToParserTable(tokens);
        return tokens;
    }

    private DefaultTableModel getParserTableModel() {
        return (DefaultTableModel) view.getParserTable().getModel();
    }

}
