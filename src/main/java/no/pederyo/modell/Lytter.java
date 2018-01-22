package no.pederyo.modell;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Lytter {
    private Integer id;
    private String sokeliste;
    private String beskrivelse;
    private String mappeFra;
    private String mappeTil;
    private Email emailByEmailId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "sokeliste", nullable = false, length = -1)
    public String getSokeliste() {
        return sokeliste;
    }

    public void setSokeliste(String sokeliste) {
        this.sokeliste = sokeliste;
    }

    @Basic
    @Column(name = "beskrivelse", nullable = false, length = -1)
    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    @Basic
    @Column(name = "mappe_fra", nullable = false, length = -1)
    public String getMappeFra() {
        return mappeFra;
    }

    public void setMappeFra(String mappeFra) {
        this.mappeFra = mappeFra;
    }

    @Basic
    @Column(name = "mappe_til", nullable = false, length = -1)
    public String getMappeTil() {
        return mappeTil;
    }

    public void setMappeTil(String mappeTil) {
        this.mappeTil = mappeTil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lytter lytter = (Lytter) o;
        return Objects.equals(id, lytter.id) &&
                Objects.equals(sokeliste, lytter.sokeliste) &&
                Objects.equals(beskrivelse, lytter.beskrivelse) &&
                Objects.equals(mappeFra, lytter.mappeFra) &&
                Objects.equals(mappeTil, lytter.mappeTil);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, sokeliste, beskrivelse, mappeFra, mappeTil);
    }

    @ManyToOne
    @JoinColumn(name = "email_id", referencedColumnName = "id")
    public Email getEmailByEmailId() {
        return emailByEmailId;
    }

    public void setEmailByEmailId(Email emailByEmailId) {
        this.emailByEmailId = emailByEmailId;
    }
}
