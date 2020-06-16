package pl.achcinski.gtk.Models;

public class Card {
    String userId;
    String name;
    String profilePic;

    public Card(){} // konstuktor domyślny

    public Card(String userId, String name, String profilePic){
        this.userId=userId;
        this.name=name;
        this.profilePic=profilePic;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}

// klasa Card stworzona do sprawnego ustawiania / odczytywania danych