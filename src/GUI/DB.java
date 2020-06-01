/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Master
 */
public class DB {

    /**
     * @return the nameMaze
     */
    public static String getNameMaze() {
        return nameMaze;
    }

    /**
     * @param aNameMaze the nameMaze to set
     */
    public static void setNameMaze(String aNameMaze) {
        nameMaze = aNameMaze;
    }

    /**
     * @return the idMaze
     */
    public static int getIdMaze() {
        return idMaze;
    }

    /**
     * @param aIdMaze the idMaze to set
     */
    public static void setIdMaze(int aIdMaze) {
        idMaze = aIdMaze;
    }

    

    /**
     * @return the infoMaze
     */
    public static String getInfoMaze() {
        return infoMaze;
    }

    /**
     * @param aInfoMaze the infoMaze to set
     */
    public static void setInfoMaze(String aInfoMaze) {
        infoMaze = aInfoMaze;
    }

    

    /**
     * @return the array
     */
    public static String[] getArray() {
        return array;
    }
    
private String tbl="users";
private Statement s=null;
Open o = new Open();
private String addName = "Такого нема";
public ArrayList<String> list = new ArrayList<>();
public static String[] array;
private static String infoMaze = "";
private static int idMaze;
private static String nameMaze;
    /**
     * @return the addName
     */
    public String getAddName() {
        return addName;
    }

    /**
     * @param addName the addName to set
     */
    public void setAddName(String addName) {
        this.addName = addName;
    }

    /**
     * @return the tbl_users
     */
    public String getTbl_users() {
        return tbl_users;
    }
    private String server=o.getServer();
    private String port=o.getPort();
    private String user=o.getUser();
    private String pass=o.getPass();
    private String dbname=o.getDbname();
    private String charset="utf8";
    private String timezone="UTC";
    private String tbl_users="users";
    
    private Connection conn=null;
    /**
     * Підключення
     * @return conn З'єднання
     */
    public Connection Connect(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(this.user);
        dataSource.setPassword(this.pass);
        dataSource.setServerName(this.server);     
        dataSource.setDatabaseName(this.dbname);
        dataSource.setPort(Integer.parseInt(this.port)); 
        try {
            dataSource.setServerTimezone(this.timezone);
        } catch (SQLException ex) {
            System.out.println("Err on setting Timezone :\n"+ex.toString());
        }
        try {
            dataSource.setCharacterEncoding(this.charset);
        } catch (SQLException ex) {
            System.out.println("Err on setting CP :\n"+ex.toString());
        }
        try {
            conn=dataSource.getConnection();
            return conn;
        } catch (SQLException ex) {
            conn=null;
            System.out.println("Err on getting connection :\n"+ex.toString());
        }
        return conn;
    }

    public Connection getCon(){
        if(this.conn!=null){
            return this.conn;
        }else{
            return Connect();
        }
    }   
    
    //Вкрадене
    
