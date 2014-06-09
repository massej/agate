package org.obiba.agate.service;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.obiba.agate.domain.Ticket;
import org.obiba.agate.repository.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

  private static final Logger log = LoggerFactory.getLogger(TicketService.class);

  @Inject
  private TicketRepository ticketRepository;

  @Inject
  private ConfigurationService configurationService;

  /**
   * Find all {@link org.obiba.agate.domain.Ticket}.
   * @return
   */
  public List<Ticket> findAll() {
    return ticketRepository.findAll();
  }

  @NotNull
  public Ticket findById(@NotNull String id) throws NoSuchTicketException {
    Ticket ticket = ticketRepository.findOne(id);
    if(ticket == null) throw NoSuchTicketException.withId(id);
    return ticket;
  }

  public List<Ticket> findByUsername(@NotNull String username) {
    return ticketRepository.findByUsername(username);
  }

  public void deleteAll(List<Ticket> tickets) {
    if(tickets == null || tickets.isEmpty()) return;
    for(Ticket ticket : tickets) {
      delete(ticket.getId());
    }
  }

  public void save(@NotNull @Valid Ticket ticket) {
    ticketRepository.save(ticket);
  }

  public void delete(@NotNull String id) {
    ticketRepository.delete(id);
  }

  /**
   * Remembered tickets have to be removed once expired.
   * This is scheduled to get fired everyday, at midnight.
   */
  @Scheduled(cron = "0 0 0 * * ?")
  public void removeExpiredRemembered() {
    removeExpired(ticketRepository
        .findByCreatedDateBeforeAndRemembered(DateTime.now().minusHours(configurationService.getConfiguration().getLongTimeout() * 3600), true));
  }

  /**
   * Not remembered tickets have to be removed once expired.
   * This is scheduled to get fired every hour.
   */
  @Scheduled(cron = "0 * 0 * * ?")
  public void removeExpiredNotRemembered() {
    removeExpired(ticketRepository
        .findByCreatedDateBeforeAndRemembered(DateTime.now().minusHours(configurationService.getConfiguration().getShortTimeout() * 3600), false));
  }

  private void removeExpired(List<Ticket> tickets) {
    for(Ticket ticket : tickets) {
      // TODO deactivate instead of delete
      ticketRepository.delete(ticket);
    }
  }
}