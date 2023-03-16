package com.digispice.m2m.utilities;


import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;

public class passwordencryption {
    public static void main(String args[])
    
    {
           MessageDigestPasswordEncoder passwordEncoder = new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("SHA-256");
                     
           
           String hashedPassword=null;
    
    
                  String password = "p2paudit12021";
                  
                  hashedPassword = passwordEncoder.encode(password);
                  System.out.println(hashedPassword);
           
    }


}
