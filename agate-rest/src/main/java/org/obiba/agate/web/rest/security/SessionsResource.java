/*
 * Copyright (c) 2014 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.agate.web.rest.security;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.obiba.agate.web.rest.config.JerseyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Path("/auth")
public class SessionsResource {

  private static final Logger log = LoggerFactory.getLogger(SessionsResource.class);

//  private static final String ENSURED_PROFILE = "ensuredProfile";

//  @Autowired
//  private SubjectProfileService subjectProfileService;

  @POST
  @Path("/sessions")
  public Response createSession(@SuppressWarnings("TypeMayBeWeakened") @Context HttpServletRequest servletRequest,
      @FormParam("username") String username, @FormParam("password") String password) {
    try {
      Subject subject = SecurityUtils.getSubject();
      subject.login(new UsernamePasswordToken(username, password));
      ThreadContext.bind(subject);
      ensureProfile(subject);
      String sessionId = SecurityUtils.getSubject().getSession().getId().toString();
      log.info("Successful session creation for user '{}' session ID is '{}'.", username, sessionId);
      return Response.created(
          UriBuilder.fromPath(JerseyConfiguration.WS_ROOT).path(SessionResource.class).build(sessionId))
          .build();

    } catch(AuthenticationException e) {
      log.info("Authentication failure of user '{}' at ip: '{}': {}", username, servletRequest.getRemoteAddr(),
          e.getMessage());
      // When a request contains credentials and they are invalid, the a 403 (Forbidden) should be returned.
      return Response.status(Response.Status.FORBIDDEN).cookie().build();
    }
  }

  private void ensureProfile(Subject subject) {
    Object principal = subject.getPrincipal();

//    if(!subjectProfileService.supportProfile(principal)) {
//      return;
//    }
//
//    Session subjectSession = subject.getSession(false);
//    boolean ensuredProfile = subjectSession != null && subjectSession.getAttribute(ENSURED_PROFILE) != null;
//    if(!ensuredProfile) {
//      String username = principal.toString();
//      log.info("Ensure HOME folder for {}", username);
//      subjectProfileService.ensureProfile(subject.getPrincipals());
//      if(subjectSession != null) {
//        subjectSession.setAttribute(ENSURED_PROFILE, true);
//      }
//    }
  }
}
