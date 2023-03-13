package com.app.engineerapp.FunctionUtil;

import com.app.engineerapp.DataBaseUtil.dbConnection;
import com.app.engineerapp.Models.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class function {


    //hashing fucntion return the hashed password the method used is Secure hash algorithme from java.security
    public static String hashing(String password){
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] resultByteArray= messageDigest.digest();
            StringBuilder sb =new StringBuilder();
            for (byte b:resultByteArray){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //isemailvalid fonction return true if email structered correctlly
    public static boolean isemailvalid(String email){
        //Regular Expression
       /* The following restrictions are imposed in the email addresses local-part by using this regex:
        It allows numeric values from 0 to 9
        Both uppercase and lowercase letters from a to z are allowed
        Allowed are underscore “_”, hyphen “-” and dot “.”
        Dot isn't allowed at the start and end of the local-part
        Consecutive dots aren't allowed
        For the local part, a maximum of 64 characters are allowed
        Restrictions for the domain-part in this regular expression include:
        It allows numeric values from 0 to 9
        We allow both uppercase and lowercase letters from a to z
        Hyphen “-” and dot “.” isn't allowed at the start and end of the domain-part
        No consecutive dots
        */
        String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher=pattern.matcher(email);
        return  matcher.matches();
    }

    public static void forgotpassword(String email){
        String verfcode=veriificationCodegen();
        setcodeverf(email,verfcode);
        JavaMailUtil.sendmail(email,verfcode);
    }

    public static boolean ifemailexist(String email){
        String sql="select email from person where email like ?";

        try {
            PreparedStatement pr= dbConnection.getConnexion().prepareStatement(sql);
            pr.setString(1,email);
            ResultSet rs= pr.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean ispasswordvalide(String password){
        if (password.length()>=8){
            String regex=  "^(?=.*[a-z])(?=."
                    + "*[A-Z])(?=.*\\d)"
                    + "(?=.*[-+_!@#$%^&*., ?]).+$";
            Pattern pattern=Pattern.compile(regex);
            Matcher matcher=pattern.matcher(password);
            return matcher.matches();
        }else
            return false;
    }

    //ispasswordvalide function check the password strength (return true if password strong false if else)
    public static String veriificationCodegen(){
        String num="0123456789";
        SecureRandom secureRandom=new SecureRandom();
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<6;i++){
            int indx=secureRandom.nextInt(num.length());
            sb.append(num.charAt(indx));
        }
        return sb.toString();
    }

    public static void setcodeverf(String email,String vercode){
        String sql="update responsable set verification_code =? where cni=(select cni from person where email like ?)";
        try {
            PreparedStatement pstat=dbConnection.getConnexion().prepareStatement(sql);
            pstat.setString(1,vercode);
            pstat.setString(2,email);
            pstat.executeUpdate();
        } catch (SQLException e) {
            System.out.println();
        }
    }

    public static boolean checkverfcode(String cni,String verfcode){
        String sql="select verification_code from responsable where cni like ?";
        try {
            PreparedStatement pstat= dbConnection.getConnexion().prepareStatement(sql);
            pstat.setString(1,cni);
            ResultSet rs =pstat.executeQuery();
            if (rs.next()){
                System.out.println(rs.getString("verification_code"));
                return rs.getString("verification_code").equals(verfcode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void setPassword(String table,String cni, String password,String salt){
        String sql="update "+table+" set password=?,salt=? where cni=?";

        try {
            PreparedStatement pstat=  dbConnection.getConnexion().prepareStatement(sql);
            pstat.setString(1,password);
            pstat.setString(2,salt);
            pstat.setString(3,cni);
           pstat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void unluckaccount(String cni,int l){
        String sql="update responsable set is_verified=? where cni=?";

        try {
            PreparedStatement pstat= dbConnection.getConnexion().prepareStatement(sql);
            pstat.setInt(1,l);
            pstat.setString(2,cni);
            pstat.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String salt_gen(int n){
        String Upper="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Lower="abcdefghijklmnopqrstuvwxyz";
        String Numb="0123456789";
        String Symboles="&~@[]{[]}()+=-_*!$";
        String all=Upper+Lower+Numb+Symboles;
        StringBuilder sb=new StringBuilder();
        SecureRandom random=new SecureRandom();
        for (int i=0;i<n;i++){
            int charIndex= random.nextInt(all.length());
            sb.append(all.charAt(charIndex));
        }
        return sb.toString();
    }

    public static String getcni(String email){

        if (function.ifemailexist(email)){
            String sql="select cni from person where email like ?";
            try {
                PreparedStatement pstat =dbConnection.getConnexion().prepareStatement(sql);
                pstat.setString(1,email);
                     ResultSet rs=pstat.executeQuery();
                if (rs.next())
                    return rs.getString("cni");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static Responsable responsableObjet(String cni){
        String sql ="select p.*,r.password,r.type from person p join responsable r on p.cni = r.cni where p.cni = ?";

        try {
            PreparedStatement pstat=   dbConnection.getConnexion().prepareStatement(sql);
            pstat.setString(1,cni);
            ResultSet rs=pstat.executeQuery();
            if (rs.next()){
                return new Responsable(rs.getString("cni"), rs.getString("nom"),
                        rs.getString("prenom"), Genre.valueOf(rs.getString("genre"))
                        ,rs.getString("tele"),rs.getDate("date_naiss"),rs.getDate("date_embau"),
                        rs.getString("email"),rs.getString("type") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getEmailByCni(String cni ){
        String sql="select email from person where cni like ?";
        try {
            PreparedStatement pstat =dbConnection.getConnexion().prepareStatement(sql);
            pstat.setString(1,cni);
            ResultSet rs=pstat.executeQuery();
            if (rs.next())
                return rs.getString("email");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ArrayList<Responsable> getResponsablesExcept(String cni ){
        String sql ="select p.*,r.password,r.type,r.is_verified from person p join responsable r on p.cni = r.cni where p.cni not like ?";
        ArrayList<Responsable> responsableArrayList=new ArrayList<>();
        Responsable responsable;
        try {
            PreparedStatement pstat=   dbConnection.getConnexion().prepareStatement(sql);
            pstat.setString(1,cni);
            ResultSet rs=pstat.executeQuery();
            while (rs.next()){
              responsable= new Responsable(rs.getString("cni"), rs.getString("nom"),
                        rs.getString("prenom"), Genre.valueOf(rs.getString("genre"))
                        ,rs.getString("tele"),rs.getDate("date_naiss"),rs.getDate("date_embau"),
                        rs.getString("email"),rs.getString("type") ) ;
              responsable.setIs_verified(rs.getInt("is_verified"));
              responsableArrayList.add(responsable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responsableArrayList;
    }
    public static String getRespoName(String cni ){
        String requette= "select CONCAT(nom,' ',prenom) as nomprenom from person where cni like ?";
        try {
            PreparedStatement pstat= dbConnection.getConnexion().prepareStatement(requette);
            pstat.setString(1,cni);
            ResultSet rs=pstat.executeQuery();
            while (rs.next())return rs.getString("nomprenom");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static ArrayList<Technicien> getTechicienCni(String cni ){
        boolean isAdmin = Variables.type.equals("ADMIN");
        String sql = isAdmin? "select p.*,t.password,t.is_verified,t.respoid  from person p join technicien t on p.cni = t.cni":"select p.*,t.password,t.is_verified from person p join technicien t on p.cni = t.cni where t.respoid like '"+cni+"'";
        ArrayList<Technicien> techniciensArrayList=new ArrayList<>();
        Technicien technicien;
        try {
            PreparedStatement pstat=   dbConnection.getConnexion().prepareStatement(sql);
            ResultSet rs=pstat.executeQuery();
            while (rs.next()){
                technicien= new Technicien(rs.getString("cni"), rs.getString("nom"),
                        rs.getString("prenom"), Genre.valueOf(rs.getString("genre"))
                        ,rs.getString("tele"),rs.getDate("date_naiss"),rs.getDate("date_embau"),
                        rs.getString("email"),isAdmin?getRespoName(rs.getString("respoid")):"none") ;
                technicien.setIs_verified(rs.getInt("is_verified"));
                techniciensArrayList.add(technicien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return techniciensArrayList;
    }
    public static ArrayList<Zone> getZones(String cni ,String type){
        String requette;
        requette= type.equals("ADMIN")?"select * from zoneagricole":"select * from zoneagricole where idrespo like '"+cni+"'";
        ArrayList<Zone> zoneArrayList=new ArrayList<>();
        Zone zone;
        try {
           ResultSet rs = dbConnection.getConnexion().createStatement().executeQuery(requette);
            while (rs.next()){
                zone= new Zone(rs.getString("adresse"),
                        rs.getDouble("surface"),rs.getString("idrespo"),rs.getString("examineur"),
                        rs.getDate("der_examin"),rs.getInt("needexamin"),rs.getString("recolte"),rs.getString("produit"),
                        rs.getString("proprietaire")
                );
                zone.setZoneId(rs.getInt("zoneid"));
                zoneArrayList.add(zone);
              }
            return zoneArrayList;
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static HashMap<String,String> getRespoNamesCni(String table){

        String requette=table.equals("technicien")?"select cni,concat(nom,' ',prenom) as name from person" : "select p.cni,concat(nom,' ',prenom) as name from person p join responsable r on p.cni = r.cni";
        HashMap<String,String>  hashMap= new HashMap<>();
        try {

            ResultSet rs = dbConnection.getConnexion().createStatement().executeQuery(requette);
            while (rs.next())
            {
                hashMap.put(rs.getString("cni"),rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  hashMap;
    }

    public static HashMap<String,String> getTechNamesCni(String cni){

        String requette= "select p.cni,concat(nom,' ',prenom) as name from person p join technicien r on p.cni = r.cni where r.respoid='"+cni+"'";
        HashMap<String,String>  hashMap= new HashMap<>();
        try {

            ResultSet rs = dbConnection.getConnexion().createStatement().executeQuery(requette);
            while (rs.next())
            {
                hashMap.put(rs.getString("cni"),rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  hashMap;
    }

    public static List<Rapport> getRappportsZone(String zoneId){
        List<Rapport> rapportList =new ArrayList<>();
        String requette = "select zoneid,gendate from rapport where zoneid ='"+zoneId+"'";
        try {

           ResultSet rs= dbConnection.getConnexion().createStatement().executeQuery(requette);
           while (rs.next()){
               rapportList.add(new Rapport(rs.getString("zoneid"),rs.getDate("gendate")));
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rapportList;
    }

    public static byte[] getRapportFile(String zoneId,Date genDate){
        byte[] data;
        String requette = "select rapportfile from rapport where zoneid = ? and gendate=?";
        try {
            System.out.println(zoneId+"  "+ java.sql.Date.valueOf(genDate.toString()));
            PreparedStatement st=dbConnection.getConnexion().prepareStatement(requette);
            st.setInt(1,Integer.parseInt(zoneId));
            st.setDate(2, java.sql.Date.valueOf(genDate.toString()));
            ResultSet rs =st.executeQuery();
            if (rs.next()) {
                data = rs.getBytes("rapportfile");
                return data;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return  null;
    }
    public static void deleteRapport(String zoneid ,Date genDate){
        String requette= "delete from rapport where zoneid=? and gendate=?";

        PreparedStatement st= null;
        try {
            st = dbConnection.getConnexion().prepareStatement(requette);
            st.setInt(1,Integer.parseInt(zoneid));
            st.setDate(2, java.sql.Date.valueOf(genDate.toString()));
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
public static String getKey(Map<String ,String> map, String value){
    return map
            .entrySet()
            .stream()
            .filter(entry -> Objects.equals(entry.getValue(), value))
            .map(Map.Entry::getKey)
            .findFirst().toString();
}

}

