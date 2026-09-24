package br.com.fiap.validator_service.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import static br.com.fiap.validator_service.config.RabbitMQConfig.EXCHANGE;
import static br.com.fiap.validator_service.config.RabbitMQConfig.ROUTING_KEY;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ValidatorController {

    private final RabbitTemplate rabbitTemplate;

    private static final Set<String> VALID_COMMANDS =
            Set.of("RIGHT", "LEFT", "FRONT", "BACK", "OPEN", "CLOSE");

    public record CommandRequest(String command) {}
    public record CommandResponse(String status) {}

    @PostMapping("/validate")
    @Retryable(
            includes = ValidationFailedException.class,
            maxRetries = 5,
            delay = 500,
            jitter = 20,
            //exponentialBackoff
            multiplier = 2,
            maxDelay = 10_000
    )
    public CommandResponse validate(@RequestBody CommandRequest request) {
        var command = request.command() == null ? null : request.command().toUpperCase();

        if (!VALID_COMMANDS.contains(command)) {
            // comando fora do vocabulario -> erro definitivo, nao entra em retry
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comando inválido: " + request.command());
        }

        if (Math.random() < 0.5) {
            log.info("Falha simulada ao validar '{}', tentando novamente...", command);
            throw new ValidationFailedException("Falha ao validar comando: " + command);
        }

        log.info("Comando '{}' validado, publicando no RabbitMQ...", command);
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, command);

        return new CommandResponse("Comando validado e enviado para a fila");
    }

}