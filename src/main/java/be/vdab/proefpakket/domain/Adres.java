package be.vdab.proefpakket.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class Adres implements Serializable {
    private static final long serialVersionUID = 1L;
    private String straat;
    private String huisNr;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gemeenteId")
    private Gemeente gemeente;

    public String getStraat() {
        return straat;
    }

    private void setStraat(String straat) {
        this.straat = straat;
    }

    public String getHuisNr() {
        return huisNr;
    }

    private void setHuisNr(String huisNr) {
        this.huisNr = huisNr;
    }

    public Gemeente getGemeente() {
        return gemeente;
    }

    private void setGemeente(Gemeente gemeente) {
        this.gemeente = gemeente;
    }
}
