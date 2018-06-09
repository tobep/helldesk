package company.dreambadly.helldesk;

/**
 * Created by Belal on 9/9/2017.
 */

class Desk {
    private int id, status;
    private String name, fio;
    private int rating;
    private String details, image;
    private Long data;

    public Desk(int id, String name, String fio, int rating, String details, Long data, String image, int status) {
        this.id = id;
        this.name = name;
        this.fio = fio;
        this.rating = rating;
        this.details = details;
        this.data = data;
        this.image = image;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getFio() {
        return fio;
    }

    public int getRating() {
        return rating;
    }

    public String getDetails() {
        return details;
    }

    public Long getData() {
        return data;
    }

    public String getImage() {
        return image;
    }
}
