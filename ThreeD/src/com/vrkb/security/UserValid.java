package com.vrkb.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.vrkb.bean.User;
import com.vrkb.service.UserService;
import com.vrkb.utils.MyMD5Util;

public class UserValid {

	/** 
     * ע���û� 
     *  
     * @param userName 
     * @param password 
     */  
    public static boolean registerUser(UserService userService,User user){  
        String encryptedPwd = null;
        boolean flag = false;
        try {  
            encryptedPwd = MyMD5Util.getEncryptedPwd(user.getPassword());  
            user.setPassword(encryptedPwd);
            flag = userService.save(user);
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }
        return flag;
    }  
      
    /** 
     * ��֤��½ 
     *  
     * @param userName 
     * @param password 
     * @return 
     * @throws UnsupportedEncodingException  
     * @throws NoSuchAlgorithmException  
     */  
    public static boolean loginValid(String pwdInDb,String password)   
                throws NoSuchAlgorithmException, UnsupportedEncodingException{  
        if(null!=pwdInDb){ // ���û�����  
                return MyMD5Util.validPassword(password, pwdInDb);  
        }else{  
            System.out.println("�����ڸ��û�������");  
            return false;  
        }  
    } 
}
