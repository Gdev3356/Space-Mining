package br.com.fiap.mining_service.command;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommandCountController {

    private final CommandCountRepository commandCountRepository;

    @GetMapping("/commands")
    public List<CommandCount> getAllCounts() {
        return commandCountRepository.findAll();
    }

}