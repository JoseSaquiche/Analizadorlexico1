package Analizador;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
   private JTextPane codeTextArea;
   private JTable tokensTable; // Cambiado de JTextArea a JTable
   private JTextArea errorsTextArea;
   private JButton analyzeButton;
   private JButton loadFileButton;
   private JButton clearButton;
   private JLabel statusLabel;
   private static final Color BACKGROUND = new Color(245, 245, 245);
   private static final Color SURFACE;
   private static final Color PRIMARY;
   private static final Color SECONDARY;
   private static final Color ACCENT;
   private static final Color TEXT_PRIMARY;
   private static final Color TEXT_SECONDARY;

   public MainFrame() {
      this.initializeUI();
   }

   private void initializeUI() {
      this.setTitle("SwiftLex - Analizador Léxico");
      this.setDefaultCloseOperation(3);
      this.setSize(1300, 850);
      this.setLocationRelativeTo((Component)null);
      this.getContentPane().setBackground(BACKGROUND);
      this.createMenuBar();
      this.createMainPanel();
      this.setVisible(true);
   }

   private void createMenuBar() {
      JMenuBar menuBar = new JMenuBar();
      menuBar.setBackground(SURFACE);
      menuBar.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
      JMenu fileMenu = this.createStyledMenu("Archivo");
      JMenuItem openItem = this.createStyledMenuItem("Abrir archivo");
      JMenuItem exitItem = this.createStyledMenuItem("Salir");
      openItem.addActionListener((e) -> {
         this.loadFile();
      });
      exitItem.addActionListener((e) -> {
         System.exit(0);
      });
      fileMenu.add(openItem);
      fileMenu.addSeparator();
      fileMenu.add(exitItem);
      JMenu helpMenu = this.createStyledMenu("Ayuda");
      JMenuItem aboutItem = this.createStyledMenuItem("Acerca de");
      aboutItem.addActionListener((e) -> {
         this.showAboutDialog();
      });
      helpMenu.add(aboutItem);
      menuBar.add(fileMenu);
      menuBar.add(helpMenu);
      this.setJMenuBar(menuBar);
   }

   private JMenu createStyledMenu(String text) {
      JMenu menu = new JMenu(text);
      menu.setForeground(TEXT_PRIMARY);
      menu.setFont(new Font("Segoe UI", 0, 12));
      return menu;
   }

   private JMenuItem createStyledMenuItem(String text) {
      JMenuItem item = new JMenuItem(text);
      item.setForeground(TEXT_PRIMARY);
      item.setFont(new Font("Segoe UI", 0, 12));
      return item;
   }

   private void createMainPanel() {
      JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
      mainPanel.setBackground(BACKGROUND);
      mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
      mainPanel.add(this.createHeaderPanel(), "North");
      mainPanel.add(this.createCodePanel(), "Center");
      mainPanel.add(this.createResultsPanel(), "South");
      this.add(mainPanel);
   }

   private JPanel createHeaderPanel() {
      JPanel headerPanel = new JPanel(new BorderLayout());
      headerPanel.setBackground(BACKGROUND);
      JLabel titleLabel = new JLabel("SwiftLex - Analizador Léxico para Swift");
      titleLabel.setFont(new Font("Segoe UI", 1, 24));
      titleLabel.setForeground(PRIMARY);
      JPanel buttonPanel = new JPanel(new FlowLayout(2, 10, 0));
      buttonPanel.setBackground(BACKGROUND);
      this.loadFileButton = this.createPrimaryButton("\ud83d\udcc1 Cargar Archivo");
      this.analyzeButton = this.createAccentButton("\ud83d\udd0d Analizar Código");
      this.clearButton = this.createSecondaryButton("\ud83d\uddd1️ Limpiar");
      this.loadFileButton.addActionListener((e) -> {
         this.loadFile();
      });
      this.analyzeButton.addActionListener((e) -> {
         this.analyzeCode();
      });
      this.clearButton.addActionListener((e) -> {
         this.clearAll();
      });
      buttonPanel.add(this.loadFileButton);
      buttonPanel.add(this.analyzeButton);
      buttonPanel.add(this.clearButton);
      headerPanel.add(titleLabel, "West");
      headerPanel.add(buttonPanel, "East");
      return headerPanel;
   }

   private JButton createPrimaryButton(String text) {
      JButton button = new JButton(text);
      button.setBackground(PRIMARY);
      button.setForeground(Color.WHITE);
      button.setFont(new Font("Segoe UI", 1, 12));
      button.setFocusPainted(false);
      button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
      button.setCursor(new Cursor(12));
      return button;
   }

   private JButton createAccentButton(String text) {
      JButton button = new JButton(text);
      button.setBackground(ACCENT);
      button.setForeground(Color.WHITE);
      bu