    public Statement getStatement(){
        try {
            return (Statement) conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
   


    /**
     * Реєстрація
     * @param pib Ініціали
     * @param login Логін
     * @param password Пароль
     */
    public void insert(String pib, String login, String password){
        try{
            Connect();
            String mySQLquery="";
                  mySQLquery=
                    "INSERT INTO `"+ tbl +"` " +
                    "(`PIB`, `login`, `password`) " +
                    "VALUES (?, ?, ?);"
                  ;                  
                PreparedStatement X=(PreparedStatement) conn.prepareStatement(mySQLquery);
                
                X.setString(1, pib);
                X.setString(2, login);
                X.setString(3, password);
            
            X.execute();
        }
        catch (Exception e){
            System.out.println(e.fillInStackTrace());   
        }
    }
   
    /**
     * 
     * @param name Ім'я полігону
     * @param l Полігон
     */
    public void insertMaze(String name, ArrayList<String> l){
        try{
            Connect();
            String mySQLquery="";
                  mySQLquery=
                    "INSERT INTO `maze_info` " +
                    "(`name_maze`, `map_maze`) " +
                    "VALUES (?, ?);"
                  ;                  
                PreparedStatement X=(PreparedStatement) conn.prepareStatement(mySQLquery);
                X.setString(1, name);
                X.setString(2, toStr(l));
            
            X.execute();
        }
        catch (Exception e){
            System.out.println(e.fillInStackTrace());   
        }
    }
    
  //писати
  public static String toStr(ArrayList<String> data){
        String z="";
        if (data.isEmpty())return "";
        for(String x:data){
            z=z+x+"";
        }
        z=z.substring(0, z.length()-1);
        return z;
    }
   public ResultSet GetMaze(String name) {
        Connect();
        ResultSet Xrez = null;
        try {
            if (s == null) {
                s = (Statement) conn.createStatement();
            }
            Xrez = s.executeQuery("SELECT map_maze, id_maze, name_maze"+" From maze_info"+ " WHERE name_maze ='"+name+"'" );
            while(Xrez.next()){
              String maze =  Xrez.getString(1);
              int idM = Xrez.getInt(2);
              String nameM = Xrez.getString(3);
                infoMaze=maze;
                idMaze=idM;
                nameMaze=nameM;
            }
            return Xrez;
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return Xrez;
        }
     }

    /**
     * Оновлення лабіринту
     * @param id Код лабіринту
     * @param l Лабіринт для запису
     */
    
    public void editMaze(int id, ArrayList<String> l ){
        try{
            Connect();
            String mySQLquery="";
                  mySQLquery=
                        "UPDATE `maze_info` " +
                       " SET map_maze=? WHERE id_maze ="+id;
                      ; 
                PreparedStatement X=(PreparedStatement) conn.prepareStatement(mySQLquery);
                
                X.setString(1, toStr(l));
            
            X.execute();
        }
        catch (Exception e){
            System.out.println(e.fillInStackTrace());   
        }
    }
    @Override
    protected void finalize(){
        if (conn != null){
            try
            {
                conn.close ();
            }
            catch (Exception e) { }
        }
    }
    
    /**
     * Отримання інформації про когось
     * @param nameRow Ім'я стовпчика
     * @param nameTbl Ім'я таблиці
     * @param txt Текст
     * @return Xrez Результат
     */
    public ResultSet GetInfo(String nameRow, String nameTbl, String txt) {
        Connect();
        ResultSet Xrez = null;
        try {
            if (s == null) {
                s = (Statement) conn.createStatement();
            }
            Xrez = s.executeQuery("SELECT "+nameRow+" From " +nameTbl+ " WHERE " + nameRow+ "=" + "'"+txt+"'");
            while(Xrez.next()){
               String name =  Xrez.getString(1);
               setAddName(name);
            }
            return Xrez;
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return Xrez;
        }
        
    }

    
     /**
      * Метод для заповнення полігонів
      * @param nameRow Ім'я стовпчика
      * @param nameTbl Ім'я таблиці
      * @param txt Текс
      * @return Xrez Реультат
      */
    public ResultSet GetInfoBox(String nameRow, String nameTbl){
        Connect();
        list.clear();
        array = null;
        ResultSet Xrez = null;
        try {
            if (s == null) {
                s = (Statement) conn.createStatement();
            }
            Xrez = s.executeQuery("SELECT "+nameRow+" FROM " +nameTbl);
            while(Xrez.next()){
               String name =  Xrez.getString(1);
               
               list.add(name);
            }
            array = list.toArray(new String[0]);
            return Xrez;
            
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
            return Xrez;
        }
      
 }
    public void del(String name){
        Connect();
        String mySQLquery="";
        try{                  
                  mySQLquery=
                    "DELETE FROM `maze_info` " +
                    "WHERE `name_maze`= '" + name +"';"
                  ;
            conn.prepareStatement(mySQLquery);                     
            PreparedStatement X=(PreparedStatement) conn.prepareStatement(mySQLquery);
            X.execute();
        }
        catch (Exception e){
            System.out.println(e.fillInStackTrace()+"\n"+mySQLquery);
        }
    }
}


