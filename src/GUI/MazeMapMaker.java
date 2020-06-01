package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MazeMapMaker extends JFrame{
   public static int rows ;
   public static int columns ;
   int panelSize = 25;
   static int map[][];
   public  ArrayList<String> mapList = new ArrayList<String>();
   public static int level = 0;
   public static boolean levelsExistAlready = false;
    public static String choice = "";
    static ArrayList<String> list = new ArrayList<String>();
    JPanel pnl = new JPanel();
    DB d = new DB();
    public MazeMapMaker(String name) throws IOException{
        File IconForm = new File("Photo/labyrinth.png");
        try {

            this.setIconImage(ImageIO.read(IconForm));
        } catch (IOException ex) {
            Logger.getLogger(Authorization.class.getName()).log(Level.SEVERE, null, ex);
        }
        choice = name;
        {
    	if(level != -1){
                //map = new int[rows][columns];
	        loadMap(choice);
                if (rows > 40) {
                    setExtendedState(MAXIMIZED_BOTH);
                 }
	        this.setSize((columns*panelSize)+50, (rows*panelSize)+70);
	        this.setTitle("Редактор");
                //нове
                this.setLayout(new BorderLayout());
                GridLayout gd=new GridLayout(rows, columns, 1, 1);
                pnl.setLayout(gd);
	        ////
	        this.addWindowListener(new WindowAdapter(){
	            public void windowClosing(WindowEvent e) {
                        try {
                            saveMap();
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(MazeMapMaker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                         rows = 0;
                         columns = 0;
	                MainMenu m = new MainMenu();
                        m.setVisible(true);
	            }
	        });
	        
	        this.setLocationRelativeTo(null);
	        
	        for(int y = 0; y < rows; y++){
	            for(int x = 0; x < columns; x++){
	            	MapMakerTile tile = new MapMakerTile(x, y);
                        tile.setToolTipText("<HTML>ЛКМ - забрати перешкоду<br>ПКМ - додати перешкоду");
	                tile.setSize(panelSize-1, panelSize-1);
	                tile.setLocation((x*panelSize)+23, (y*panelSize)+25);
	                if(map[x][y] == 0){
	                    tile.setBackground(Color.GRAY);
	                }else{
	                    tile.setBackground(Color.WHITE);
	                }
                       tile.setVisible(true);
                        pnl.add(tile, BorderLayout.CENTER);
	            }
	        }
	        this.setVisible(true);
                //нове
                this.add(pnl);
                pnl.setVisible(true);
                //
    	}else{
                rows = 0;
                columns = 0;
               MainMenu m = new MainMenu();
               m.setVisible(true);
                
    	}
    }
}
    
// //   public void getMapList(){
//        File folder = new File("Level\\");
//       File[] listOfFiles = folder.listFiles();
//       for(File file : listOfFiles)
//       {
//           if(file.isFile()||file.getName().endsWith(".map"))
//           {
//               mapList.add(file.getName());
//               levelsExistAlready = true;
//           }
//       }
//    }
    
      String getLevelChoice(){
         mapList.clear();
        d.GetInfoBox("name_maze","maze_info");
        for(int i=0;i<d.list.size();i++)
        {
            mapList.add(d.array[i]);
        }
    	if(mapList.size()!=0){
            String maps[] = new String[mapList.size()+20];
            System.out.println(mapList.size());
	    	mapList.toArray(maps);
                choice = null;
	    	choice = (String)JOptionPane.showInputDialog(null, "Оберіть карту", "Доступні карти", JOptionPane.QUESTION_MESSAGE, null, maps, maps[0]);
	    	System.out.println(choice);
	    	if(choice != null){
                    return choice;
	    	}
                else if(choice == null){
                      return null;
                }
    	}
    return null;
 }
    
    public void saveMap() throws URISyntaxException{
       try{
        for (int j = 0; j < rows; j++) {
                for (int i = 0; i < columns; i++) {
                    if (map[i][j]==0) {
                        list.add("0");
                    } else {
                        list.add("1");
                    }
                }
                list.add("\n");
            }
        DB d = new DB();
        d.editMaze(d.getIdMaze(), list);
        list.clear();
        }
       catch(Exception ex)
       {
       }
        
    }
    
    public void loadMap(String str){try {
            DB d = new DB();
            d.GetMaze(str);
            String mapStr = d.getInfoMaze();
            int count = 0;
            int c = 1;
            for (char element : mapStr.toCharArray()) {
                if (element == '0' || element == '1') {
                    count++;
                }
                else if(element != '0' && element != '1'){
                    break;
                }
            }
            for (char element : mapStr.toCharArray()) {
                if (element == '\n') {
                    c++;
                }
                else
                {}
            }
            rows = c;
            columns = count;
            System.out.println("Рядок: "+rows+"  Стовпчик: "+columns);
            //columns = (int) Math.sqrt(count);
            //columns=23;
            map = new int[columns][rows];
            System.out.println("Розмір " + count);
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
}

