package no.pederyo.modell;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Bruker {
    private Integer id;
    private String name;
    private Collection<Email> emailsById;

    public Bruker() {
    }

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
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @OneToMany(mappedBy = "brukerByBrukerId", fetch= FetchType.EAGER)
    public Collection<Email> getEmailsById() {
        return emailsById;
    }

    public void setEmailsById(Collection<Email> emailsById) {
        this.emailsById = emailsById;
    }
}
