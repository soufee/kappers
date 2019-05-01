package ru.kappers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.kappers.model.Fixture;
import ru.kappers.model.dto.FixtureDTO;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    private Converter<FixtureDTO, Fixture> fixtureDTOToFixtureConverter;

    @Autowired
    public void setFixtureDTOToFixtureConverter(Converter<FixtureDTO, Fixture> fixtureDTOToFixtureConverter) {
        this.fixtureDTOToFixtureConverter = fixtureDTOToFixtureConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // регистрируем FixtureDTOToFixtureConverter в системе конвертаций Spring
        registry.addConverter(fixtureDTOToFixtureConverter);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/ui/view/index.html");
    }
}
