package br.com.fiap.mining_service.command;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommandCountRepository extends JpaRepository<CommandCount, Long> {
    Optional<CommandCount> findByCommandName(String commandName);
}