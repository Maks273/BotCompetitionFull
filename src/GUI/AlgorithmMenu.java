/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import filework.FileWork;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author maks
 */
@SuppressWarnings({"FieldCanBeLocal", "rawtypes"})
public class AlgorithmMenu extends javax.swing.JFrame {

    MainMenu m = new MainMenu();
    private static class MarksCollection {

        private final ArrayList<String> marks;
        private final ArrayList<MarkUpdateListener> listeners;

        public MarksCollection() {
            marks = new ArrayList<>();
            listeners = new ArrayList<>();
        }

        /**
         * this method adds listener for mark
         *
         * @param listener mark listener
         */
        public void addListener(MarkUpdateListener listener) {
            listeners.add(listener);
        }

        /**
         * this method removes listener for mark
         *
         * @param listener
         */
        public void removeListener(MarkUpdateListener listener) {
            listeners.remove(listener);
        }

        /**
         * this method adds mark
         *
         * @param mark name mark in String
         */
        public void addMark(String mark) {
            marks.add(mark);
            listeners.forEach(MarkUpdateListener::notifyAboutUpdate);
        }

        /**
         * this method removes mark
         *
         * @param mark name mark in String
         */
        public void removeMark(String mark) {
            marks.remove(mark);
            listeners.forEach(MarkUpdateListener::notifyAboutUpdate);
        }

        /**
         * this method gets marks array list
         *
         * @return array list of marks
         */
        public ArrayList<String> getMarks() {
            return new ArrayList<>(marks);
        }
    }

    /**
     * mark update interface
     */
    private interface MarkUpdateListener {

        void notifyAboutUpdate();
    }

    public String[] sideNameArr = {
        "BOTTOM",
        "LEFT",
        "TOP",
        "RIGHT"
    };
    public Map<String, String> availableCommandForCmb = new HashMap<>();
    private String componentText = "";
    private final FileWork fileWork = new FileWork();
    public Date date = new Date();
    private final Map<String, String> commandForFileMap = new HashMap<>();
    private int i = 1;
    private int locPanelX = 0;
    private int locPanelY = 0;
    private int instPaneLastComponentYPos = 0;
    private int instPaneHeight = 0;
    private final MarksCollection marksCollection;
    public int markCount = 0;
    public JComboBox<String> marksCmb = new JComboBox<>();

    private ArrayList<Pair<String, String[]>> commandsWithArguments;

