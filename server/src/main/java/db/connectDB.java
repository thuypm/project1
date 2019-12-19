/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author MINH THUY
 */

public class connectDB {
     public Statement getConnect() {
          Statement stmt ;
  try {
//          try {
//                  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//              } catch (ClassNotFoundException ex) {
//                  Logger.getLogger(connectDB.class.getName()).log(Level.SEVERE, null, ex);
//              }
    String dbURL = "jdbc:sqlserver://localhost;databaseName=chatAplication;user=sa;password=kochodau";
    Connection conn  = DriverManager.getConnection(dbURL);
       stmt = conn.createStatement();
   } catch (SQLException ex) {
     System.err.println("Cannot connect database, " + ex);
     stmt = null;
   };
    return stmt;
 
  
  }
    
}
