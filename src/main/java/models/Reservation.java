package models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation {
    private int reservationId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Host host;
    private Guest guest;
    private BigDecimal total;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Reservation() {
    }

    public Reservation(int reservationId, LocalDate startDate, LocalDate endDate, Host host, Guest guest, BigDecimal total) {
        this.reservationId = reservationId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.host = host;
        this.guest = guest;
        this.total = total;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
