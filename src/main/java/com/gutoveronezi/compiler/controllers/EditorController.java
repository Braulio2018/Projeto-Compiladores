package com.gutoveronezi.compiler.controllers;

import com.gutoveronezi.compiler.compiler.LexicalAnalyzer;
import com.gutoveronezi.compiler.compiler.SyntacticAnalyzer;
import com.gutoveronezi.compiler.enums.TokenType;
import com.gutoveronezi.compiler.exceptions.InvalidStateException;
import com.gutoveronezi.compiler.exceptions.InvalidSyntaxException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair; 

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

    public void runCompiler(String text, int interval) {
        new Thread(() -> {
            console.logInInfo("Starting compiling...");
            LinkedList<Token> userTokens = runLexicalAnalysis(text);
            runSyntacticAnalysis(userTokens, interval);
            console.logInInfo("Finishing compiling...");     
        }).start();
    }

    private LinkedList<Token> runLexicalAnalysis(String text) { 
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(console);
        LinkedList<Token> tokens = lexicalAnalyzer.analyze(text);
        addTokensToTokensTable(tokens);
        return tokens;
    }

    private void runSyntacticAnalysis(LinkedList<Token> userTokens, int interval) {
        if (userTokens == null) { 
            console.logInDebug("Not running syntatic analyzer due to user's token list is null.");
            return;
        }

        if (!userTokens.isEmpty() && userTokens.get(0).getType() != TokenType.PROGRAM) {
            console.logInError("The code must start with the token 'program'.");
            return;
        }
 
        Stack<TokenType> systemTokenTypeStack = setFirstParserToken();
        Stack<Token> userTokenStack = new Stack<>();
        SyntacticAnalyzer syntacticAnalyzer = new SyntacticAnalyzer(console, userTokens, systemTokenTypeStack);
        try {
            Pair <Stack<TokenType>, Stack<Token>> pairTokens;
            while (!systemTokenTypeStack.isEmpty()) {
                pairTokens = syntacticAnalyzer.processNextSystemToken();
                systemTokenTypeStack = pairTokens.getLeft();
                userTokenStack = pairTokens.getRight();

                parseTokensStackToParserTable(systemTokenTypeStack);
                parseTokensStackToTokensTable(userTokenStack);
                Thread.sleep(interval);
            }

            if (!userTokenStack.isEmpty()) {
                throw new InvalidSyntaxException("The code does not match the language syntax.");
            }
            parseTokensStackToParserTable(systemTokenTypeStack);
        } catch (InvalidStateException | InvalidSyntaxException e) {
            console.logInError(e.getMessage());
        } catch (InterruptedException ex) {
            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void parseTokensStackToParserTable(Stack<TokenType> tokenTypeStack) {
        DefaultTableModel model = getParserTableModel();
        model.setRowCount(0);

        int x = 0;
        for (int i = tokenTypeStack.size() - 1; i > -1; i--) {
            Object[] object = {++x, tokenTypeStack.get(i).toString()};
            model.addRow(object);
        }
    }

    private Stack<TokenType> setFirstParserToken() {
        getParserTableModel().setRowCount(0);
        Stack<TokenType> tokenTypeStack = new Stack<>();
        tokenTypeStack.add(TokenType.PROGRAMA);
        parseTokensStackToParserTable(tokenTypeStack);
        return tokenTypeStack;
    }

    private DefaultTableModel getParserTableModel() {
        return (DefaultTableModel) view.getParserTable().getModel();
    }

    private void parseTokensStackToTokensTable(Stack<Token> userTokensStack) {
        LinkedList tokens = new LinkedList();

        for (int i = userTokensStack.size() - 1; i > -1; i--) { 
            tokens.add(userTokensStack.get(i));
        }

        addTokensToTokensTable(tokens);
    }

}
