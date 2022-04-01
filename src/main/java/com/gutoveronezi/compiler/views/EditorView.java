package com.gutoveronezi.compiler.views;

import com.gutoveronezi.compiler.controllers.EditorController;
import com.gutoveronezi.compiler.utils.ConsoleUtils;
import javax.swing.JEditorPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class EditorView extends javax.swing.JFrame {

    private final EditorController controller;
    private final ConsoleUtils console;

    public EditorView() {
        initComponents();
        setIcons();
        console = new ConsoleUtils(this);
        controller = new EditorController(this);
        console.logInInfo("Application started!");
    }

    private void setIcons() {
        newFileButton.setIcon(UIManager.getIcon("FileView.fileIcon"));
        openFileButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        saveFileButton.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
        closeFileButton.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        runCompiilerButton.setIcon(UIManager.getIcon("FileView.computerIcon"));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        menuPanel = new javax.swing.JPanel();
        newFileButton = new javax.swing.JButton();
        openFileButton = new javax.swing.JButton();
        saveFileButton = new javax.swing.JButton();
        closeFileButton = new javax.swing.JButton();
        menuSeparator1 = new javax.swing.JSeparator();
        runCompiilerButton = new javax.swing.JButton();
        infoPanel = new javax.swing.JPanel();
        tokenTableScrollPane = new javax.swing.JScrollPane();
        tokenTable = new javax.swing.JTable();
        editorPanel = new javax.swing.JPanel();
        editorScrollPane = new javax.swing.JScrollPane();
        editorPane = new javax.swing.JEditorPane();
        consolePanel = new javax.swing.JPanel();
        consoleScrollPanel = new javax.swing.JScrollPane();
        consoleTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        newFileButton.setToolTipText("New file");
        newFileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newFileButtonActionPerformed(evt);
            }
        });

        openFileButton.setToolTipText("Open a file");

        saveFileButton.setToolTipText("Save the file");

        closeFileButton.setToolTipText("Close the file");

        menuSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        runCompiilerButton.setToolTipText("Run the compiler");
        runCompiilerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runCompiilerButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuPanelLayout = new javax.swing.GroupLayout(menuPanel);
        menuPanel.setLayout(menuPanelLayout);
        menuPanelLayout.setHorizontalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuPanelLayout.createSequentialGroup()
                .addComponent(newFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(openFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeFileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(menuSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(runCompiilerButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        menuPanelLayout.setVerticalGroup(
            menuPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(newFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(openFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(saveFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(closeFileButton, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
            .addComponent(menuSeparator1)
            .addComponent(runCompiilerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        infoPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tokenTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nº", "Type", "Content", "Line", "Start Index", "End Index"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tokenTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tokenTable.getTableHeader().setReorderingAllowed(false);
        tokenTableScrollPane.setViewportView(tokenTable);
        if (tokenTable.getColumnModel().getColumnCount() > 0) {
            tokenTable.getColumnModel().getColumn(3).setResizable(false);
            tokenTable.getColumnModel().getColumn(4).setResizable(false);
            tokenTable.getColumnModel().getColumn(5).setResizable(false);
        }

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tokenTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tokenTableScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        editorPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        editorScrollPane.setViewportView(editorPane);

        javax.swing.GroupLayout editorPanelLayout = new javax.swing.GroupLayout(editorPanel);
        editorPanel.setLayout(editorPanelLayout);
        editorPanelLayout.setHorizontalGroup(
            editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
        );
        editorPanelLayout.setVerticalGroup(
            editorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(editorScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
        );

        consolePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        consoleTextArea.setEditable(false);
        consoleTextArea.setColumns(20);
        consoleTextArea.setRows(5);
        consoleScrollPanel.setViewportView(consoleTextArea);

        javax.swing.GroupLayout consolePanelLayout = new javax.swing.GroupLayout(consolePanel);
        consolePanel.setLayout(consolePanelLayout);
        consolePanelLayout.setHorizontalGroup(
            consolePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(consoleScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 990, Short.MAX_VALUE)
        );
        consolePanelLayout.setVerticalGroup(
            consolePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(consoleScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(menuPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(consolePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(infoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(editorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(consolePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newFileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newFileButtonActionPerformed
        controller.newFile();
    }//GEN-LAST:event_newFileButtonActionPerformed

    private void runCompiilerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runCompiilerButtonActionPerformed
        controller.runCompiler(editorPane.getText());
    }//GEN-LAST:event_runCompiilerButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeFileButton;
    private javax.swing.JPanel consolePanel;
    private javax.swing.JScrollPane consoleScrollPanel;
    private javax.swing.JTextArea consoleTextArea;
    private javax.swing.JEditorPane editorPane;
    private javax.swing.JPanel editorPanel;
    private javax.swing.JScrollPane editorScrollPane;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JSeparator menuSeparator1;
    private javax.swing.JButton newFileButton;
    private javax.swing.JButton openFileButton;
    private javax.swing.JButton runCompiilerButton;
    private javax.swing.JButton saveFileButton;
    private javax.swing.JTable tokenTable;
    private javax.swing.JScrollPane tokenTableScrollPane;
    // End of variables declaration//GEN-END:variables

    public JTextArea getConsoleTextArea() {
        return consoleTextArea;
    }

    public JEditorPane getEditorPane() {
        return editorPane;
    }

    public ConsoleUtils getConsole() {
        return console;
    }

    public JTable getTokenTable() {
        return tokenTable;
    }

}
