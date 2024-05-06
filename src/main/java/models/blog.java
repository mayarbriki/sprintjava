package models;


import java.util.Date;

public class blog {
    private int id;
    private String title;
    private String content;
    private String featuredImage;
    private Date publicationDate;
    private Date lastUpdatedDate;


    public blog(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public blog(String title, String content, Date publicationDate, Date lastUpdatedDate) {
        this.title = title;
        this.content = content;
        this.publicationDate = publicationDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public blog(String title, String content, String featuredImage, Date publicationDate, Date lastUpdatedDate) {
        this.title = title;
        this.content = content;
        this.featuredImage = featuredImage;
        this.publicationDate = publicationDate;
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", featuredImage='" + featuredImage + '\'' +
                ", publicationDate=" + publicationDate +
                ", lastUpdatedDate=" + lastUpdatedDate +
                '}';
    }
}
