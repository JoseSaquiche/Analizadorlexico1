package Analizador;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class SyntaxHighlighter {

    private static final Color KEYWORD_COLOR = new Color(156, 39, 176);
    private static final Color NUMBER_COLOR = new Color(255, 87, 34);
    private static final Color STRING_COLOR = new Color(76, 175, 80);
    private static final Color COMMENT_COLOR = new Color(158, 158, 158);
    private static final Color OPERATOR_COLOR = new Color(33, 150, 243);
    private static final Color ERROR_COLOR = new Color(244, 67, 54);

    public static void applySwiftHighlighting(JTextPane textPane, String code) {
        try {
            StyledDocument doc = textPane.getStyledDocument();

            // Limpiar estilos previos de forma correcta
            Style defaultStyle = textPane.getStyle(StyleContext.DEFAULT_STYLE);
            doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);

            SwiftLexer lexer = new SwiftLexer(code);
            java.util.List<Token> tokens = lexer.Analizar();

            // Crear estilos
            Style keywordStyle = doc.addStyle("keyword", null);
            StyleConstants.setForeground(keywordStyle, KEYWORD_COLOR);
            StyleConstants.setBold(keywordStyle, true);

            Style numberStyle = doc.addStyle("number", null);
            StyleConstants.setForeground(numberStyle, NUMBER_COLOR);

            Style stringStyle = doc.addStyle("string", null);
            StyleConstants.setForeground(stringStyle, STRING_COLOR);

            Style commentStyle = doc.addStyle("comment", null);
            StyleConstants.setForeground(commentStyle, COMMENT_COLOR);
            StyleConstants.setItalic(commentStyle, true);

            Style operatorStyle = doc.addStyle("operator", null);
            StyleConstants.setForeground(operatorStyle, OPERATOR_COLOR);
            StyleConstants.setBold(operatorStyle, true);

            Style errorStyle = doc.addStyle("error", null);
            StyleConstants.setBackground(errorStyle, new Color(255, 235, 238));
            StyleConstants.setForeground(errorStyle, ERROR_COLOR);
            StyleConstants.setBold(errorStyle, true);

            // Aplicar estilos a cada token
            for (Token token : tokens) {
                int start = calculatePosition(code, token.getLinea(), token.getColumna());
                int length = token.getValor().length();

                if (start >= 0 && start + length <= code.length()) {
                    switch (token.getTipo()) {
                        case Palabra_Reservada:
                            doc.setCharacterAttributes(start, length, keywordStyle, false);
                            break;
                        case Literal_Numerico:
                            doc.setCharacterAttributes(start, length, numberStyle, false);
                            break;
                        case Literal_Cadena:
                            doc.setCharacterAttributes(start, length, stringStyle, false);
                            break;
                        case Comentario_Linea:
                        case Comentario_Bloque:
                            doc.setCharacterAttributes(start, length, commentStyle, false);
                            break;
                        case Operador_Aritmetico:
                        case Operador_Comparacion:
                        case Operador_Logico:
                        case Operador_Asignacion:
                        case Operador_Rango:
                        case Operador_Nil_Coalescente:
                            doc.setCharacterAttributes(start, length, operatorStyle, false);
                            break;
                        case Error:
                            doc.setCharacterAttributes(start, length, errorStyle, false);
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int calculatePosition(String text, int line, int column) {
        if (line < 1 || column < 1) return 0;

        String[] lines = text.split("\n", -1);
        int position = 0;

        for (int i = 0; i < line - 1 && i < lines.length; i++) {
            position += lines[i].length() + 1;
        }

        position += Math.min(column - 1, lines[line - 1].length());
        return Math.min(position, text.length());
    }
}