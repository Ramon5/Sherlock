/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import java.util.EventObject;

/**
 *
 * @author root
 */
public class InternetEvent{
    
   private Internet source;

    public InternetEvent(Internet source) {
        this.source = source;
    }

    public Internet getSource() {
        return source;
    }
   
    
   
    
}
