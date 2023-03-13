package com.app.engineerapp.FunctionUtil;

import com.app.engineerapp.DataBaseUtil.dbConnection;
import com.app.engineerapp.Models.Section;
import com.app.engineerapp.Models.Zone;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JsonFormnParser {

    static JSONArray json_sections = new JSONArray();

    static public JSONObject sectiontoJson(String title,JSONArray fields){
        JSONObject section = new JSONObject();
        section.put("title",title);
        section.put("fields",fields);
        return section;
    }
    static public JSONArray fieldstoJson(List<String> questions,List<String> types,JSONArray arrayOfOptions){
        int counter = 0 ;
        JSONArray array = new JSONArray();
        for(int i=0;i< questions.size();i++) {
            // tador 3la kol question w taththa f label
            // w tatzidlha type li nefs index m3aha
            // w ila kan type combobox tatzidlha loptions li kaynin fl counter (since aytheto b tertib)
            JSONObject field = new JSONObject();
            JSONObject labels= new JSONObject();
            if (types.get(i).equals("CHECKBOX")){
                // since kayn array kbir wahd dl options tandiro 0 dima w dak l counter tay3ti array(json) dl options
                labels.put("options",arrayOfOptions.getJSONArray(0).get(counter));
                counter++;
            }
            labels.put("label",questions.get(i));
            labels.put("type",types.get(i));
            field.put("field",labels);
            array.put(field);
        }
        // tat returni l array dl fields dyal section
        return array;
    }
    static public JSONArray optionstoJson(ArrayList<ArrayList<String>> options){
        // tatakhd array d array d options w tatrej3o array d object json d option (9adit hadi but ma9adch nchr7ha don't touch it)
        JSONArray array = new JSONArray();
        int counter = 0;
        for(List<String> listoption : options) {
            JSONObject data= new JSONObject();
            int i = 1;
            for(String option : listoption) {
                data.put("option"+i, option);
                i++;
            }
            array.put(counter,data);
            counter++;
        }
        return array;
    }

    static public JSONObject all(String title,List<String> questions,List<String> types,ArrayList<ArrayList<String>> options){
        // optionstoJson tatakhd array d array d options w w tatrj3hom array d json options
        JSONArray option = optionstoJson(options);

        // 3la 9bel json syntax tanchdo l array d option w tanhetoh f array akhor :/
        JSONArray arrayOfOptions = new JSONArray();
        arrayOfOptions.put(option);

        // fields to json tayakhd question , types , json d options w tay return array dl fields d section
        JSONArray fields = fieldstoJson(questions,types,arrayOfOptions);

        // sectiontoJson tatzid title 3la l fields w tat9ad objet d section
        return sectiontoJson(title,fields);
    }
    static public void wrapAll(JSONObject section){
        // tatzid section 3la sections
        json_sections.put(section);
    }

    static public int insertDB(Zone zone, JSONArray array){
        String sql = "insert into zoneagricole( adresse, surface, idrespo, examineur, der_examin,formulaire,proprietaire ,produit) values ( ?,?,?,?,?,to_json(?::json),?,?);";

        try {
            PreparedStatement st = dbConnection.getConnexion().prepareStatement(sql);
            st.setString(1, zone.getAdress());
            st.setDouble(2,zone.getSurface());
            st.setString(3, zone.getIdResponsable());
            st.setString(4,zone.getIsExamineur());
            st.setDate(5, (Date) zone.getLast_Examin());
            st.setString(6,array.toString());
            st.setString(7,zone.getProprietaire());
            st.setString(8,zone.getProduit());

            st.executeUpdate();
            return 1;
        } catch (SQLException  e) {
            e.printStackTrace();
        }
        return 0;
    }


    static public void getFromDB(){
        JSONObject jsonObject = new JSONObject();
        String sql="select * from formulaire where zoneid="+1111;
        try {
            ResultSet rs = dbConnection.getConnexion().createStatement().executeQuery(sql);
            while (rs.next()){
                jsonObject = new JSONObject("{\"sections\":"+rs.getString("data")+"}");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = jsonObject.getJSONArray("sections");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            System.out.println(explrObject);
        }

    }

    public static int json_form_maker(Zone zone,ArrayList<Section> sections) {

        for (Section section:sections){
            wrapAll(all(section.getTitle(),section.getQuestions(),section.getTypes(),section.getOptions()));
        }
        System.out.println(json_sections);
        System.out.println(zone);

        return insertDB(zone, json_sections);
    }
}

