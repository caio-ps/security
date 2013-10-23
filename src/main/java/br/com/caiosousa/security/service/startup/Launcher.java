package br.com.caiosousa.security.service.startup;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "br.com.caiosousa.security.service", "br.com.caiosousa.security.spring" })
@EnableAutoConfiguration
public class Launcher {
}
