package Ejercicio2;

public class Quotes {
    private String text;

    public Quotes(String text, String author) {
        this.text = text;
        this.author = author;
    }
    public Quotes(){

    }

    private String author;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Quotes{" +
                "Text: '" +
                text +
                '\'' +
                ", author: '" +
                author +
                '\'' +
                "}";

    }
}