    /**
     * Creates new form AlgorithmMenu
     */
    public AlgorithmMenu() {
        marksCollection = new MarksCollection();
        initComponents();
        jScrollPane1.setMaximumSize(new Dimension(430, 550));
        jScrollPane1.setPreferredSize(new Dimension(430, 550));
        jScrollPane1.setMinimumSize(new Dimension(430, 550));
        jScrollPane2.setMaximumSize(new Dimension(430, 550));
        jScrollPane2.setPreferredSize(new Dimension(430, 550));
        jScrollPane2.setMinimumSize(new Dimension(430, 550));
        saveAlgBtn.setText("Зберегти");
        jTabbedPane1.setTitleAt(0, "Створити алгоритм");
        jTabbedPane1.setTitleAt(1, "Переглянути алгоритм");
        updateBtn.setText("Оновити");
        //programList.setEnabled(false);
        setLocationRelativeTo(null);
        addElemIntoAvailableCommandMap();

        addElemIntoMap();
        addCommandInCommandPane();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                String[] btn = {"Так", "Ні"};
                int a = JOptionPane.showOptionDialog(rootPane, "Ви впевнені?", "Вихід",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, btn, "OK");
                if (a == JOptionPane.YES_OPTION || a == 0) {
                    dispose();
                    m.setVisible(true);
                } else {
                      
                }
            }
        });
    }

    /**
     * this method put available command into map
     */
    public void addElemIntoAvailableCommandMap() {
        availableCommandForCmb.put("Зчитати координати в пам'ять", "getCurCords");
        availableCommandForCmb.put("Записати вміст пам'яті в стек", "pushIntoStack");
        availableCommandForCmb.put("Зчитати стек", "peekCordsStack");
        availableCommandForCmb.put("Переміститись", "moveAt");
        availableCommandForCmb.put("Перейти до мітки", "goTo");
        availableCommandForCmb.put("Витягнути із стеку в пам'ять", "popCordsStack");
        availableCommandForCmb.put("Крок назад", "comeBackOneStep");
    }

    /**
     * this method adds name for buttons and their id name into map collection
     */
    public void addElemIntoMap() {
        commandsWithArguments = new ArrayList<>();
        commandForFileMap.put("Зчитати координати в пам'ять", "getCurCords");
        commandForFileMap.put("Записати вміст пам'яті в стек", "pushIntoStack");
        commandsWithArguments.add(new Pair<>("Записати вміст пам'яті в стек", new String[]{"stack1", "stack2"}));
        commandForFileMap.put("Зчитати стек", "peekCordsStack");
        commandsWithArguments.add(new Pair<>("Зчитати стек", new String[]{"stack1", "stack2"}));
        commandForFileMap.put("Переміститись", "moveAt");
        commandsWithArguments.add(new Pair<>("Переміститись", sideNameArr));
        commandForFileMap.put("Витягнути із стеку в пам'ять", "popCordsStack");
        commandsWithArguments.add(new Pair<>("Витягнути із стеку в пам'ять", new String[]{"stack1", "stack2"}));
        commandForFileMap.put("Крок назад", "comeBackOneStep");
        commandForFileMap.put("Якщо пусто", "if(lookAt)");
        commandForFileMap.put("То", "then");
        commandForFileMap.put("Інакше", "else");
        commandForFileMap.put("Мітка", "mark");
        commandForFileMap.put("Перейти до мітки", "goTo");
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    /**
     * this method adds buttons into command pane
     */
    public void addCommandInCommandPane() {
        instPane.getName();
        for (Map.Entry<String, String> curMap : commandForFileMap.entrySet()) {

            if (curMap.getKey().equals("Переміститись")) {
                JLabel label = new JLabel(curMap.getKey());
                label.setName(curMap.getValue());
                JComboBox<String> sidesCombobox = createSideCmb();
                JPanel component = createPanel(label, sidesCombobox);
                commandPane.add(component);

            } else if (curMap.getKey().equals("Записати вміст пам'яті в стек")
                    || curMap.getKey().equals("Зчитати стек")
                    || curMap.getKey().equals("Витягнути із стеку в пам'ять")) {
                JLabel label = new JLabel(curMap.getKey());
                label.setName(curMap.getValue());
                JComboBox<String> comboBoxStacks = new JComboBox<>();
                comboBoxStacks.addItem("stack1");
                comboBoxStacks.addItem("stack2");
                Component component = createPanel(label, comboBoxStacks);

                commandPane.add(component);
            } else if (curMap.getKey().equals("Якщо пусто")) {
                JLabel label = new JLabel(curMap.getKey());
                label.setName(curMap.getValue());
                JComboBox<String> sidesCombobox = createSideCmb();

                JLabel thenLabel = new JLabel("То");
                thenLabel.setName(commandForFileMap.get("То"));
                JComboBox<String> commandCmb = createAvailableCommandsCmb();
                //commandCmb.setName("commandCmb");
                JComboBox<String> argsCmb = createArgumentsCmb();
                commandCmb.addItemListener(itemEvent -> {
                    argsCmb.removeAllItems();
                    String selectedCommand = (String) itemEvent.getItem();
                    if (selectedCommand.equals("Перейти до мітки")) {
                        argsCmb.setVisible(true);
                        marksCollection.getMarks().forEach(argsCmb::addItem);
                        //for (int i = 0; i < marksCmb.getItemCount(); i++) {
                        //    argsCmb.addItem(marksCmb.getItemAt(i));
                        //}
                    } else {
                        Optional<Pair<String, String[]>> optionalThisCommandWithArguments = commandsWithArguments.stream()
                                .filter(commandPair -> commandPair.getKey().equals(selectedCommand))
                                .findFirst();
                        if (optionalThisCommandWithArguments.isPresent()) {
                            argsCmb.setVisible(true);
                            String[] argumentsToAdd = optionalThisCommandWithArguments.get().getValue();
                            for (String argument : argumentsToAdd) {
                                argsCmb.addItem(argument);
                            }
                        } else {
                            argsCmb.setVisible(false);
                        }
                    }
                });
                argsCmb.setVisible(false);
                JLabel elseLabel = new JLabel("Інакше");
                elseLabel.setName(commandForFileMap.get("Інакше"));
                JComboBox<String> commandCmb2 = (JComboBox<String>) cloneSwingComponent(commandCmb);
                JComboBox<String> argsCmb2 = createArgumentsCmb();
                commandCmb2.addItemListener(itemEvent -> {
                    argsCmb2.removeAllItems();
                    String selectedCommand = (String) itemEvent.getItem();
                    if (selectedCommand.equals("Перейти до мітки")) {
                        argsCmb2.setVisible(true);
                        marksCollection.addListener(() -> {
                            argsCmb2.removeAllItems();
                            marksCollection.getMarks().forEach(argsCmb2::addItem);
                        });
                        marksCollection.getMarks().forEach(argsCmb2::addItem);
                        //for (int i = 0; i < marksCmb.getItemCount(); i++) {
                        //    argsCmb.addItem(marksCmb.getItemAt(i));
                        //}
                    } else {
                        Optional<Pair<String, String[]>> optionalThisCommandWithArguments = commandsWithArguments.stream()
                                .filter(commandPair -> commandPair.getKey().equals(selectedCommand))
                                .findFirst();
                        if (optionalThisCommandWithArguments.isPresent()) {
                            argsCmb2.setVisible(true);
                            String[] argumentsToAdd = optionalThisCommandWithArguments.get().getValue();
                            for (String argument : argumentsToAdd) {
                                argsCmb2.addItem(argument);
                            }
                        } else {
                            argsCmb2.setVisible(false);
                        }
                    }
                });
                argsCmb2.setVisible(false);

                Component component = createPanel(label, sidesCombobox, thenLabel, commandCmb, argsCmb,
                        elseLabel, commandCmb2, argsCmb2);
                component.setName("if(lookAt)");
                commandPane.add(component);
            } else if (curMap.getKey().equals("Перейти до мітки")) {
                JLabel label = new JLabel(curMap.getKey());
                label.setName(curMap.getValue());
                Component component = createPanel(label, marksCmb);
                commandPane.add(component);
            } else if (!curMap.getKey().equals("Інакше")) {
                if (!curMap.getKey().equals("То")) {

                    JLabel label = new JLabel(curMap.getKey());
                    label.setName(curMap.getValue());
                    Component component = createPanel(label);
                    commandPane.add(component);
                }
            }

        }
    }

    /**
     * this method creates arguments combo box
     *
     * @return arguments combo box
     */
    public JComboBox<String> createArgumentsCmb() {
        JComboBox<String> argCmb = new JComboBox<>();

        return argCmb;
    }

    /**
     * this method creates available command combo box
     *
     * @return available command combo box
     */
    public JComboBox<String> createAvailableCommandsCmb() {
        JComboBox<String> commandCmb = new JComboBox<>();

        for (Map.Entry<String, String> availableCommandPair : availableCommandForCmb.entrySet()) {
            commandCmb.addItem(availableCommandPair.getKey());

        }

        return commandCmb;
    }

    /**
     * this method creates sides combo box
     *
     * @return sides combo box
     */
    public JComboBox<String> createSideCmb() {
        JComboBox<String> sidesCombobox = new JComboBox<>();
        for (String side : sideNameArr) {
            sidesCombobox.addItem(side);
        }
        return sidesCombobox;
    }

    /**
     * this event when update btn is pressed, he update available bot`s program
     */
    private void updateBtnActionPerformed() {
        DefaultListModel<String> defListProgram = new DefaultListModel<>();
        File pathDirectory = new File("res/botPrograms");

        for (File programFile : Objects.requireNonNull(pathDirectory.listFiles())) {

            if (programFile.getName().endsWith(".txt")) {

                defListProgram.addElement(programFile.getName());
            }
        }

        programList.setModel(defListProgram);
        programList.revalidate();

    }

    /**
     * this event when user chooses object from program list
     *
     * @param evt list event
     */
    private void programListValueChanged(javax.swing.event.ListSelectionEvent evt) {

        if (evt.getValueIsAdjusting()) {
            readCommandField.setText("");
            JList source = (JList) evt.getSource();
            String selected = source.getSelectedValue().toString();
            fileWork.printFile(readCommandField, "res/botPrograms/" + selected);
        }

    }

    /**
     * this event for saveBtn he saves data from directions pane in bot program
     * file
     */
    public void saveAlgBtnActionPerformed() {
        DateFormat dateFormat = new SimpleDateFormat("mmss");
        Date date = new Date();
        fileWork.createDir();
        File dir = new File("res/botPrograms/");
        int CountFile = dir.list().length;
        
        StringBuilder text = new StringBuilder();
        if (instPane.getComponentCount() != 0) {
            for (Component component : instPane.getComponents()) {
                JPanel panel = (JPanel) component;
                getTextFromPanelComponent(panel);
                text.append(componentText).append("\n");
            }
            String in = JOptionPane.showInputDialog(null, "Введіть назву інструкції", "Збереження", JOptionPane.QUESTION_MESSAGE);
            if(in.matches("[0-9a-zA-Z]{0,9}"))
            {
            fileWork.addToFile(text.toString(), "res/botPrograms/" +in);
            CountFile += 1;
            instPane.removeAll();
            instPane.updateUI();
            instPaneLastComponentYPos = 0;
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "Неправильно введена назва файлу\nНазва файлу може містити цифри та букви англійського алфавіту", "Помилка", 0);
            }
        }

    }

    /**
     * this method create custom panel for command and he creates events for
     * adding and deleting panels from commandPane and instPane
     *
     * @param components component for custom panel
     * @return new custom panel
     */
    public JPanel createPanel(JComponent... components) {

        JPanel customPane = new JPanel();
        customPane.setBackground(Color.GRAY);
        customPane.setSize(commandPane.getWidth(), 65);
        customPane.setLocation(locPanelX, locPanelY);
        customPane.setBorder(BorderFactory.createLineBorder(Color.RED));
        for (JComponent component : components) {
            customPane.add(component);
        }
        customPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getTextFromPanelComponent(customPane);
                instPane.setSize(commandPane.getWidth() + 100, commandPane.getHeight());
                Component newInstPanel = cloneSwingComponent(customPane);
                if (newInstPanel == null) {
                    return;
                }
                newInstPanel.removeMouseListener(this);
                setMarkNumber((JPanel) newInstPanel);

                Arrays.stream(((JPanel) newInstPanel).getComponents())
                        .filter(child -> child instanceof JComboBox)
                        .forEach(child -> child.setEnabled(false));
                //for (Component newInstPaneChild:((JPanel) newInstPanel).getComponents()) {
                //    newInstPaneChild.setEnabled(false);
                //}
                newInstPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                        instPaneLastComponentYPos = newInstPanel.getY();
                        deleteMarkFromInstrPane((JPanel) newInstPanel);
                        instPane.remove(newInstPanel);
                        instPaneHeight -= 40;
                        instPane.setSize(instPane.getWidth(), Math.min(375, instPaneHeight));
                        instPane.updateUI();
                        for (Component component : instPane.getComponents()) {
                            if (component.getY() > instPaneLastComponentYPos) {

                                component.setLocation(0, instPaneLastComponentYPos);
                                instPaneLastComponentYPos += 70;
                                instPane.updateUI();
                            }
                        }
                    }
                });

                newInstPanel.setLocation(0, instPaneLastComponentYPos);
                newInstPanel.setSize(instPane.getWidth(), newInstPanel.getHeight());

                instPaneLastComponentYPos += 70;
                instPaneHeight += 70;
                instPane.setMinimumSize(new Dimension(instPane.getWidth(), instPaneHeight));
                instPane.setPreferredSize(new Dimension(instPane.getWidth(), instPaneHeight));
                instPane.add(newInstPanel);
                instPane.setBackground(Color.BLUE);
                instPane.setAutoscrolls(true);
                instPane.revalidate();
                instPane.updateUI();

                requestFocusInWindow();
            }

        });

        locPanelY += 70;
        return customPane;
    }

    /**
     * this method set number for mark
     *
     * @param jPanel panel from commandPane
     */
    public void setMarkNumber(JPanel jPanel) {

        for (Component component : jPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel lbl = (JLabel) component;
                if (lbl.getText().equals("Мітка")) {
                    String markName = lbl.getName() + markCount;
                    String markText = lbl.getText() + markCount;
                    lbl.setText(markText);
                    lbl.setName(markName);
                    marksCollection.addMark(lbl.getName());
                    marksCmb.addItem(lbl.getName());
                    marksCmb.updateUI();
                    markCount++;
                }
            }
        }

    }

    /**
     * this method removes mark from instruction panel
     *
     * @param jPanel custom panel
     */
    public void deleteMarkFromInstrPane(JPanel jPanel) {

        for (Component component : jPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel lbl = (JLabel) component;
                marksCollection.removeMark(lbl.getName());
                marksCmb.removeItem(lbl.getName());
            }
        }

    }

    /**
     * this method clones component
     *
     * @param c component
     * @return new cloned component
     */
    private Component cloneSwingComponent(Component c) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Component) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * this method gets text from custom pane children and writes him into
     * variable componentText
     */
    public void getTextFromPanelComponent(JPanel customPane) {
        boolean check = false;
        if (customPane.getName() != null && customPane.getName().equals("if(lookAt)")) {
            int currentComponentIndex = 0;
            componentText = "if(lookAt(";
            currentComponentIndex++; // skip "РЇРєС‰Рѕ РїСѓСЃС‚Рѕ" and get straight side combo
            JComboBox<String> sideComboBox = (JComboBox<String>) customPane
                    .getComponent(currentComponentIndex); // side combo
            String selectedSide = (String) sideComboBox.getSelectedItem();
            componentText += selectedSide + ")";
            currentComponentIndex += 2; // skip "С‚Рѕ" and get straight firstCommand combo index
            JComboBox<String> firstCommandBox = (JComboBox<String>) customPane
                    .getComponent(currentComponentIndex); // firstCommand index
            String selectedRawFirstCommand = (String) firstCommandBox.getSelectedItem();
            String selectedFirstCommand = availableCommandForCmb.get(selectedRawFirstCommand);
            componentText += "," + selectedFirstCommand;
            currentComponentIndex++; // up to firstCommandArgument index
            JComboBox<String> firstCommandArgumentBox = (JComboBox<String>) customPane
                    .getComponent(currentComponentIndex); // firstCommandArgument index
            if (firstCommandArgumentBox.isVisible()) { // if firstCommand has arguments
                String selectedFirstCommandArgument = (String) firstCommandArgumentBox.getSelectedItem();
                //String selectedFirstCommandArgument = availableCommandForCmb.get(selectedRawFirstCommandArgument);
                componentText += "," + selectedFirstCommandArgument;
            } else {
                componentText += ",null";
            }
            currentComponentIndex += 2; // skip "РёРЅР°С‡Рµ" and get straight secondCommand combo index
            JComboBox<String> secondCommandBox = (JComboBox<String>) customPane
                    .getComponent(currentComponentIndex); // secondCommand index
            String selectedRawSecondCommand = (String) secondCommandBox.getSelectedItem();
            String selectedSecondCommand = availableCommandForCmb.get(selectedRawSecondCommand);
            componentText += "," + selectedSecondCommand;
            currentComponentIndex++; // up to secondCommandArgumentIndex
            JComboBox<String> secondCommandArgumentBox = (JComboBox<String>) customPane
                    .getComponent(currentComponentIndex); // secondCommand index
            if (secondCommandArgumentBox.isVisible()) { // if secondCommand has arguments
                String selectedSecondCommandArgument = (String) secondCommandArgumentBox.getSelectedItem();
                //String selectedSecondCommandArgument = availableCommandForCmb.get(selectedRawSecondCommandArgument);
                componentText += "," + selectedSecondCommandArgument;
            } else {
                componentText += ",null";
            }
            componentText += ")";
        } else {
            for (int j = 0; j < customPane.getComponentCount(); j++) {
                Component component = customPane.getComponent(j);
                if (component instanceof JLabel) {
                    JLabel lbl = (JLabel) component;
                    componentText = lbl.getName();
                }

                if (customPane.getComponentCount() > 1) {
                    j++;
                    component = customPane.getComponent(j);
                    if (component instanceof JComboBox) {
                        JComboBox comboBox = (JComboBox) component;

                        for (Map.Entry<String, String> availableCommandPair : availableCommandForCmb.entrySet()) {
                            if (Objects.equals(comboBox.getSelectedItem(), availableCommandPair.getKey())) {
                                componentText = componentText + "(" + availableCommandPair.getValue() + ")";
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            componentText = componentText + "(" + comboBox.getSelectedItem() + ")";
                        }
                    }
                }
            }
        }
        System.out.println(componentText);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        commandPane = new javax.swing.JPanel();
        instPane = new javax.swing.JPanel();
        saveAlgBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        programList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        readCommandField = new javax.swing.JTextArea();
        updateBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        // this.setBackground(new Color(204, 0, 204));
        jPanel1.setBackground(new Color(204, 0, 204));
        jTabbedPane1.setBackground(new Color(0, 51, 204));
        jTabbedPane1.setForeground(Color.RED);
        jTabbedPane1.setTabPlacement(JTabbedPane.LEFT);
        jTabbedPane1.setToolTipText("");
        // jPanel1.setBackground(new Color(0, 0, 102));
        instPane.setBackground(new Color(0, 51, 204));
        instPane.setForeground(new Color(0, 0, 0));
        commandPane.setBackground(new Color(0, 51, 204));
        commandPane.setForeground(new Color(0, 0, 0));
        saveAlgBtn.setLocation(0, 0);

        saveAlgBtn.setBackground(new Color(0, 51, 204));
        saveAlgBtn.setFont(new Font("Times New Roman", 1, 18)); // NOI18N
        saveAlgBtn.setForeground(new Color(255, 255, 255));
        saveAlgBtn.setText("Р—Р±РµСЂРµРіС‚Рё");


        programList.setFont(new Font("Times New Roman", 1, 18)); // NOI18N
        programList.setForeground(new Color(255, 255, 255));
        readCommandField.setFont(new Font("Times New Roman", 1, 18)); // NOI18N
        readCommandField.setForeground(new Color(255, 255, 255));
        readCommandField.setEditable(false);
        readCommandField.setBackground(new Color(0, 51, 204));

        javax.swing.GroupLayout commandPaneLayout = new javax.swing.GroupLayout(commandPane);


        commandPane.setLayout(commandPaneLayout);
        commandPaneLayout.setHorizontalGroup(
                commandPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 500, Short.MAX_VALUE)
        );
        commandPaneLayout.setVerticalGroup(
                commandPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 625, Short.MAX_VALUE)
        );

        scrollPane = new JScrollPane(instPane);
        scrollPane.setAutoscrolls(true);
        javax.swing.GroupLayout instPaneLayout = new javax.swing.GroupLayout(instPane);
        instPane.setLayout(instPaneLayout);
        scrollPane.setMaximumSize(new Dimension(430, 625));
        scrollPane.setPreferredSize(new Dimension(430, 625));
        scrollPane.setMinimumSize(new Dimension(430, 625));

        instPaneLayout.setHorizontalGroup(
                instPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 430, Short.MAX_VALUE)
        );
        instPaneLayout.setVerticalGroup(
                instPaneLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGap(0, 625, Short.MAX_VALUE)

        );

        saveAlgBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAlgBtnActionPerformed();
            }
        });
        //saveAlgBtn.setSize(100, 100);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(commandPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 354, Short.MAX_VALUE)
                                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(saveAlgBtn)
                                .addGap(86, 86, 86))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(commandPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(saveAlgBtn)
                                .addContainerGap(41, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("РЎС‚РІРѕСЂРёС‚Рё Р°Р»РіРѕСЂРёС‚Рј", jPanel1);

        jScrollPane1.setViewportView(programList);

        // jScrollPane1.setS
        programList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                programListValueChanged(evt);
            }
        });


        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 373, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(74, 74, 74)
                                .addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane2)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(updateBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jTabbedPane1.addTab("РџРµСЂРµРіР»СЏРЅСѓС‚Рё РІРјС–СЃС‚", jPanel2);

        // readCommandField.setColumns(20);
        //readCommandField.setRows(5);
        jScrollPane2.setViewportView(readCommandField);
        jPanel2.setBackground(new Color(0, 0, 102));

        programList.setBackground(new Color(0, 51, 204));
        programList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                programListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(programList);

        updateBtn.setBackground(new Color(0, 51, 204));
        updateBtn.setFont(new Font("Times New Roman", 1, 18)); // NOI18N

        updateBtn.setText("РћР±РЅРѕРІРёС‚Рё");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed();
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane1)
        );

        pack();

    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AlgorithmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AlgorithmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AlgorithmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AlgorithmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AlgorithmMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel commandPane;
    private JScrollPane scrollPane;
    //private JScrollPane scrollPaneForCommand;
    private javax.swing.JPanel instPane;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList<String> programList;
    private javax.swing.JTextArea readCommandField;
    private javax.swing.JButton saveAlgBtn;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
