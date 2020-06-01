package bot;

import GUI.Maze;
import filework.FileWork;
import javafx.geometry.Side;
import javafx.scene.control.Alert;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BooleanSupplier;

import static GUI.Maze.columns;
import static GUI.Maze.pnl;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Bot implements Runnable {

    private int cordX;
    private int cordY;
    public Pair<Integer, Integer> memoryPair = new Pair<>(-1, -1);
    private Color colorBot;
    private String nameBot;
    private String programBot;
    public Stack<Pair<Integer, Integer>> cordsStack = new Stack<>();
    public Stack<Pair<Integer, Integer>> tempStack = new Stack<>();
    public final Map<Side, Pair<Integer, Integer>> sideNames = new HashMap<>();
    public Map<String, Function> availableCommands = new HashMap<>();
    public int nextCordX;
    public int nextCordY;
    public boolean isFinish = false;
    private int nextNumPanel, previousNumPanel;
    public ArrayList<Pair<String, String>> commandPairList = new ArrayList<>();
    private final FileWork fileWork = new FileWork();
    public ArrayList<Pair<String, Integer>> markPairList = new ArrayList<>();
    public int countCycle = 0;
    public boolean checkWall;
    public Map<String, Color> botColorMap = new HashMap<>();

    public Bot(Color colorBot, String programBot) {
        setColorBot(colorBot);
        setProgramBot(programBot);
        createPropSide();
        putFunctionIntoMap();
        getCommandFromFile();
        addColorsIntoMap();
    }

    /**
     * this method push cordX and cordY into memory pair
     *
     * @return memory pair<int,int>
     */
    public Pair<Integer, Integer> getCurCords() {
        return memoryPair = new Pair<>(getCordX(), getCordY());
    }

    /**
     * this method adds sides and their cords in sidesName map
     */
    public void createPropSide() {
        sideNames.put(Side.RIGHT, new Pair(1, 0));
        sideNames.put(Side.LEFT, new Pair(-1, 0));
        sideNames.put(Side.BOTTOM, new Pair(0, 1));
        sideNames.put(Side.TOP, new Pair(0, -1));
    }

    /**
     * this method looks at side and checks there is a wall or no
     *
     * @param nameSide side name where bot will move
     * @return true if there is no wall
     */
    public boolean lookAt(Side nameSide) {
        nextCordX = getCordX() + sideNames.get(nameSide).getKey();
        nextCordY = getCordY() + sideNames.get(nameSide).getValue();
        checkWall = true;

        if (Maze.map[nextCordX][nextCordY] != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * this method makes bot moving on the side
     *
     * @param nameSide side name where bot will move
     */
    public void moveAt(Side nameSide) {
        System.out.println(nameSide);
        nextCordX = getCordX() + sideNames.get(nameSide).getKey();
        nextCordY = getCordY() + sideNames.get(nameSide).getValue();
        if (!checkWall && Maze.map[nextCordX][nextCordY] == 0) {
            for (Map.Entry<String, Color> botColor : botColorMap.entrySet()) {
                if (botColor.getValue().equals(getColorBot().brighter())) {
                    JOptionPane.showMessageDialog(null, "Бот з "+botColor.getKey() +" кольором  в'їхав в стінку і розбився\nНаступного разу перевіряйте чи пусто\nперед переходом");
                    Thread.currentThread().stop();
                }
            }
        } else {
            redrawBot(nextCordX, nextCordY);
            updateCurCords();
            checkFinish(nameSide);
        }
    }

    /**
     * this method update current bot coordinate
     */
    public void updateCurCords() {
        setCordX(nextCordX);
        setCordY(nextCordY);
    }

    /**
     * this method makes bot redrawing
     *
     * @param x coordinate x, int
     * @param y coordinate y, int
     */
    public void redrawBot(int x, int y) {
        setNextAndPreviousNumbPanel(x, y);
        changePane(nextNumPanel, previousNumPanel);
    }

    /**
     * this method counts next and previous number of panels
     *
     * @param x coordinate X
     * @param y coordinate Y
     */
    public void setNextAndPreviousNumbPanel(int x, int y) {
        if (y == 0) {
            this.nextNumPanel = x;
            previousNumPanel = getCordX() + columns;
        } else {
            this.nextNumPanel = (x) + y * columns;
            previousNumPanel = getCordX() + getCordY() * columns;
        }
    }

    /**
     * this method returns bot one step, cords for return he takes from bot`s
     * memory and after moving those cords deletes from bot`s memory
     */
    public void comeBackOneStep() {

        if (!cordsStack.isEmpty()) {
            nextCordX = cordsStack.peek().getKey();
            nextCordY = cordsStack.peek().getValue();
            redrawBot(nextCordX, nextCordY);
            updateCurCords();
            if (cordsStack.size() >= 1) {
                cordsStack.pop();
            }
        }
    }

    /**
     * this method sets bot in maze
     */
    public void setBot() {
        setNextAndPreviousNumbPanel(getCordX(), getCordY());
        changePane(nextNumPanel, previousNumPanel);

    }

    /**
     * this method changes color for next and previous panels
     *
     * @param nextNumPanel number of the next panel
     * @param previousNumPanel number of the previous panel
     */
    public void changePane(int nextNumPanel, int previousNumPanel) {
        JPanel nextPanel, previousPanel;

        previousPanel = (JPanel) pnl.getComponent(previousNumPanel);
        previousPanel.setBackground(Color.WHITE);
        previousPanel.setVisible(true);

        nextPanel = (JPanel) pnl.getComponent(nextNumPanel);
        nextPanel.setBackground(getColorBot());
        nextPanel.setVisible(true);
    }

    /**
     * this method checks to be or not to be the next bot`s cords the finish
     *
     * @param nameSide side name where bot will move
     */
    public void checkFinish(Side nameSide) {
        
        if (getCordY() + sideNames.get(nameSide).getValue() == Maze.rows) {
            isFinish = true;
            for (Map.Entry<String, Color> botColor : botColorMap.entrySet()) {
                if (botColor.getValue().equals(getColorBot().brighter())) {
                    JOptionPane.showMessageDialog(null, "Бот з " + botColor.getKey() + " кольором прийшов до фінішу першим");
                }
            }
        }
    }

    /**
     * this method put color name in Ukrainian and his color in Java
     */
    public void addColorsIntoMap() {
        botColorMap.put("синім", Color.BLUE);
        botColorMap.put("червоним", Color.RED);
        botColorMap.put("жовтим", Color.YELLOW);
        botColorMap.put("оранджевим", Color.ORANGE);
        botColorMap.put("зеленим", Color.GREEN);
        botColorMap.put("рожевим", Color.PINK);
        botColorMap.put("магнета", Color.MAGENTA);
        botColorMap.put("чорним", Color.BLACK);
    }

    /**
     * this method push coordinate from memory bot into stack
     *
     * @param nameStack name of the stack
     */
    public void pushIntoStack(Stack<Pair<Integer, Integer>> nameStack) {
        Pair<Integer, Integer> pair = memoryPair;
        nameStack.push(pair);
    }

    /**
     * this method starts bot`s thread
     */
    public void run() {
        if (isFinish) {
            Thread.currentThread().stop();
        }
        while (!isFinish) {
            startProgram();
        }
    }

    /**
     * this method creates rest for bot`s thread
     *
     * @param restTime time in milliseconds
     */
    public void rest(int restTime) {
        try {
            Thread.sleep(restTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method creates start program
     */
    public void startProgram() {

        for (countCycle = 0; countCycle < commandPairList.size(); countCycle++) {
            Pair<String, String> commandPair = commandPairList.get(countCycle);
            for (Map.Entry<String, Function> pair : availableCommands.entrySet()) {
                if (pair.getKey().equals(commandPair.getKey())) {
                    pair.getValue().doSomething(commandPair.getValue());
                    rest(500);

                } else if (commandPair.getKey().contains("mark")) {
                    checkExistAndAddMarkData(commandPair.getKey());
                }
                if (isFinish) {
                    Thread.currentThread().stop();
                }
            }
        }
    }

    /**
     * this method checks to exist mark data and adds her into markList
     *
     * @param nameMark mark name in String
     */
    public void checkExistAndAddMarkData(String nameMark) {
        if (markPairList.stream().noneMatch(markPair -> markPair.getKey().equals(nameMark))) {
            markPairList.add(new Pair<>(nameMark, countCycle));
        }
    }

    /**
     * this method go to specific marks
     *
     * @param markName mark name in String
     */
    public void goTo(String markName) {
        for (Pair<String, Integer> markPair : markPairList) {
            if (markPair.getKey().equals(markName)) {
                this.countCycle = markPair.getValue();
            }
        }
    }

    /**
     * this method gets commands from bot program
     */
    public void getCommandFromFile() {
        commandPairList = fileWork.getCommandPairList("res/botPrograms/" + getProgramBot());
        for (Pair<String, String> s : commandPairList) {
            System.out.println(s);
        }
    }

    /**
     * FunctionalInterface of functions map
     */
    @FunctionalInterface
    private interface Function {

        void doSomething(Object data);
    }

    @SuppressWarnings("unchecked")
    /**
     * this method put name function and her reference
     */
    public void putFunctionIntoMap() {
        availableCommands.put("moveAt", data -> {
            Side side;
            if ("BOTTOM".equals(data)) {
                side = Side.BOTTOM;
            } else if ("LEFT".equals(data)) {
                side = Side.LEFT;
            } else if ("RIGHT".equals(data)) {
                side = Side.RIGHT;
            } else {
                side = Side.TOP;
            }
            moveAt(side);

        });
        availableCommands.put("getCurCords", data -> {
            Pair<Integer, Integer> curCords = getCurCords();
            System.out.println("Cur" + curCords);
        });
        availableCommands.put("comeBackOneStep", data -> comeBackOneStep());
        availableCommands.put("pushIntoStack", data -> {
            if ("stack1".equals(data)) {
                pushIntoStack(cordsStack);
            } else {
                pushIntoStack(tempStack);
            }

        });
        availableCommands.put("peekCordsStack", data -> {
            if ("stack1".equals(data)) {
                System.out.println("First stack " + peekCordsStack(cordsStack));
            } else {
                System.out.println("Second stack " + peekCordsStack(tempStack));
            }

        });
        availableCommands.put("popCordsStack", data -> {
            if ("stack1".equals(data)) {
                memoryPair = popCordsStack(cordsStack);
                System.out.println("First stack " + popCordsStack(cordsStack));
            } else {
                memoryPair = popCordsStack(tempStack);
                System.out.println("Second stack " + popCordsStack(tempStack));
            }
        });
        availableCommands.put("goTo", data -> {
            goTo(data.toString());
        });
        //availableCommands.put("lookAt", data -> {
        //
        //});

        
        availableCommands.put("if", data -> {
            System.out.println("\n" + data);
            String[] parts = ((String) data).split(",");
            String sideName = parts[0].substring(parts[0].indexOf('(') + 1, parts[0].indexOf(')'));
            Side side = Side.valueOf(sideName);
            Function firstCommand = availableCommands.get(parts[1]);
            Object firstCommandArgument = parts[2];
            Function secondCommand = availableCommands.get(parts[3]);
            Object secondCommandArgument = parts[4];
            if (lookAt(side)) {
                firstCommand.doSomething(firstCommandArgument);
            } else {
                secondCommand.doSomething(secondCommandArgument);
            }
        });

    }

    /**
     * this method peek coordinate from stack
     *
     * @param nameStack name of the stack
     * @return pair of coordinate
     */
    public Pair<Integer, Integer> peekCordsStack(Stack<Pair<Integer, Integer>> nameStack) {
        if (nameStack.isEmpty()) {
            return new Pair<>(-1, -1);
        }
        return nameStack.peek();
    }

    /**
     * this method deletes coordinate from stack
     *
     * @param nameStack name of the stack
     * @return pair of coordinate
     */
    public Pair<Integer, Integer> popCordsStack(Stack<Pair<Integer, Integer>> nameStack) {

        if (!nameStack.isEmpty()) {
            return nameStack.pop();
        }
        return new Pair<>(-1, -1);
    }

    /**
     * this method gets current bot cord X
     *
     * @return current bot cord X
     */
    public int getCordX() {
        return cordX;
    }

    /**
     * this method sets current bot cord X
     *
     * @param cordX int cord X
     */
    public void setCordX(int cordX) {
        this.cordX = cordX;
    }

    /**
     * this method gets current bot cord Y
     *
     * @return current bot cord Y
     */
    public int getCordY() {
        return cordY;
    }

    /**
     * this method sets current bot cord Y
     *
     * @param cordY int cord Y
     */
    public void setCordY(int cordY) {
        this.cordY = cordY;
    }

    /**
     * this method gets bot color
     *
     * @return bot color
     */
    public Color getColorBot() {
        return colorBot;
    }

    /**
     * this method sets bot color
     *
     * @param colorBot color of bot
     */
    public void setColorBot(Color colorBot) {
        this.colorBot = colorBot;
    }

    /**
     * this method gets bot name
     *
     * @return name bot in String
     */
    public String getNameBot() {
        return nameBot;
    }

    /**
     * this method sets bot name
     *
     * @param nameBot bot name in String
     */
    public void setNameBot(String nameBot) {
        this.nameBot = nameBot;
    }

    /**
     * this method gets bot program
     *
     * @return bot program
     */
    public String getProgramBot() {
        return programBot;
    }

    /**
     * this method sets bot program
     *
     * @param programBot bot program in String
     */
    public void setProgramBot(String programBot) {
        this.programBot = programBot;
    }
}
