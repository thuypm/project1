/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

/**
 *
 * @author MINH THUY
 */

public class Message {
 public   String messID;
 public    String send;
 public   String  messContent;
  public   String  type;

    public Message(String messID, String send, String messContent, String type) {
        this.messID = messID;
        this.send = send;
        this.messContent = messContent;
         this.type = type;
        
    }
   
   
}
