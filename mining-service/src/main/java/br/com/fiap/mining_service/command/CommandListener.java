package br.com.fiap.mining_service.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandListener {

    private final CommandCountRepository commandCountRepository;

    @RabbitListener(queues = "mining.queue")
    public void handleCommand(String command) {
        log.info("Robô executando comando: {}", command);

        var commandCount = commandCountRepository.findByCommandName(command)
                .orElseGet(() -> {
                    var novo = new CommandCount();
                    novo.setCommandName(command);
                    novo.setCount(0);
                    return novo;
                });

        commandCount.setCount(commandCount.getCount() + 1);
        commandCountRepository.save(commandCount);

        log.info("{} -> {}", command, commandCount.getCount());
    }

}