package GUI;

import bot.Bot;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.awt.image.ImageObserver.HEIGHT;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.stream.Stream;

import javafx.geometry.Side;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Maze extends JFrame {

    /**
     * @return the botAlg
     */
    public static String getBotAlg(int c) {
        return botAlg[c];
    }

    /**
     * @param botAlg the botAlg to set
     */
    public static void setBotAlg (String text) {
      // programArrayList.add(text);
    }

    /**
     * @return the botArray
     */
    // public static Bot[] getBotArray() {
    //   return botArray;
    //}

    /**
     * @param aBotArray the botArray to set
     */
    //  public static void setBotArray(Bot[] aBotArray) {
    //     botArray = aBotArray;
    /// }

    /**
     * @return the botAlg
     */


    /**
     * @return the howDo
     */
    public static int getHowDo() {
        return howDo;
    }

    /**
     * @param aHowDo the howDo to set
     */
    public static void setHowDo(int aHowDo) {
        howDo = aHowDo;
    }

    /**
     * @return the botCount
     */
    public static int getBotCount() {
        return botCount;
    }

    /**
     * @param aBotCount the botCount to set
     */
    public static void setBotCount(int aBotCount) {
        botCount = aBotCount;
    }

    private static int botCount = 0;
    File IconForm = new File("Photo/labyrinth.png");
    public static int rows;
    private static int howDo;
    public static int columns;
    public static int panelSize = 25;
    public static int map[][];
    public static int endLevelLoc;
    MainMenu m = new MainMenu();
    JButton startBtn = new JButton();
    JButton stopBtn = new JButton();
    public Thread[] threadArr;
    private static String[] botAlg = new String[8];

    public static JPanel pnl = new JPanel();
    JPanel play;
    public static JPanel pnlMaks = new JPanel();
    public Button moveBottomBtn = new Button();
    public Color[] botAvailableColors = {Color.BLUE, Color.RED, Color.YELLOW, Color.ORANGE, Color.GREEN, Color.PINK, Color.MAGENTA, Color.BLACK};
    /////////////////
    int c;
    int z;
    int num = 0;
    public Bot[] botArray;
    public Maze(String str) throws IOException {
        loadMap(str);
        // this.setResizable(false);
        this.setSize((rows * panelSize) + 50, (columns * panelSize) + 70);
        this.setTitle("Лабіринт");
        //this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.NORMAL);
        this.setIconImage(ImageIO.read(IconForm));
        //пїЅпїЅпїЅпїЅ
        this.setLayout(new BorderLayout());
        GridLayout gd = new GridLayout();
        gd.setRows(rows);
        gd.setColumns(columns);
        pnl.setLayout(gd);
        if (columns > rows) {
            this.setSize(1000, 600);
        }
        if (rows > 40) {
            setExtendedState(MAXIMIZED_BOTH);
        }
        //////////////////////////////////
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                String[] btn = {"Так", "Ні"};
                int a = JOptionPane.showOptionDialog(rootPane, "Ви впевнені?", "Вихід",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, btn, "OK");
                if (a == JOptionPane.YES_OPTION || a == 0) {
                    DB d = new DB();
                    dispose();
                    columns = 0;
                    rows = 0;
                    pnl.removeAll();
                    pnlMaks.removeAll();
                    for (Thread botThread : threadArr) {
                        if (botThread != null) botThread.stop();
                    }
                    m.setVisible(true);
                } else {

                }
            }
        });
        if (getHowDo() == 0) {
            this.addKeyListener(new KeyListener() {

                @Override
                public void keyPressed(KeyEvent e) {
                    int key = e.getKeyCode();
                    revalidate();
                    repaint();
                    //Player movement
                    if (key == KeyEvent.VK_W) {
                     //   System.out.println("пїЅпїЅпїЅпїЅпїЅ");
                      //  System.out.println(z);
                        z = z - 1;
                        System.out.println(z);
                        if (z == -1) {
                            z = z + 1;
                        }
                        if (map[c][z] != 0) {
                            setBoth(c, z + 1);
                            System.out.println(z);
                        } else {
                            z = z + 1;
                        }
                    }
                    if (key == KeyEvent.VK_A) {
                        c = c - 1;
                        if (map[c][z] != 0) {
                       //     System.out.println("пїЅпїЅпїЅпїЅ");
                            setBoth(c, z + 1);
                        } else {
                            c = c + 1;
                        }
                    }
                    if (key == KeyEvent.VK_S) {
                      //  System.out.println("пїЅпїЅпїЅпїЅ");
                        z = z + 1;
                        if (map[c][z] != 0) {
                            setBoth(c, z + 1);
                        } else {
                            z = z - 1;
                        }
                    }
                    if (key == KeyEvent.VK_D) {
                        c = c + 1;
                        if (map[c][z] != 0) {
                      //      System.out.println("пїЅпїЅпїЅпїЅпїЅпїЅ");
                            setBoth(c, z + 1);
                        } else {
                            c = c - 1;
                        }

                    }
                    if (z == rows - 1) {
                        JOptionPane.showMessageDialog(null, "Рівень пройдено!", "Повідомлення", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        columns = 0;
                        rows = 0;
                        z = 0;
                        c = 0;
                        play = null;
                        pnl.removeAll();
                        pnlMaks.removeAll();
                        m.setVisible(true);
                    }
                }

                @Override
                public void keyReleased(KeyEvent arg0) {

                }

                @Override
                public void keyTyped(KeyEvent arg0) {

                }

            });
        } else {

        }
        this.setLocationRelativeTo(null);
        //Color map
       // System.out.println("пїЅпїЅпїЅпїЅпїЅ " + rows + " пїЅпїЅпїЅпїЅпїЅпїЅпїЅ " + columns);
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Tile tile = new Tile(x, y);
                tile.setSize(panelSize, panelSize);
                tile.setLocation((x * panelSize) + 23, (y * panelSize) + 25);
                if (map[x][y] == 0) {
                    tile.setBackground(Color.GRAY);
                } else {
                    tile.setBackground(Color.WHITE);
                    tile.setWall(false);
                    if (y == 0) {
                        c = x;
                        z = y;
                    }
                    if (y == columns - 1) {
                        endLevelLoc = x;
                    }
                }
                tile.setVisible(true);
                pnl.add(tile, BorderLayout.CENTER);
                ///
            }
        }
        //пїЅпїЅпїЅпїЅ
        this.add(pnl);

      //  System.out.println("КіпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ пїЅпїЅпїЅпїЅпїЅ " + getBotCount());
        threadArr = new Thread[getBotCount()];


        if (getHowDo() == 0) {
            setBoth(c, z);
        } else {
            //  createBot(c, z);
            pnlMaks.add(createStart());
            pnlMaks.add(createStop());
            pnlMaks.setPreferredSize(new Dimension(200, HEIGHT));
            pnlMaks.setBackground(Color.BLUE);
            pnlMaks.setEnabled(false);
            this.add(pnlMaks, BorderLayout.EAST);
            this.add(pnl, BorderLayout.CENTER);
        }
        botArray = createBotArray(getBotCount(), c, z);

        this.setVisible(true);
        this.setFocusable(true);
    }


    public JButton createStart() {
        startBtn.setLabel("start");
        startBtn.setSize(20, 30);
        startBtn.addActionListener((ActionEvent e) -> {
            for (Thread botThread : threadArr) {
                if (!botThread.isAlive() || botThread.isInterrupted()) botThread.start();
            }

            this.requestFocusInWindow();
        });
        return startBtn;
    }

    public JButton createStop() {
        stopBtn.setLabel("stop");
        stopBtn.setSize(20, 30);
        stopBtn.addActionListener((ActionEvent e) -> {
            for (Thread botThread : threadArr) {

                if (botThread.isAlive())
                    botThread.stop();
            }
            this.requestFocusInWindow();
        });
        return stopBtn;
    }

    public Bot[] createBotArray(int arraySize, int cordX, int cordY) {
        Bot[] botArray = new Bot[arraySize];
        threadArr = new Thread[arraySize];


        for (int i = 0; i < botArray.length; i++) {
            System.out.println(BotCount.programList.get(i));
            botArray[i] = new Bot(botAvailableColors[i],BotCount.programList.get(i));
            threadArr[i] = new Thread(botArray[i]);
            botArray[i].setCordX(cordX);
            botArray[i].setCordY(cordY);
            botArray[i].setBot();

        }
        return botArray;
    }



    public static void main(String args[]) throws URISyntaxException, IOException {
        new MainMenu();
    }

    public void setBoth(int x, int y) {
        if (y == 0) {
            num = x;
        } else {
            play.setBackground(Color.WHITE);
            num = (x) + (y - 1) * columns;
            play = (JPanel) pnl.getComponent(x);
        }
       // System.out.println("пїЅпїЅпїЅпїЅпїЅпїЅпїЅ" + num);
        play = (JPanel) pnl.getComponent(num);
        play.setBackground(new Color(255, 20, 147));
        play.setVisible(true);

    }

    Maze() {
        throw new UnsupportedOperationException("Не підтримується"); //To change body of generated methods, choose Tools | Templates.
    }

    public void loadMap(String str) {
        try {
            DB d = new DB();
            d.GetMaze(str);
            String mapStr = d.getInfoMaze();
            int count = 0;
            int c = 1;
            for (char element : mapStr.toCharArray()) {
                if (element == '0' || element == '1') {
                    count++;
                } else if (element != '0' && element != '1') {
                    break;
                }
            }
            for (char element : mapStr.toCharArray()) {
                if (element == '\n') {
                    c++;
                } else {
                }
            }
            rows = c;
            columns = count;
            //System.out.println("пїЅпїЅпїЅпїЅпїЅ: " + rows + "  пїЅпїЅпїЅпїЅпїЅпїЅпїЅпїЅ: " + columns);
            //columns = (int) Math.sqrt(count);
            //columns=23;
            map = new int[columns][rows];
            System.out.println("пїЅпїЅпїЅпїЅпїЅ " + count);
            int counter = 0;
            for (int y = 0; y < rows; y++) {
                for (int x = 0; x < columns; x++) {
                    String mapChar = mapStr.substring(counter, counter + 1);
                    if (!mapChar.equals("\r\n") && !mapChar.equals("\n") && !mapChar.equals("\r")) {
                        map[x][y] = Integer.parseInt(mapChar);
                    } else {
                        x--;
                    }
                    counter++;
                }

            }
        } catch (Exception e) {
            System.out.println("Неможливо завантажити карту, створіть нову.");
        }
    }

    public Button createMoveBottomBtn() {
        moveBottomBtn.setLabel("move bottom");
        moveBottomBtn.setSize(20, 30);
        moveBottomBtn.addActionListener((ActionEvent e) -> {
//           System.out.println("пїЅпїЅпїЅ 1: " + bot1.getNowCordY());
            this.requestFocusInWindow();

        });
        return moveBottomBtn;
    }
