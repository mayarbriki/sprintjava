package models;

public class reponse {
    private int id;
    private String reponse;
    private int idrec_id;

    public reponse() {
    }

    public reponse(int id, String reponse, int idrec_id) {
        this.id = id;
        this.reponse = reponse;
        this.idrec_id = idrec_id;
    }

    public reponse(String reponse, int idrec_id) {
        this.reponse = reponse;
        this.idrec_id = idrec_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getIdrec_id() {
        return idrec_id;
    }

    public void setIdrec_id(int idrec_id) {
        this.idrec_id = idrec_id;
    }

    @Override
    public String toString() {
        return "reponse{" +
                "id=" + id +
                ", reponse='" + reponse + '\'' +
                ", idrec_id=" + idrec_id +
                '}';
    }
}
