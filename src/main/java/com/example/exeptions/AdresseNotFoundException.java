package com.example.exeptions;

public class AdresseNotFoundException extends RuntimeException{

    public AdresseNotFoundException(String message){
        super(message);
    }
}
