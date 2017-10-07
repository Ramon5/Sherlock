/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * TwitterStreamCollect deve extender esta classe
 * @author root
 */
public abstract class Internet {
    
    Set<InternetListener> listeners;
    protected boolean conectado;

    public Internet() {
        listeners = new HashSet<>();
        conectado = false;
        life.start();
    }
    
    private Thread life = new Thread(){
        @Override
        public void run() {
            while(true){
                fireConectadoEvent();
                try {
                    Thread.sleep(5000l);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Internet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }
      
    };
    
    public synchronized void addInternetListener(InternetListener listener){
        listeners.add(listener);
    }
    
    public synchronized void removeListener(InternetListener listerner){
        listeners.remove(listerner);
    }
    
    protected synchronized void fireConectadoEvent(){
        try {
            URL url = new URL("https://www.google.com");
            URLConnection con = url.openConnection();
            HttpURLConnection httpCon = (HttpURLConnection) con;
            httpCon.connect();
            int code = httpCon.getResponseCode();
            if(code == 200){
                conectado = true;
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(Internet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            conectado = false;
        }finally{
            InternetEvent event = new InternetEvent(this);
            for(InternetListener listener: listeners){
                listener.conectado(event);
            }
            
        }
    }   

    public boolean isConectado() {
        return conectado;
    }
    
    
    
}
