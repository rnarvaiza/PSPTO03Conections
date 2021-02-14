package Ejercicio2;

public class Answer {
    public Answer(boolean exist, String text) {
        this.setExist(exist);
        this.setText(text);
    }
    public Answer(){

    }

    private boolean exist;
    private String text;



    public boolean exist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
