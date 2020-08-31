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

import java.util.Collections;
import java.util.List;
import org.keycloak.Config;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

/**
 * Factory class for the Conditional Headers Authenticator
 */
public class ConditionalHeaderAuthenticatorFactory implements ConditionalAuthenticatorFactory {

  public static final String PROVIDER_ID = "conditional-headers";
  public static final String PROVIDER_DISPLAY = "Condition - headers";
  public static final String PROVIDER_HELP = "Regular expression that must match request headers to execute this flow.";

  protected static final String CONDITIONAL_HEADER_EXPRESSION = "condHeaderExpression";

  private static final List<ProviderConfigProperty> commonConfig = Collections.unmodifiableList(ProviderConfigurationBuilder.create()
          .property().name(CONDITIONAL_HEADER_EXPRESSION).label("Required Header Expression")
          .helpText(PROVIDER_HELP)
          .type(ProviderConfigProperty.STRING_TYPE).add()
          .build()
  );

  private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
    AuthenticationExecutionModel.Requirement.REQUIRED,
    AuthenticationExecutionModel.Requirement.DISABLED
  };

  @Override
  public void init(Config.Scope config) {
    // no-op
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
    // no-op
  }

  @Override
  public void close() {
    // no-op
  }

  @Override
  public String getId() {
    return PROVIDER_ID;
  }

  @Override
  public String getDisplayType() {
    return PROVIDER_DISPLAY;
  }

  @Override
  public String getReferenceCategory() {
    return "condition";
  }

  @Override
  public boolean isConfigurable() {
    return true;
  }

  @Override
  public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
    return REQUIREMENT_CHOICES;
  }

  @Override
  public boolean isUserSetupAllowed() {
    return true;
  }

  @Override
  public String getHelpText() {
    return PROVIDER_HELP;
  }

  @Override
  public List<ProviderConfigProperty> getConfigProperties() {
    return commonConfig;
  }

  @Override
  public ConditionalAuthenticator getSingleton() {
    return ConditionalHeaderAuthenticator.SINGLETON;
  }
}
