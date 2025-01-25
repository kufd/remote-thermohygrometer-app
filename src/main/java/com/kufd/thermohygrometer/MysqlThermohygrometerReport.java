package com.kufd.thermohygrometer;

import jakarta.persistence.*;
import org.apache.commons.lang3.Validate;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(indexes = {
    @Index(name = "multiIndex_place_dateTimeAdjusted", columnList = "place, dateTimeAdjusted")
})
public class MysqlThermohygrometerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime; //location timezone
    private LocalDateTime dateTimeAdjusted; //location timezone
    private double temperatureC;
    private double humidityPercent;
    private String place;
    private LocalDateTime createdAt; //UTC
    private LocalDateTime publishedAt; //UTC

    protected MysqlThermohygrometerReport() {
    }

    public MysqlThermohygrometerReport(
        Long id,
        LocalDateTime dateTime,
        LocalDateTime dateTimeAdjusted,
        double temperatureC,
        double humidityPercent,
        String place,
        LocalDateTime createdAt,
        LocalDateTime publishedAt
    ) {
        Validate.notNull(dateTime, "dateTime cannot be null");
        Validate.notNull(dateTimeAdjusted, "dateTimeAdjusted cannot be null");
        Validate.notEmpty(place);
        Validate.notNull(createdAt, "createdAt cannot be null");
        Validate.notNull(publishedAt, "publishedAt cannot be null");

        this.id = id;
        this.dateTime = dateTime;
        this.dateTimeAdjusted = dateTimeAdjusted;
        this.temperatureC = temperatureC;
        this.humidityPercent = humidityPercent;
        this.place = place;
        this.createdAt = createdAt;
        this.publishedAt = publishedAt;
    }

    public static MysqlThermohygrometerReport parse(final String report, final LocalDateTime publishedAt) {
        final String[] reportParts = report.split(",");
        final String timezone = "Europe/Kyiv";
        final LocalDateTime publishedAtLocalTime = DateTimeUtil.convertFromUtc(publishedAt, timezone);
        final LocalDateTime dateTime = LocalDateTime.parse(reportParts[0]);
        final LocalDateTime dateTimeAdjusted = Duration.between(dateTime, publishedAtLocalTime).abs().toMinutes() > 2 ? publishedAtLocalTime : dateTime;

        return new MysqlThermohygrometerReport(
            null,
            dateTime,
            dateTimeAdjusted,
            Double.parseDouble(reportParts[1]),
            Double.parseDouble(reportParts[2]),
            reportParts[3],
            DateTimeUtil.now(),
            publishedAt
        );
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDateTime getDateTimeAdjusted() {
        return dateTimeAdjusted;
    }

    public double getTemperatureC() {
        return temperatureC;
    }

    public double getHumidityPercent() {
        return humidityPercent;
    }

    public String getPlace() {
        return place;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
}
