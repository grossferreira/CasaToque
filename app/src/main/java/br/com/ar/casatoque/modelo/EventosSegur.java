
package br.com.ar.casatoque.modelo;

public class EventosSegur {

    public EventosSegur(String name, String born, String image, String nation) {
        super();
        this.name = name;
        this.born = born;
        this.image = image;
        this.nation = nation;
    }
    private String name;
    private String born;
    private String image;
    private String nation;

    public String getName() {
        return name;
    }
    public void setName(String nameText) {
        name = nameText;
    }
    public String getNick() {
        return born;
    }
    public void setNick(String born) {
        this.born = born;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getNation() {
        return nation;
    }
    public void setNation(String image) {
        this.image = nation;
    }
}