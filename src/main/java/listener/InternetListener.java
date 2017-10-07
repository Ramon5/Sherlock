/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listener;

import java.util.EventListener;

/**
 *
 * @author root
 */
public interface InternetListener extends EventListener{
    
    void conectado(InternetEvent evt);
    
}
