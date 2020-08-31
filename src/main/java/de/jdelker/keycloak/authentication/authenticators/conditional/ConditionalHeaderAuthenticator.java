/*
 * Copyright 2020 Joerg Delker
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.jdelker.keycloak.authentication.authenticators.conditional;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.ws.rs.core.MultivaluedMap;
import jdk.internal.joptsimple.internal.Strings;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

/**
 * Conditional Header Authenticator for Keycloak.
 * 
 * Checks the request headers for matching a given expression
 */
public class ConditionalHeaderAuthenticator implements ConditionalAuthenticator {

  public static final ConditionalHeaderAuthenticator SINGLETON = new ConditionalHeaderAuthenticator();
  private static final Logger LOG = Logger.getLogger(ConditionalHeaderAuthenticator.class);

  @Override
  public boolean matchCondition(AuthenticationFlowContext context) {
    UserModel user = context.getUser();
    AuthenticatorConfigModel authConfig = context.getAuthenticatorConfig();
    MultivaluedMap<String, String> requestHeaders = context.getHttpRequest().getHttpHeaders().getRequestHeaders();
    if (user != null && authConfig != null && authConfig.getConfig() != null) {
      String headerExpression = authConfig.getConfig().get(ConditionalHeaderAuthenticatorFactory.CONDITIONAL_HEADER_EXPRESSION);

      return containsMatchingRequestHeader(requestHeaders, headerExpression);
    }
    return false;
  }

  @Override
  public void action(AuthenticationFlowContext afc) {
    // Not used
  }

  @Override
  public boolean requiresUser() {
    return true;
  }

  @Override
  public void setRequiredActions(KeycloakSession ks, RealmModel rm, UserModel um) {
    // Not used
  }

  @Override
  public void close() {
    // Not used
  }

  private boolean containsMatchingRequestHeader(MultivaluedMap<String, String> requestHeaders, String headerPattern) {

    if (headerPattern==null || headerPattern.isEmpty() || requestHeaders.isEmpty()) {
      return false;
    }

    Pattern pattern = Pattern.compile(headerPattern, Pattern.DOTALL);

    for (Map.Entry<String, List<String>> entry : requestHeaders.entrySet()) {
      String key = entry.getKey();
      for (String value : entry.getValue()) {
        String headerEntry = key.trim() + ": " + value.trim();
        if (pattern.matcher(headerEntry).matches()) {
          return true;
        }
      }
    }
    return false;
  }

}
