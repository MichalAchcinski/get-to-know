package pl.achcinski.gtk.Models;

public class Match {
    String age;
    String name;
    String profilePic;
    String ID;

    public Match(){} // konstuktor domyślny

    public Match(String age, String name, String profilePic, String ID){
        this.age=age;
        this.name=name;
        this.profilePic=profilePic;
        this.ID=ID;
    }

    public void changeText1(String text){
        age=text;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
