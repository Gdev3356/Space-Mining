package br.com.fiap.mining_service.command;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CommandCount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String commandName;

    private int count;

}