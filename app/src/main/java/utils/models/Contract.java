package utils.models;


public class Contract {
    private int id;

    private int idClient;
    private String datedebut;
    private String datefin;
    private double reference;
    private int valSync;

    public Contract(int id, int idClient, String datedebut, String datefin, double reference, int valSync) {
        this.id = id;
        this.idClient = idClient;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.reference = reference;
        this.valSync = valSync;
    }

    public int getId() {
        return id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getDatedebut() {
        return datedebut;
    }

    public String getDatefin() {
        return datefin;
    }

    public double getReference() {
        return reference;
    }

    public int getValSync() {
        return valSync;
    }
}
