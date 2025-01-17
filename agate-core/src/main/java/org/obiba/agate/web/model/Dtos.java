/*
 * Copyright (c) 2019 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.obiba.agate.web.model;

import org.obiba.agate.domain.*;
import org.obiba.agate.service.TicketService;
import org.obiba.oidc.OIDCConfiguration;
import org.obiba.web.model.AuthDtos;
import org.obiba.web.model.OIDCDtos;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

@Component
@SuppressWarnings("OverlyCoupledClass")
public class Dtos {

  private final TicketDtos ticketDtos;

  private final UserDtos userDtos;

  private final ApplicationDtos applicationDtos;

  private final GroupDtos groupDtos;

  private final RealmConfigDtos realmConfigDtos;

  private final ConfigurationDtos configurationDtos;

  private final OidcAuthProviderDtos oidcAuthProviderDtos;

  @Inject
  public Dtos(TicketService ticketService,
              TicketDtos ticketDtos,
              UserDtos userDtos,
              ApplicationDtos applicationDtos,
              GroupDtos groupDtos,
              RealmConfigDtos realmConfigDtos,
              ConfigurationDtos configurationDtos, OidcAuthProviderDtos oidcAuthProviderDtos) {
    this.ticketDtos = ticketDtos;
    this.userDtos = userDtos;
    this.applicationDtos = applicationDtos;
    this.groupDtos = groupDtos;
    this.realmConfigDtos = realmConfigDtos;
    this.configurationDtos = configurationDtos;
    this.oidcAuthProviderDtos = oidcAuthProviderDtos;
  }

  @NotNull
  public Agate.TicketDto asDto(@NotNull Ticket ticket) {
    return ticketDtos.asDto(ticket);
  }

  @NotNull
  public Agate.AuthorizationDto asDto(@NotNull Authorization authorization) {
    return ticketDtos.asDto(authorization);
  }

  @NotNull
  public Agate.UserDto asDto(@NotNull User user) {
    return userDtos.asDto(user);
  }

  @NotNull
  public Agate.UserSummaryDto asSummaryDto(@NotNull User user) {
    return userDtos.asSummaryDto(user);
  }

  @NotNull
  public AuthDtos.SubjectDto asDto(@NotNull User user, boolean withAttributes) {
    return userDtos.asDto(user, withAttributes);
  }

  @NotNull
  public Agate.GroupDto.Builder asDtoBuilder(@NotNull Group group) {
    return groupDtos.asBuilderDto(group);
  }

  @NotNull
  public Agate.GroupDto asDto(@NotNull Group group) {
    return asDtoBuilder(group).build();
  }

  @NotNull
  public Agate.ApplicationDto asDto(@NotNull Application application) {
    return applicationDtos.asDto(application, false);
  }

  @NotNull
  public Agate.ApplicationDto asDto(@NotNull Application application, boolean summary) {
    return applicationDtos.asDto(application, summary);
  }

  @NotNull
  public Agate.ConfigurationDto asDto(@NotNull Configuration configuration) {
    return configurationDtos.asDto(configuration);
  }

  @NotNull
  public Agate.AttributeConfigurationDto asDto(@NotNull AttributeConfiguration attributeConfiguration) {
    return configurationDtos.asDto(attributeConfiguration).build();
  }

  @NotNull
  public Configuration fromDto(@NotNull Agate.ConfigurationDtoOrBuilder dto) {
    return configurationDtos.fromDto(dto);
  }

  @NotNull
  public Application fromDto(@NotNull Agate.ApplicationDto dto) {
    return applicationDtos.fromDto(dto);
  }

  @NotNull
  public Group fromDto(@NotNull Agate.GroupDto dto) {
    return groupDtos.fromDto(dto);
  }
  @NotNull
  public User fromDto(@NotNull Agate.UserDto dto) {
    return userDtos.fromDto(dto);
  }

  @NotNull
  public RealmConfig fromDto(Agate.RealmConfigDto dto) {
    return realmConfigDtos.fromDto(dto);
  }

  @NotNull
  public Agate.RealmConfigDto.Builder asDtoBuilder(RealmConfig config) {
    return realmConfigDtos.asDtoBuilder(config);
  }

  @NotNull
  public Agate.RealmConfigDto asDto(RealmConfig config) {
    return realmConfigDtos.asDtoBuilder(config).build();
  }

  @NotNull
  public Agate.RealmConfigSummaryDto asSummaryDto(RealmConfig config) {
    return realmConfigDtos.asSummaryDto(config);
  }

  @NotNull
  public OIDCDtos.OIDCAuthProviderSummaryDto asSummaryDto(@NotNull OIDCConfiguration configuration, @Nullable String locale) {
    return oidcAuthProviderDtos.asSummaryDto(configuration, locale);
  }
}
