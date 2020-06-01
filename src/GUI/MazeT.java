package GUI;

import com.mysql.cj.xdevapi.JsonArray;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class MazeT {

    public static int Width = 0, Height = 0, RandomSeed = 0;
    static int MaxMazeSize = 100;
    public static boolean[][] result;
    static String file;
    static JsonArray json = new JsonArray();
    static ArrayList<String> list = new ArrayList<String>();

    public static void Generate() throws IOException {
        int k = 1;
        int q =1;
        do {
            try {
                String in = JOptionPane.showInputDialog(null, "������ ������", "�����������", JOptionPane.QUESTION_MESSAGE);
                if (in != null && in.trim().length() != 0 && Integer.parseInt(in) > 2 && Integer.parseInt(in) <= MaxMazeSize) {
                    Width = ((Integer.parseInt(in) - 1) / 2);
                    do {
                        try{
                        in = JOptionPane.showInputDialog(null, "������ ������", "�����������", JOptionPane.QUESTION_MESSAGE);
                        if(in != null && in.trim().length() != 0 && Integer.parseInt(in) > 2 && Integer.parseInt(in) <= MaxMazeSize)
                        {
                        Height = ((Integer.parseInt(in) - 1) / 2);
                        int a = (int) (2 + Math.random() * Width);
                        RandomSeed = a;
                        q=0;
                        k = 0;
                        }else if (in == null) {
                            q = 0;
                            k = 0;
                } else {
                    JOptionPane.showMessageDialog(null, "������� �����, �������� �������� 3, ����������� 100");
                }
                    }
                    catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "����������� ������ ���, �������� ��� ������");
                    }
                    } while (q > 0);
                } else if (in == null) {
                    k = 0;
                } else {
                    JOptionPane.showMessageDialog(null, "������� �����, �������� �������� 3, ����������� 100");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "����������� ������ ���, �������� ��� ������");
            }
        } while (k > 0);
        System.out.println("�����");
        if (Width == 0 || Height == 0 || RandomSeed == 0) {

        } else {
            Generator g = new Generator(Width, Height, RandomSeed);
            result = g.getMazeResult();
            Width = 0;
            Height = 0;
            RandomSeed = 0;
            for (int j = 0; j < result[0].length; j++) {
                for (int i = 0; i < result.length; i++) {
                    if (result[i][j]) {
                        list.add("0");
                    } else {
                        list.add("1");
                    }
                }
                list.add("\n");
            }
            DB d = new DB();

            int c = 1;
            do {
                String in = JOptionPane.showInputDialog(null, "������ �����", "����������", JOptionPane.QUESTION_MESSAGE);
                d.GetMaze(in);
                System.out.println(d.getNameMaze());
                if (in != null && in.trim().length() != 0 && d.getNameMaze() == null) {
                   
                    System.out.println(list.toString());
                    d.insertMaze(in,list);
                    c = 0;
                    Width = 0;
                    Height = 0;
                    RandomSeed = 0;
                    JOptionPane.showMessageDialog(null, "������", "�����������", JOptionPane.INFORMATION_MESSAGE);
                } else if (in == null) {
                    c = 0;
                    Width = 0;
                    Height = 0;
                    RandomSeed = 0;
                    d.setNameMaze(null);
                } else if (in.trim().length() <= 0) {
                    JOptionPane.showMessageDialog(null, "������ ����� ��������");
                } else if (d.getNameMaze() != null) {
                    JOptionPane.showMessageDialog(null, "������� � ����� ������ ��� ����, �������� ������.");
                    d.setNameMaze(null);
                }

            } while (c > 0);
            list.clear();

        }
    }
}
