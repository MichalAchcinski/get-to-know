package pl.achcinski.gtk.Models;

public class Match {
    String age;
    String name;
    String profilePic;

    public Match(){} // konstuktor domyślny

    public Match(String age, String name, String profilePic){
        this.age=age;
        this.name=name;
        this.profilePic=profilePic;
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
}
