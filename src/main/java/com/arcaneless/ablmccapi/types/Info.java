package com.arcaneless.ablmccapi.types;

public class Info {

    String text, date, href;

    public Info(String text, String date, String href) {
        this.text = text;
        this.date = date;
        this.href = href;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getLink() {
        return href;
    }

    @Override
    public String toString() {
        return "{\'text\': " + text + ", \'date\': " + date + ", \'href\': " + href + "}";
    }
}
