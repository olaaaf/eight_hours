package com.oleaf.eighthours.settings;

public class SettingType {
    Object v;
    Type type;

    SettingType(int x){
        v = x;
    }

    SettingType(boolean b){
        v = b;
    }

    SettingType(String s){
        v = s;
    }

    void set(int x){
        v = x;
    }

    void set(boolean b){
        v = b;
    }

    void set(String s){
        v = s;
    }

    Object get(){
        return v;
    }

    Type getType(){
        return type;
    }

    enum Type{
        STRING,BOOLEAN,INT
    }
}
