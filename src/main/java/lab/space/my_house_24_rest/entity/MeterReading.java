package lab.space.my_house_24_rest.entity;

import jakarta.persistence.*;
import lab.space.my_house_24_rest.enums.MeterReadingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "meter_reading")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MeterReadingStatus status;

    @Column(nullable = false)
    private Double count;

    @Column(nullable = false)
    private Instant date;

    @ManyToOne
    private Apartment apartment;

    @ManyToOne
    private Service service;

}
