package io.bega.servicebase.screen.register;

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
public class RegisterScreenTest {
	private RegisterScreen screen;

	@Before
	public void before() throws Exception {
		screen = new RegisterScreen();
	}

	@Test
	public void shouldDefineBlueprint() throws Exception {
		BlueprintVerifier.forScreen(screen)
				.injectsView(RegisterView.class)
				.addsToModule(MainScreen.Module.class)
				.hasLayout(R.layout.view_register)
				.verify();
	}

	@Test
	public void shouldDefineParent() throws Exception {
		assertThat(screen.getParent()).isInstanceOf(SplashScreen.class);
	}
}
