package com.gutoveronezi.compiler.utils;

import com.gutoveronezi.compiler.enums.LogLevel;
import com.gutoveronezi.compiler.views.EditorView;
import org.apache.commons.lang3.StringUtils;

public class ConsoleUtils {
    private final EditorView view;

    public ConsoleUtils(EditorView view) {
        this.view = view;
    }

    public void logInInfo(String stringToLog) {
        log(stringToLog, LogLevel.INFO);
    }

    public void logInDebug(String stringToLog) {
        log(stringToLog, LogLevel.DEBUG);
    }

    private void log(String stringToLog, LogLevel level) {
        if (StringUtils.isNotBlank(view.getConsoleTextArea().getText())) {
            view.getConsoleTextArea().append("\n");
        }

        view.getConsoleTextArea().append(getLog(stringToLog, level));
    }

    private String getLog(String stringToLog, LogLevel logLevel) {
        return String.format("%s - %s - %s", DateUtils.getDateFormated(), logLevel, stringToLog);
    }
    
}
