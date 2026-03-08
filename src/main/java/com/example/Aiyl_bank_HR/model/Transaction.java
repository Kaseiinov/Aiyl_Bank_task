package com.example.Aiyl_bank_HR.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

}
