package Analizador;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class SyntaxHighlighter {
    
    private static final Color KEYWORD_COLOR = new Color(170, 40, 170);
    private static final Color STRING_COLOR = new Color(200, 40, 40);
    private static final Color NUMBER_COLOR = new Color(30, 120, 30);
    private static final Color COMMENT_COLOR = new Color(100, 100, 100);
    private static final Color DEFAULT_COLOR = Color.BLACK;
    
    private static final String[] KEYWORDS = {
        "var", "let", "func", "class", "struct", "enum", "protocol", "extension",
        "if", "else", "switch", "case", "default", "for", "while", "repeat",
        "in", "return", "import", "init", "self", "Self", "deinit", "guard",
        "defer", "do", "try", "catch", "throw", "as", "is", "nil", "true",
        "false", "super", "any", "some", "where", "break", "continue", "fallthrough"
    };
    
    public static void applySwiftHighlighting(JTextPane textPane) {
        String text = textPane.getText();
        StyledDocument doc = textPane.getStyledDocument();
        
        Style defaultStyle = textPane.addStyle("default", null);
        StyleConstants.setForeground(defaultStyle, DEFAULT_COLOR);
        
        Style keywordStyle = textPane.addStyle("keyword", null);
        StyleConstants.setForeground(keywordStyle, KEYWORD_COLOR);
        StyleConstants.setBold(keywordStyle, true);
        
        Style stringStyle = textPane.addStyle("string", null);
        StyleConstants.setForeground(stringStyle, STRING_COLOR);
        
        Style numberStyle = textPane.addStyle("number", null);
        StyleConstants.setForeground(numberStyle, NUMBER_COLOR);
        
        Style commentStyle = textPane.addStyle("comment", null);
        StyleConstants.setForeground(commentStyle, COMMENT_COLOR);
        StyleConstants.setItalic(commentStyle, true);
        
        doc.setCharacterAttributes(0, text.length(), defaultStyle, true);
        
        for (String keyword : KEYWORDS) {
            highlightPattern(doc, "\\b" + keyword + "\\b", keywordStyle, text);
        }
        
        highlightPattern(doc, "\"[^\"]*\"", stringStyle, text);
        highlightPattern(doc, "\\b\\d+\\.?\\d*\\b", numberStyle, text);
        highlightPattern(doc, "//[^\n]*", commentStyle, text);
        highlightPattern(doc, "/\\*.*?\\*/", commentStyle, text);
    }
    
    private static void highlightPattern(StyledDocument doc, String pattern, Style style, String text) {
        try {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(text);
            
            while (m.find()) {
                doc.setCharacterAttributes(m.start(), m.end() - m.start(), style, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
