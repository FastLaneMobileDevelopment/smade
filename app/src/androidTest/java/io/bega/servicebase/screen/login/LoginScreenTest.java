package io.bega.servicebase.screen.login;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import io.bega.servicebase.R;

import io.bega.servicebase.screen.main.MainScreen;
import io.bega.servicebase.screen.splash.SplashScreen;
import io.bega.servicebase.test.util.BlueprintVerifier;

@RunWith(RobolectricTestRunner.class)
public class LoginScreenTest {

	private LoginScreen screen;

	@Before
	public void before() throws Exception {
		screen = new LoginScreen();
	}

	@Test
	public void shouldDefineBlueprint() throws Exception {
		BlueprintVerifier.forScreen(screen)
				.injectsView(LoginView.class)
				.addsToModule(MainScreen.Module.class)
				.hasLayout(R.layout.view_login)
				.verify();
	}

	@Test
	public void shouldDefineParent() throws Exception {
		assertThat(screen.getParent()).isInstanceOf(SplashScreen.class);
	}
}
