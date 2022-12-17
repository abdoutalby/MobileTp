package utils.models;

public class Client {

      int id;
             String nom;
              String adr;
              String tel;
              String fax;
              String mail;
              String contact;
              String contacttel;
              int valsync;

    public Client(int id, String nom, String adr, String tel, String fax, String mail, String contact, String contacttel, int valsync) {
        this.id = id;
        this.nom = nom;
        this.adr = adr;
        this.tel = tel;
        this.fax = fax;
        this.mail = mail;
        this.contact = contact;
        this.contacttel = contacttel;
        this.valsync = valsync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContacttel() {
        return contacttel;
    }

    public void setContacttel(String contacttel) {
        this.contacttel = contacttel;
    }

    public int getValsync() {
        return valsync;
    }

    public void setValsync(int valsync) {
        this.valsync = valsync;
    }
}
