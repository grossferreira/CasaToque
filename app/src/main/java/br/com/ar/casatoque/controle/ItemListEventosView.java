package br.com.ar.casatoque.controle;


public class ItemListEventosView  {
    private String texto;
    private int iconeRid;

    public ItemListEventosView() {
        this("", -1);
    }

    public ItemListEventosView(String texto, int iconeRid) {
        this.texto = texto;
        this.iconeRid = iconeRid;
    }

    public int getIconeRid() {

        return iconeRid;
    }

    public void setIconeRid(int iconeRid) {

        this.iconeRid = iconeRid;
    }

    public String getTexto() {

        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}