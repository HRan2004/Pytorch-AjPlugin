package com.hraps.pytorch.tec;

public class TecUtils {
    public static String onlyLitter(String str){
        String s = "";
        for (int i = 0; i < str.length(); i++) {
            if(Character.isLetterOrDigit(str.charAt(i))){
                s+=""+str.charAt(i);
            }
            if(str.charAt(i)==' '){
                s+=" ";
            }
        }
        return s;
    }
}
