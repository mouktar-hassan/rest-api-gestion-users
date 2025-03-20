package com.example.exeptions;

public class UtilisateurNotFoundException extends RuntimeException{

    public UtilisateurNotFoundException(String message){
        super(message);
    }
}
