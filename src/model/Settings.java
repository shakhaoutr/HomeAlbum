/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import view.gui.AlertWindow;

/**
 * saves the user's preferences
 * 
 * @author Shakhaout Rahman
 */
public class Settings {
    
    private static Settings single_instance = null;
    
    private static final String SETTINGS_FILE = "settings/settings.txt";
    
    public String BASE_DIR_PIC;
    public String BASE_DIR_PREVIEW;
    
    
    public String DB_CLASS;
    public String DB_DRIVER;
    
    public String DB_HOST;
    public String DB_NAME;
    public String DB_USER;
    public String DB_PASSWD;
    
    
    private Settings() {
        if(!settingsFileExist()){
            this.BASE_DIR_PIC = "";
            this.BASE_DIR_PREVIEW = this.BASE_DIR_PIC+"preview\\";
            
            this.DB_CLASS = "";
            this.DB_DRIVER = "";
            
            this.DB_HOST = "";
            this.DB_NAME = "";
            this.DB_USER = "";
            this.DB_PASSWD = "";
        } else {
            readFromFile();
            this.BASE_DIR_PREVIEW = this.BASE_DIR_PIC+"preview\\";
        }
    }
    
    public static Settings getSettings(){
        if( single_instance == null){
            single_instance = new Settings();
        }
        
        return single_instance;
    }
    
    public void saveToFile(){
        saveToFile(BASE_DIR_PIC, DB_CLASS, DB_DRIVER, DB_HOST, DB_NAME, DB_USER, DB_PASSWD);
    }
            
    public void saveToFile(String dir, String dbCls, String dbDriver, String dbHost,
                                String dbName, String dbUser, String dbPasswd){
        try {
            new File("settings/").mkdirs();
            
            PrintWriter pw = new PrintWriter(SETTINGS_FILE);
            
            pw.println("BASE_DIR_PIC - "+dir);
            
            pw.println("DB_CLASS - "+dbCls);
            pw.println("DB_DRIVER - "+dbDriver);
            
            pw.println("DB_HOST - "+dbHost);
            pw.println("DB_NAME - "+dbName);
            pw.println("DB_USER - "+dbUser);
            pw.println("DB_PASSWD - "+dbPasswd);
            
            pw.close();
            
            readFromFile();
        } catch (FileNotFoundException ex) {
            AlertWindow.display("Settings write error", "Could not write settings file!\n\n"+ex.getCause().toString());
        }
    }
    
    public void readFromFile(){
        Scanner s;
        try {
            s = new Scanner(new File(SETTINGS_FILE));

            while(s.hasNext()){
            String line = s.nextLine();
            String[] elm = line.split("-");
            switch(elm[0].trim()){
                case "BASE_DIR_PIC" :
                    this.BASE_DIR_PIC = elm[1].trim();
                    break;

                case "DB_CLASS" :
                    this.DB_CLASS = elm[1].trim();
                    break;

                case "DB_DRIVER" :
                    this.DB_DRIVER = elm[1].trim();
                    break;

                case "DB_HOST" :
                    this.DB_HOST = elm[1].trim();
                    break;

                case "DB_NAME" :
                    this.DB_NAME = elm[1].trim();
                    break;

                case "DB_USER" :
                    this.DB_USER = elm[1].trim();
                    break;

                case "DB_PASSWD" :
                    this.DB_PASSWD = elm[1].trim();
                    break;
            }
        }
        } catch (FileNotFoundException ex) {
            AlertWindow.display("Settings read error", "Could not read settings file!\n\n"+ex.getCause().toString());
        }
    }
    
    public static boolean settingsFileExist(){
        File f = new File(SETTINGS_FILE);
        return f.exists();
    }

    @Override
    public String toString() {
        return "Settings{\n"
            + "BASE_DIR_PIC= " + BASE_DIR_PIC + "\n"
            + "BASE_DIR_PREVIEW= " + BASE_DIR_PREVIEW + "\n"
            + "DB_CLASS= " + DB_CLASS + "\n"
            + "DB_DRIVER= " + DB_DRIVER + "\n"
            + "DB_HOST= " + DB_HOST + "\n"
            + "DB_NAME= " + DB_NAME + "\n"
            + "DB_USER= " + DB_USER + "\n"
            + "DB_PASSWD= " + DB_PASSWD + "\n"
            + "}";
    }
   
}
