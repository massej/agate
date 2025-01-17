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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Configuration
public class FreemarkerConfiguration {

  @Bean
  public FreeMarkerViewResolver freemarkerViewResolver() {
    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
    //resolver.setCache(true);
    //resolver.setPrefix("");
    resolver.setSuffix(".ftl");
    return resolver;
  }

}
