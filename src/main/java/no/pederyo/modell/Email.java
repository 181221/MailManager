package no.pederyo.modell;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Email {
    private Integer id;
    private String username;
    private String passord;
    private String salt;
    private int mailtype;
    private Bruker brukerByBrukerId;
    private Collection<Lytter> lyttersById;

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
    @Column(name = "username", nullable = false, length = -1)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "passord", nullable = false, length = -1)
    public String getPassord() {
        return passord;
    }

    public void setPassord(String passord) {
        this.passord = passord;
    }

    @Basic
    @Column(name = "salt", nullable = false, length = -1)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Basic
    @Column(name = "mailtype", nullable = false)
    public int getMailtype() {
        return mailtype;
    }

    public void setMailtype(int mailtype) {
        this.mailtype = mailtype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return mailtype == email.mailtype &&
                Objects.equals(id, email.id) &&
                Objects.equals(username, email.username) &&
                Objects.equals(passord, email.passord) &&
                Objects.equals(salt, email.salt);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, username, passord, salt, mailtype);
    }

    @ManyToOne
    @JoinColumn(name = "bruker_id", referencedColumnName = "id")
    public Bruker getBrukerByBrukerId() {
        return brukerByBrukerId;
    }

    public void setBrukerByBrukerId(Bruker brukerByBrukerId) {
        this.brukerByBrukerId = brukerByBrukerId;
    }

    @OneToMany(mappedBy = "emailByEmailId")
    public Collection<Lytter> getLyttersById() {
        return lyttersById;
    }

    public void setLyttersById(Collection<Lytter> lyttersById) {
        this.lyttersById = lyttersById;
    }
}
