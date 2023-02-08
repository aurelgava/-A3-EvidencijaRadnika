/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a3.evidencijaradnika;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author Korisnik
 */
public class BazaProxy {

    public static Connection c;
    public static ArrayList<PodaciDO> podaciZaGrafik;
    public static void PoveziSe() {
        if (c == null) {
            try {
                c = DriverManager.getConnection("jdbc:ucanaccess://src\\resursi\\Evidencija_Radnik_Projekat.accdb");
            } catch (SQLException ex) {
                Logger.getLogger(A3EvidencijaRadnika.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null, "Грешка код повезивања са базом", "Грешка", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    static ResultSet getProjects() {
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Projekat");
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(BazaProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    static void brisiProjekat(int id) {
        try {
            PreparedStatement ps = c.prepareStatement("DELETE FROM Projekat WHERE ProjekatID = ?");
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(BazaProxy.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    static ArrayList<PodaciDO> getProjectsByYear(int starost) {
        try {
            PreparedStatement ps = c.prepareStatement("SELECT Godina, COUNT(BrojProjekata) AS BrojProjekata, SUM(BrojRadnika) AS BrojRadnika FROM\n"
                    + "(SELECT YEAR(Projekat.DatumPocetka) AS Godina, Projekat.ProjekatID AS BrojProjekata, COUNT(Ucesce.RadnikID) AS BrojRadnika\n"
                    + "FROM\n"
                    + "Projekat INNER JOIN Ucesce ON Projekat.ProjekatID=Ucesce.ProjekatID\n"
                    + "WHERE YEAR(Projekat.DatumPocetka)>=YEAR(Date())-?"
                    + "GROUP BY YEAR(Projekat.DatumPocetka), Projekat.ProjekatID)\n"
                    + "GROUP BY Godina ");
            ps.setInt(1, starost);
            ResultSet rs = ps.executeQuery();
            podaciZaGrafik = new ArrayList<>();
            while(rs.next()){
                //System.out.println(rs.getInt("Godina")+" "+ rs.getInt("BrojProjekata")+" "+rs.getInt("BrojRadnika"));
                PodaciDO podatak = new PodaciDO();
                podatak.godina = rs.getInt("Godina");
                podatak.brojProjekata = rs.getInt("BrojProjekata");
                podatak.brojRadnika = rs.getInt("BrojRadnika");
                podaciZaGrafik.add(podatak);
            }
            return podaciZaGrafik;
        } catch (SQLException ex) {
            Logger.getLogger(BazaProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
