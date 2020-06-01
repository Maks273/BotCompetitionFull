package filework;

import javafx.util.Pair;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class FileWork {
    public static BufferedReader in;


    /**
     * метод виводить вміст файлу на екран
     *
     * @param NameFile передає назву та розширення файлу
     */
    public void printFile(JTextArea textFiled,
                          String NameFile) {
        try {

            in = new BufferedReader(new InputStreamReader(new FileInputStream(NameFile)));

            while (in.ready()) {
                String s = in.readLine();
                textFiled.setText(textFiled.getText() + s + "\n");

            }

            in.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        } finally {

        }
    }


    public ArrayList<Pair<String, String>> getCommandPairList(String nameFile) {
        StringBuilder command = new StringBuilder();
        StringBuilder argCommand = new StringBuilder();
        ArrayList<Pair<String, String>> commandPairList = new ArrayList<>();
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(nameFile)));
            while (in.ready()) {
                String line = in.readLine();
                //System.out.println("line: " + line);
                command.setLength(0);
                argCommand.setLength(0);
                for (int i = 0; i < line.length(); i++) {
                    char symbol=line.charAt(i);
                    if(symbol == '(') {
                        for (int j = i+1; j < line.length(); j++) {
                            symbol = line.charAt(j);
                            if(symbol!=')') argCommand.append(symbol);
                            else if (j != line.length() - 1) argCommand.append(symbol);
                        }
                        break;
                    }
                    else {
                        command.append(symbol);
                    }
                }
                commandPairList.add(new Pair<>(command.toString(), argCommand.toString()));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return commandPairList;
    }

    public BufferedWriter out;

    /**
     * метод дописує у кінець файлу переданий текст, якщо файл не існує його
     * буде створено
     *
     * @param text
     * @param nameFile
     */
    public void addToFile(String text, String nameFile) {
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nameFile + ".txt", true)));

            out.append(text);
            out.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * метод перезаписує файл, якщо файл не існує його буде створено
     * <p>
     * //* @param X текст яким буде перезаписано файл
     *
     * @param NameFile передає назву та розширення файлу
     */
    public void rewriteFile(String data, String NameFile) {
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(NameFile)));
            out.write(data);
            out.newLine();
            out.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *this method create folder for resources and in her creates folder composers images
     */
    public  void createDir()
    {
        File resFolder=new File("res");
        if(!resFolder.exists())
        {
            resFolder.mkdir();
        }
        File folder=new File("res/botPrograms");
        if(!folder.exists())
        {
            folder.mkdir();
        }


    }


}
