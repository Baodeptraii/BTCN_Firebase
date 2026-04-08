package com.example.mung_tam_thang_tu;

public class Ticket {
    private String id;
    private String movieTitle;
    private String userId;
    private String seat;
    private String showtime;
    private long timestamp;

    public Ticket() {}

    public Ticket(String movieTitle, String userId, String seat, String showtime, long timestamp) {
        this.movieTitle = movieTitle;
        this.userId = userId;
        this.seat = seat;
        this.showtime = showtime;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMovieTitle() { return movieTitle; }
    public String getUserId() { return userId; }
    public String getSeat() { return seat; }
    public String getShowtime() { return showtime; }
    public long getTimestamp() { return timestamp; }
}