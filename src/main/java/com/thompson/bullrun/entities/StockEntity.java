package com.thompson.bullrun.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String name;
    private double costBasis;
    private double closePrice;
    private byte[] logoImage;
    private int sharesOwned;
    private double currentValue;
    private LocalDateTime timestamp;
}
