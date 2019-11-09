package sandhya.prabhu.in.ndmoviesapp.model;


public class Movie {

    private double vote_average;
    private String title;
    private Integer id;
    private String poster_path;
    private String original_title;
    private String backdrop_path;
    private String overview;
    private String release_date;


    public Movie(Integer id, double vote_average, String title, String poster_path, String original_title, String backdrop_path, String overview, String release_date) {
        this.vote_average = vote_average;
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.title = title;
        this.poster_path = poster_path;
        this.original_title = original_title;
        this.overview = overview;
        this.release_date = release_date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
