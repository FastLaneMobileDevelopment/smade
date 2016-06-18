package io.bega.servicebase.environment;

import static com.atomicleopard.expressive.Expressive.map;

import java.util.Collections;
import java.util.Map;

import javax.inject.Singleton;

import io.bega.servicebase.R;
import io.bega.servicebase.util.logging.Logger;
import dagger.Provides;

@dagger.Module(
  library = true,
  complete = false
)
public class EnvironmentModule {

  private static final Logger LOG = Logger.getLogger(EnvironmentModule.class);
  private static final Environment PRODUCTION_ENVIRONMENT = new ProductionEnvironment();

  private static Map<Integer, Environment> ENVIRONMENTS = map(
    1, PRODUCTION_ENVIRONMENT
  );

  @Provides
  @Singleton
  public Environment provideEnvironment() {
    //Environment environment = ENVIRONMENTS.get(R.id.action_environment_production);
    //LOG.info("Environment is set to <%s>", environment.getName());

    return new ProductionEnvironment();
  }

  @Provides
  @Singleton
  public Map<Integer, Environment> provideEnvironments() {
    return Collections.unmodifiableMap(ENVIRONMENTS);
  }
}

