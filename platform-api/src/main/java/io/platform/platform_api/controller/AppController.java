package io.platform.platform_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.platform.platform_api.dto.AppCreateRequest;
import io.platform.platform_api.service.AppService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor 
public class AppController {
    
    private final AppService appService;
    
    @PostMapping("/create")
    public ResponseEntity<String> createApp(@RequestBody AppCreateRequest request) {
        
        boolean isCreated = appService.createApp(request);

        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("{\"message\": \"Application deployed successfully\"}");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\"error\": \"Application already exists in this namespace\"}");
        }
    }

    @DeleteMapping("/delete")
    public void deleteApp() {}

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("{\"status\": \"UP\"}");
    }
    
}
