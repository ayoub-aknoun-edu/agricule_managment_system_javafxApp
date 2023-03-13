package com.app.engineerapp.Models;

import java.util.ArrayList;

public class Section {
    private String title;
    private ArrayList<String> questions;
    private ArrayList<String> types;
    private ArrayList<ArrayList<String>> options;
    public Section(){

    }

    public Section(String title, ArrayList<String> questions, ArrayList<String> types) {
        this.title = title;
        this.questions = questions;
        this.types = types;
    }

    public Section(String title, ArrayList<String> questions, ArrayList<String> types, ArrayList<ArrayList<String>> options) {
        this.title = title;
        this.questions = questions;
        this.types = types;
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public ArrayList<ArrayList<String>> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<ArrayList<String>> options) {
        this.options = options;
    }


    @Override
    public String toString() {
        return "Section{" +
                "title='" + title + '\'' +
                ", questions=" + questions +
                ", types=" + types +
                ", options=" + options.toString() +
                '}';
    }
}
