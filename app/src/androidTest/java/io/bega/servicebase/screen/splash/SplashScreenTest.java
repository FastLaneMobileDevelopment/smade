package io.bega.servicebase.screen.splash;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import io.bega.servicebase.R;

import io.bega.servicebase.screen.main.MainScreen;
import io.bega.servicebase.test.util.BlueprintVerifier;

@RunWith(RobolectricTestRunner.class)
public class SplashScreenTest {
	@Test
	public void shouldDefineBlueprint() throws Exception {
		BlueprintVerifier.forScreen(new SplashScreen())
				.injectsView(SplashView.class)
				.addsToModule(MainScreen.Module.class)
				.hasLayout(R.layout.view_splash)
				.verify();
	}
}
