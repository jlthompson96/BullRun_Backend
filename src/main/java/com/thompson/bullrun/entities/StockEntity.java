package com.thompson.bullrun.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TimeSeries;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "Stock")
public class StockEntity {

    @Id
    private String id;
    private String symbol;
    private double closePrice;
    private int sharesOwned;
    private LocalDateTime timestamp;
}
