/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;
import config.UserData;
import db.connectDB;
import java.sql.Statement;
import java.sql.ResultSet;
import config.*;

/**
 *
 * @author MINH THUY
 */

public class UserDAO {
        connectDB conn = new connectDB() ;
        Statement stmt = conn.getConnect();
      
      public  UserData getUser(String username)
      {
           UserData user = new UserData();
          try{
              ResultSet rs  = stmt.executeQuery("SELECT * FROM userInfo WHERE (username = '" + username +"')" );
              if(rs.next())
              {
                   user.setUsername(rs.getString("username").replaceAll(" ", ""));
                   user.setPassword(rs.getString("password").replaceAll(" ", ""));
     
              }
                  }
             catch(Exception e){
                    user= null;
                 System.out.println(e);
                 
             };
             return user;
         
               
};
           public int addUser(String username, String password)
      {
          try{
              int rs  = stmt.executeUpdate("INSERT INTO userInfo (username,password) VALUES('"
       +username +"','"  +password+"')");
              
             return rs;
          }
             catch(Exception e){
                 System.out.println(e);
                 return 0;
             }
            
      }

    public String getMessID(String user1, String user2) {
              try {
             ResultSet r  = stmt.executeQuery("SELECT messID FROM listMessage"
                     + " WHERE (user1 ='" + user1+"' and user2 ='" + user2+"') OR (user1 ='" + user2+"' and user2 ='" + user1+"') ");
               if(r.next())
               {
                   
                   return r.getString("messID");
               }
               else 
                  return null;
               
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
   }

    public String addMessID(String user1, String user2) {
            try{
                 
                   String messID = null;
                  ResultSet rs = stmt.executeQuery("INSERT INTO listMessage"
                          + "(user1, user2) OUTPUT Inserted.messID  VALUES('"+user1+"','" +user2 + "')");
                 
                  if(rs.next())
                  {
                      messID = rs.getString("messID");
                      stmt.executeUpdate("Create TABLE " +messID + " (messContent nvarchar(MAX), send nvarchar(50),  type nvarchar(20))" );
                      stmt.executeUpdate("INSERT INTO "+ messID+ "(messContent, send, type) values ('0', '"
                            + user1 +"',' scoreNone'), ('0', '"
                            + user1 +"',' winScore'),"
                                    + "('0', '"
                            + user2 +"',' winScore')");
                  }
                  
                  return  messID;
       
            }
            catch(Exception e){
                 System.out.println(e);
                 return null;
             }
    }

    public Message[] getListMessage(String messID) {
                 try {
             ResultSet r  = 
            stmt.executeQuery("SELECT COUNT(*)  AS [rowcount]  FROM  " + messID );
             r.next();
             int count = r.getInt("rowcount");
             Message [] listMessage = new Message[count];
              ResultSet rs  = 
            stmt.executeQuery("SELECT *  FROM " + messID );
               for(int i = 0; i<count; i++)   
               {
                   rs.next();
                   listMessage[i] = new Message(messID, messID, messID, rs.getString("type"));
                   listMessage[i].send = rs.getString("send").trim();
                   listMessage[i].messContent = rs.getString("messContent");
                    listMessage[i].type = rs.getString("type").trim() ;
               }
    
                                     return listMessage;
               
              
               
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
   }

    public int saveMess(Message mess) {
           try{
                 
            
                  int rs = stmt.executeUpdate("INSERT INTO "+ mess.messID
                          + "(send, messContent, type)  VALUES('"+mess.send+"','" +mess.messContent + "','"+mess.type + "')");
                 
               
                     return  rs;
                
                 
       
            }
            catch(Exception e){
                 System.out.println(e);
                 return 0;
             }
   }

    public Message saveScore(Message mess) {
         try{
            
                  ResultSet rs = stmt.executeQuery("UPDATE "+ mess.messID
      + "  SET messContent = CAST(messContent as int) +1  OUTPUT Inserted.messContent  where send = '" + mess.send +"' and type like '%"+ mess.type + "%'");
        if(rs.next())
            mess.messContent = rs.getString("messContent");
        
return mess;
            }
            catch(Exception e){
                 System.out.println("loi o ham saveScore" + e);
                 return null;
             }
    }
           
   
        
}
