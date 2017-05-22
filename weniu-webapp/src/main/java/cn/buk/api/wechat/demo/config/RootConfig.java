package cn.buk.api.wechat.demo.config;

import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by yfdai on 2017/5/20.
 */
@Configuration
@PropertySource(value="classpath:config.properties")
@ComponentScan(basePackages = {"cn.buk"},
        excludeFilters = {
                @ComponentScan.Filter(type= FilterType.ANNOTATION, value = EnableWebMvc.class)
        })
public class RootConfig {
        @Bean
        public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
                return new PropertySourcesPlaceholderConfigurer();
        }
}
