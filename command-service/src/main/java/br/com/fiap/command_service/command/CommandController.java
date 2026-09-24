package br.com.fiap.command_service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommandController {

    private final RestTemplate restTemplate;

    public record CommandRequest(String command) {}
    public record CommandResponse(String status) {}

    @PostMapping("/command")
    public ResponseEntity<CommandResponse> receiveCommand(@RequestBody CommandRequest request) {
        log.info("📨 Comando recebido: {}", request.command());
        try {
            var response = restTemplate.postForObject(
                    "http://VALIDATOR-SERVICE/validate",
                    request,
                    CommandResponse.class
            );
            return ResponseEntity.ok(response);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new CommandResponse(e.getResponseBodyAsString()));
        }
    }

}