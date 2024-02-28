package br.com.pupposoft.fiap;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class })
public class SgrPedidoService {
    public static void main(String[] args) {
    	Locale.setDefault(new Locale( "pt", "BR" ));
        SpringApplication.run(SgrPedidoService.class, args);
    }
}
