/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import config.UserData;
import DAO.UserDAO;
import config.Message;
/**
 *
 * @author MINH THUY
 */
public class UserModel {
    UserDAO dao = new UserDAO();
       public int checkUser(String username, String password) // check đăng nhập
    {
        UserData user = dao.getUser(username);
        if(user == null)
            return 0;
        else
            if((user.getPassword()).equals(password))
                return 1;
            else 
            return  0;
    }
       public int addUser(String username, String password)
            {
                if(dao.getUser(username) != null)
                    return 0;
                else 
                   return dao.addUser(username, password);
                
            }

    public String getMessID(String user1, String user2) {// kiểm tra xem đã có ID trò chuyện chưa? nếu chưa thì tạo
            String messID = dao.getMessID(user1, user2);
            if(messID == null)
            {
                messID = dao.addMessID(user1, user2);
            }
            return messID;
            
    }

 
}