//
//    ////////////////////////top move
//    public Button createMoveTopBtn() {
//        moveTopBtn.setLabel("move top");
//        moveTopBtn.setSize(20, 30);
//        moveTopBtn.addActionListener((ActionEvent e) -> {
//            bot.moveAt(Side.TOP);
//
//            this.requestFocusInWindow();
//        });
//        return moveTopBtn;
//    }
//
//    ///////////////////////left move
//    public Button createMoveLeftBtn() {
//        moveLeftBtn.setLabel("move left");
//        moveLeftBtn.setSize(20, 30);
//        moveLeftBtn.addActionListener((ActionEvent e) -> {
//            bot.moveAt(Side.LEFT);
//
//            this.requestFocusInWindow();
//        });
//        return moveLeftBtn;
//    }
//
//    //////////////////////////right move
//    public Button createMoveRightBtn() {
//        moveRightBtn.setLabel("move right");
//        moveRightBtn.setSize(20, 30);
//        moveRightBtn.addActionListener((ActionEvent e) -> {
//            bot.moveAt(Side.RIGHT);
//
//            this.requestFocusInWindow();
//        });
//        return moveRightBtn;
//    }
//    ///////////////////////////
//
//    public Button createLookBtn() {
//        lookBtn.setLabel("look at bottom");
//        lookBtn.setSize(20, 30);
//        lookBtn.addActionListener((ActionEvent e) -> {
//            System.out.println(bot.lookAt(Side.BOTTOM));
//            this.requestFocusInWindow();
//        });
//        return lookBtn;
//    }
//
//    //////////////////////////////
//    public Button createCurCordBtn() {
//
//        curCordBtn.setLabel("current cords");
//        curCordBtn.setSize(20, 30);
//        curCordBtn.addActionListener((ActionEvent e) -> {
//            bot.getCurCords();
//
//            this.requestFocusInWindow();
//        });
//
//        return curCordBtn;
//    }
//
//    public Button createPushCordsBtn() {
//
//        pushCordsBtn.setLabel("push cords");
//        pushCordsBtn.setSize(20, 30);
//        pushCordsBtn.addActionListener((ActionEvent e) -> {
//            bot.pushIntoStack();
//            stackCordsArea.setText("");
//            for (int i = 0; i < bot.stackOb.size(); i++) {
//                stackCordsArea.setText(bot.stackOb.get(i) + "\n");
//            }
//
//            this.requestFocusInWindow();
//        });
//
//        return pushCordsBtn;
//    }
//
//    public Button createComeBackOneStepBtn() {
//
//        comeBackOneStepBtn.setLabel("come Back One Step ");
//        comeBackOneStepBtn.setSize(20, 30);
//        comeBackOneStepBtn.addActionListener((ActionEvent e) -> {
//
//            bot.comeBackOneStep(stackCordsArea);
//
//            this.requestFocusInWindow();
//        });
//
//        return comeBackOneStepBtn;
//    }
//
//    public Button createComeBackToStartBtn() {
//
//        comeBackToStartBtn.setLabel("come to start ");
//        comeBackToStartBtn.setSize(20, 30);
//        comeBackToStartBtn.addActionListener((ActionEvent e) -> {
//
//            bot.comeBackToStart();
//
//            this.requestFocusInWindow();
//        });
//
//        return comeBackToStartBtn;
//    }

//    public JScrollPane createStackCordsArea() {
//        jsp.setPreferredSize(new Dimension(180, 300));
//        jsp.setEnabled(false);
//        Font f = new Font("Courier", Font.PLAIN, 10);
//        stackCordsArea.setFont(f);
//        stackCordsArea.setEnabled(false);
//        return jsp;
//    }
}
