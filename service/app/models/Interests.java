package models;

import javax.persistence.*;

@Entity
public class Interests {
    public Interests()
    {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long uid;

    public Long getUid() { return uid; }

    public void setUid(Long uid) { this.uid = uid; }

    public String interests;

    public String getInterests() { return interests; }

    public void setInterests(String interests) { this.interests = interests; }

}