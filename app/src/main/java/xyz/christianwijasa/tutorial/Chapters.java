package xyz.christianwijasa.tutorial;

/**
 * Created by Richard Firdaus on 06/12/2016.
 */

public class Chapters {

    private String id;
    private String chapter_title;
    private String chapter_description;
    private String image;

    public Chapters(String id, String chapter_title, String chapter_description, String image) {
        this.image = image;
        this.chapter_description = chapter_description;
        this.chapter_title = chapter_title;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapter_title() {
        return chapter_title;
    }

    public void setChapter_title(String chapter_title) {
        this.chapter_title = chapter_title;
    }

    public String getChapter_description() {
        return chapter_description;
    }

    public void setChapter_description(String chapter_description) {
        this.chapter_description = chapter_description;
    }
}

