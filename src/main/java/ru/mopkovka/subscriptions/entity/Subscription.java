package ru.mopkovka.subscriptions.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "subscriptions")
public class Subscription extends BaseEntity {
    private String name;
    private String description;
    private BigDecimal actualPrice;
}
