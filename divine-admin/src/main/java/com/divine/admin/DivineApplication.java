package com.divine.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * 启动程序
 *
 * @author divine
 */

@Slf4j
@SpringBootApplication(scanBasePackages = "com.divine")
public class DivineApplication {
    public static void main(String[] args) throws Exception {
        // 设置 JVM 默认时区为刚果金时区（影响所有未指定时区的 java.time 操作）
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Africa/Lubumbashi")));
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication application = new SpringApplication(DivineApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        ConfigurableApplicationContext context = application.run(args);
        ConfigurableEnvironment environment = context.getEnvironment();
        String port = environment.getProperty("server.port");
        port = (port == null || port.isEmpty()) ? "8080" : port;
        log.info("Access URLs:\n----------------------------------------------------------\n\t"
                + "Local: \t\thttp://127.0.0.1:{}\n\t"
                + "External: \thttp://{}:{}\n\t"
                + "Swagger: \thttp://{}:{}/doc.html"
                + "\n----------------------------------------------------------",
            port,
            InetAddress.getLocalHost().getHostAddress(),
            port,
            InetAddress.getLocalHost().getHostAddress(),
            port);
    }

}
