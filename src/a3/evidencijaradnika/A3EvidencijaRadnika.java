/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a3.evidencijaradnika;

import javax.swing.JFrame;


/**
 *
 * @author Korisnik
 */
final public class A3EvidencijaRadnika {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        BazaProxy.PoveziSe();
        GlavniProzor gp = new GlavniProzor();
        gp.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gp.setVisible(true);
    }
    
}
