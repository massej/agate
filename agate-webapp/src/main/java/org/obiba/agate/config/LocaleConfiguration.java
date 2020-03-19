/*
 * Copyright (c) 2019 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.agate.config;

import com.google.common.eventbus.Subscribe;
import org.obiba.agate.config.locale.AngularCookieLocaleResolver;
import org.obiba.agate.config.locale.ExtendedResourceBundleMessageSource;
import org.obiba.agate.event.AgateConfigUpdatedEvent;
import org.obiba.agate.service.ConfigurationService;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.inject.Inject;

@Configuration
public class LocaleConfiguration extends WebMvcConfigurerAdapter implements EnvironmentAware {

  private RelaxedPropertyResolver propertyResolver;

  private final ConfigurationService configurationService;

  private ExtendedResourceBundleMessageSource messageSource;

  @Inject
  public LocaleConfiguration(ConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  @Override
  public void setEnvironment(Environment environment) {
    propertyResolver = new RelaxedPropertyResolver(environment, "spring.messageSource.");
  }

  @Bean(name = "localeResolver")
  public LocaleResolver localeResolver() {
    AngularCookieLocaleResolver cookieLocaleResolver = new AngularCookieLocaleResolver();
    cookieLocaleResolver.setCookieName("NG_TRANSLATE_LANG_KEY");
    return cookieLocaleResolver;
  }

  @Bean
  public MessageSource messageSource() {
    int cacheSeconds = propertyResolver.getProperty("cacheSeconds", Integer.class, 60);
    messageSource = new ExtendedResourceBundleMessageSource(configurationService, cacheSeconds);
    messageSource.setBasenames("classpath:/translations/messages", "classpath:/i18n/messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(cacheSeconds);
    return messageSource;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("language");

    registry.addInterceptor(localeChangeInterceptor);
  }

  @Async
  @Subscribe
  public void configUpdated(AgateConfigUpdatedEvent event) {
    if (messageSource != null)
      messageSource.evict();
  }
}

