package pl.achcinski.gtk;

public class Card {
    String userId;
    String name;

    public Card(){}; // konstuktor domyślny

    public Card(String userId, String name){
        this.userId=userId;
        this.name=name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

// klasa Card stworzona do sprawnego ustawiania / odczytywania danych