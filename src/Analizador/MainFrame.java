package Analizador;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class MainFrame extends JFrame {
    private JTextPane codeTextArea;
    private JTextArea tokensTextArea;
    private JTextArea errorsTextArea;
    private JButton analyzeButton;
    private JButton loadFileButton;
    private JButton clearButton;
    private JLabel statusLabel;

    private static final Color BACKGROUND = new Color(245, 245, 245);
    private static final Color SURFACE = Color.WHITE;
    private static final Color PRIMARY = new Color(70, 130, 180);
    private static final Color SECONDARY = new Color(100, 150, 200);
    private static final Color ACCENT = new Color(33, 150, 243);
    private static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private static final Color TEXT_SECONDARY = new Color(97, 97, 97);

    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("SwiftLex - Analizador Léxico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 850);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);

        createMenuBar();
        createMainPanel();

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(SURFACE);
        menuBar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        JMenu fileMenu = createStyledMenu("Archivo");
        JMenuItem openItem = createStyledMenuItem("Abrir archivo");
        JMenuItem exitItem = createStyledMenuItem("Salir");

        openItem.addActionListener(e -> loadFile());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = createStyledMenu("Ayuda");
        JMenuItem aboutItem = createStyledMenuItem("Acerca de");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(TEXT_PRIMARY);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return menu;
    }

    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setForeground(TEXT_PRIMARY);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return item;
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createCodePanel(), BorderLayout.CENTER);
        mainPanel.add(createResultsPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND);

        JLabel titleLabel = new JLabel("SwiftLex - Analizador Léxico para Swift");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND);

        loadFileButton = createPrimaryButton("📁 Cargar Archivo");
        analyzeButton = createAccentButton("🔍 Analizar Código");
        clearButton = createSecondaryButton("🗑️ Limpiar");

        loadFileButton.addActionListener(e -> loadFile());
        analyzeButton.addActionListener(e -> analyzeCode());
        clearButton.addActionListener(e -> clearAll());

        buttonPanel.add(loadFileButton);
        buttonPanel.add(analyzeButton);
        buttonPanel.add(clearButton);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createAccentButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(SECONDARY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createCodePanel() {
        JPanel codePanel = new JPanel(new BorderLayout(10, 10));
        codePanel.setBackground(BACKGROUND);

        JLabel codeLabel = new JLabel("EDITOR DE CÓDIGO SWIFT");
        codeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        codeLabel.setForeground(TEXT_SECONDARY);
        codeLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

        codeTextArea = new JTextPane();
        codeTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        codeTextArea.setBackground(new Color(253, 253, 253));
        codeTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JScrollPane scrollPane = new JScrollPane(codeTextArea);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        statusLabel = new JLabel(" Listo para analizar");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(TEXT_SECONDARY);
        statusLabel.setBackground(new Color(240, 240, 240));
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        codePanel.add(codeLabel, BorderLayout.NORTH);
        codePanel.add(scrollPane, BorderLayout.CENTER);
        codePanel.add(statusLabel, BorderLayout.SOUTH);

        return codePanel;
    }

    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        resultsPanel.setBackground(BACKGROUND);

        resultsPanel.add(createTokensPanel());
        resultsPanel.add(createErrorsPanel());

        return resultsPanel;
    }

    private JPanel createTokensPanel() {
        JPanel tokensPanel = new JPanel(new BorderLayout());
        tokensPanel.setBackground(BACKGROUND);

        JLabel tokensLabel = new JLabel("TOKENS IDENTIFICADOS");
        tokensLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tokensLabel.setForeground(TEXT_SECONDARY);
        tokensLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        tokensTextArea = new JTextArea();
        tokensTextArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        tokensTextArea.setBackground(SURFACE);
        tokensTextArea.setEditable(false);
        tokensTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JScrollPane tokensScroll = new JScrollPane(tokensTextArea);
        tokensScroll.setBorder(null);

        tokensPanel.add(tokensLabel, BorderLayout.NORTH);
        tokensPanel.add(tokensScroll, BorderLayout.CENTER);

        return tokensPanel;
    }

    private JPanel createErrorsPanel() {
        JPanel errorsPanel = new JPanel(new BorderLayout());
        errorsPanel.setBackground(BACKGROUND);

        JLabel errorsLabel = new JLabel("ERRORES LÉXICOS");
        errorsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        errorsLabel.setForeground(TEXT_SECONDARY);
        errorsLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        errorsTextArea = new JTextArea();
        errorsTextArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        errorsTextArea.setBackground(SURFACE);
        errorsTextArea.setEditable(false);
        errorsTextArea.setForeground(new Color(200, 0, 0));
        errorsTextArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JScrollPane errorsScroll = new JScrollPane(errorsTextArea);
        errorsScroll.setBorder(null);

        errorsPanel.add(errorsLabel, BorderLayout.NORTH);
        errorsPanel.add(errorsScroll, BorderLayout.CENTER);

        return errorsPanel;
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos Swift (*.swift)", "swift"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Archivos de texto (*.txt)", "txt"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                String content = Files.readString(file.toPath());
                codeTextArea.setText(content);

                // Aplicar coloreado inmediatamente al cargar el archivo
                SyntaxHighlighter.applySwiftHighlighting(codeTextArea, content);

                statusLabel.setText(" Archivo cargado: " + file.getName());
            } catch (IOException e) {
                showError("Error al leer el archivo: " + e.getMessage());
            }
        }
    }

    private void analyzeCode() {
        String code = codeTextArea.getText();
        if (code.trim().isEmpty()) {
            showWarning("No hay código para analizar");
            return;
        }

        try {
            statusLabel.setText(" Analizando código...");

            // PRIMERO aplicar el coloreado
            SyntaxHighlighter.applySwiftHighlighting(codeTextArea, code);

            // LUEGO realizar el análisis léxico para mostrar tokens
            SwiftLexer lexer = new SwiftLexer(code);
            java.util.List<Token> tokens = lexer.Analizar();

            displayTokens(tokens);
            displayErrors(lexer, tokens);

            statusLabel.setText(" Análisis completado" +
                    (lexer.tieneError() ? " - Se encontraron errores" : " - Sin errores"));

        } catch (Exception e) {
            statusLabel.setText(" Error durante el análisis");
            showError("Error durante el análisis: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayTokens(java.util.List<Token> tokens) {
        StringBuilder sb = new StringBuilder();

        // Encabezados con anchos ajustados
        sb.append(String.format("%-25s %-30s %-8s %-8s\n",
                "TIPO", "VALOR", "LÍNEA", "COLUMNA"));
        sb.append(String.format("%-25s %-30s %-8s %-8s\n",
                "─────────────────────────", "──────────────────────────────", "────────", "────────"));

        for (Token token : tokens) {
            if (token.getTipo() != TokenType.Espacio &&
                    token.getTipo() != TokenType.Nueva_linea) {

                String tipo = token.getTipo().toString();
                String valor = token.getValor()
                        .replace("\n", "\\n")
                        .replace("\t", "\\t")
                        .replace("\r", "\\r");

                // Limitar longitud del valor pero más generoso
                if (valor.length() > 28) {
                    valor = valor.substring(0, 25) + "...";
                }

                // Formatear con anchos adecuados
                sb.append(String.format("%-25s %-30s %-8d %-8d\n",
                        tipo, valor, token.getLinea(), token.getColumna()));
            }
        }

        tokensTextArea.setText(sb.toString());
    }

    private void displayErrors(SwiftLexer lexer, java.util.List<Token> tokens) {
        StringBuilder sb = new StringBuilder();

        if (lexer.tieneError()) {
            int errorCount = 0;
            for (Token token : tokens) {
                if (token.getTipo() == TokenType.Error) {
                    errorCount++;
                    sb.append(String.format("• Error %d: Línea %d, Columna %d\n",
                            errorCount, token.getLinea(), token.getColumna()));
                    sb.append("  Token: '" + token.getValor() + "'\n\n");
                }
            }
            sb.insert(0, "Se encontraron " + errorCount + " errores:\n\n");
        } else {
            sb.append("✓ Análisis completado sin errores léxicos");
        }

        errorsTextArea.setText(sb.toString());
    }

    private void clearAll() {
        int result = JOptionPane.showConfirmDialog(this,
                "¿Limpiar todo el contenido?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            codeTextArea.setText("");
            tokensTextArea.setText("");
            errorsTextArea.setText("");
            statusLabel.setText(" Listo para analizar");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    private void showAboutDialog() {
        String aboutText =
                "SwiftLex - Analizador Léxico\n\n" +
                        "Versión 1.0\n" +
                        "Analizador léxico para código Swift\n" +
                        "Interfaz minimalista moderna";

        JOptionPane.showMessageDialog(this, aboutText, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame();
                } catch (Exception e) {
                    System.err.println("Error al crear la ventana: " + e.getMessage());
                    e.printStackTrace();


                    JOptionPane.showMessageDialog(null,
                            "No se pudo iniciar la aplicación: " + e.getMessage(),
                            "Error de Inicio", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}