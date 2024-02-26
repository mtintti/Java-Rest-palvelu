package com.game.visailu;

public class Questions {
    private int id;
    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getQuery() {
        return query;
    }


    public void setQuery(String query) {
        this.query = query;
    }


    public String getAnswer() {
        return answer;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }


    private String query;
    private String answer;

    
    public Questions(int id, String query, String answer) {
        this.id = id;
        this.query = query;
        this.answer = answer;
    }

    

}