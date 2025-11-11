import com.google.api.client.util.Value;
import com.livraison.demo.application.service.DeepseekClient;
import org.springframework.context.annotation.Bean;

@Value("${spring.ai.openai.api-key}")
private String apiKey;

@Bean
public DeepseekClient deepseekClient() {
    return new DeepseekClien(apiKey);
}