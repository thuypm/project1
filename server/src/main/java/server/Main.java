/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import DAO.UserDAO;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.gson.Gson;
import config.*;
import java.util.ArrayList;
import config.UserData;
import model.UserModel;


/**
 *
 * @author MINH THUY
 */
public class Main {
    
    public static UserModel model = new UserModel();
     public static   UserDAO dao = new UserDAO();
     public static int matrixCheck[][] = new int[25][25];
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Configuration config = new Configuration();
        config.setHostname("localhost");
        Gson gson = new Gson();
        
        config.setPort(8082);
        ArrayList<String> listClient = new ArrayList<String>();
        
        final SocketIOServer server = new SocketIOServer(config);
        
        
         server.addEventListener("join", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
               sioc.joinRoom(t);
               Message [] listMessages = dao.getListMessage(t);
             server.getRoomOperations(t).sendEvent("loadMessages", gson.toJson(listMessages));
            }
        });
              server.addEventListener("left", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
               sioc.leaveRoom(t);
            }
        });
 server.addEventListener("busy", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception {
           Room room = gson.fromJson(t, Room.class);
           server.getRoomOperations(room.messID).sendEvent("busy", room);
            }
        });
        server.addEventListener("selectChat", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                Room room = gson.fromJson(t, Room.class);
               String messID = model.getMessID(room.user1, room.user2);
               room.messID = messID;
              sioc.sendEvent("newRoom", room);
              server.getBroadcastOperations().sendEvent("newRoom", room);
            }
        });
       server.addEventListener("startGame", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                Room room = gson.fromJson(t, Room.class);
               server.getRoomOperations(room.messID).sendEvent("startGame", room);
            }
        });
         server.addEventListener("stopGame", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                Room room = gson.fromJson(t, Room.class);
               server.getRoomOperations(room.messID).sendEvent("stopGame", room);
            }
        });
               
    server.addEventListener("newPlayer", String.class, new DataListener<String>() {//bắt người kết nối mới
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                listClient.add(t);
                server.getBroadcastOperations().sendEvent("newPlayer", listClient);
            }
        });
    
     server.addEventListener("exitAll", String.class, new DataListener<String>() {//bắt người kết nối mới
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception {
             Message mess = gson.fromJson(t, Message.class);
            
                listClient.remove(mess.send);
                server.getBroadcastOperations().sendEvent("newPlayer", listClient);
                if(mess.messID != null)
                  server.getRoomOperations(mess.messID).sendEvent("exitAll", mess);
            }
        });
         server.addEventListener("exit", String.class, new DataListener<String>() {//bắt người kết nối mới
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception {
             Message mess = gson.fromJson(t, Message.class);;
                server.getRoomOperations(mess.messID).sendEvent("exit", mess);
            }
        });
         server.addEventListener("quitGame", String.class, new DataListener<String>() {//bắt người kết nối mới
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
               Message mess = gson.fromJson(t, Message.class);
            server.getRoomOperations(mess.messID).sendEvent("quitGame", mess);
            }
        });
  server.addEventListener("sendMessage", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                Message mess = gson.fromJson(t, Message.class);
                dao.saveMess(mess);
        server.getRoomOperations(mess.messID).sendEvent("receiveMessage", mess);
            }
        });
    server.addEventListener("resultCompete", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                Message mess = gson.fromJson(t, Message.class);
               Message result = dao.saveScore(mess);
        server.getRoomOperations(mess.messID).sendEvent("receiveMessage", result);
            }
        });
   server.addEventListener("caroButtonCheck", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                CaroCheck checkButton = gson.fromJson(t, CaroCheck.class);  
            server.getRoomOperations(checkButton.messID).sendEvent("buttonCheck", checkButton);
            }
        });
   server.addEventListener("signin", String.class, new DataListener<String>() { // đăng nhập
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                UserData user = gson.fromJson(t, UserData.class);
          server.getBroadcastOperations().sendEvent("SigninResult", model.checkUser(user.getUsername(), user.getPassword()));
            }
        });
   server.addEventListener("signup", String.class, new DataListener<String>() {// đăng kí
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                UserData user = gson.fromJson(t, UserData.class);
          server.getBroadcastOperations().sendEvent("SignupResult", model.addUser(user.getUsername(), user.getPassword()));
            }
        });
        server.addEventListener("test", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient sioc, String t, AckRequest ar) throws Exception { 
                System.err.println(t);
             
            }
        });

          
            server.start();
            try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
        }
    }
    
}
