package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Define los destinos donde los clientes se suscriben
        config.enableSimpleBroker("/topic");
        // Prefijo para los mensajes que el cliente envía al servidor
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-habitup")
                // ✅ Ajustado para permitir cualquier origen de Vercel y local
                .setAllowedOriginPatterns(
                        "https://*.vercel.app",
                        "http://localhost:5173",
                        "*" // Opcional: Esto permite TODO, útil si el profe usa otra URL
                )
                .withSockJS(); // Importante para navegadores antiguos o firewalls
    }
}