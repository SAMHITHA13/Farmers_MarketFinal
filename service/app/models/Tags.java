package models;

import javax.persistence.*;

@Entity
public class Tags {
    public Tags()
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

    public String tags;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    public Long postId;
    public Long getPostId(){ return postId;}
    public void setPostId(Long postId)
    {
        this.postId=postId;
    }

}