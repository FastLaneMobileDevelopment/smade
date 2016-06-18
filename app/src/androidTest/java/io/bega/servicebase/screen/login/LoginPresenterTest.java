package io.bega.servicebase.screen.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static io.bega.servicebase.test.util.ViewTestHelper.mockView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import io.bega.servicebase.actionbar.ActionBarConfig;
import io.bega.servicebase.actionbar.ActionBarOwner;

@RunWith(RobolectricTestRunner.class)
public class LoginPresenterTest {

	@Mock ActionBarOwner actionBarOwner;

	@Captor ArgumentCaptor<ActionBarConfig> actionBarConfigCaptor;

	@Before
	public void before() throws Exception {
		MockitoAnnotations.initMocks(this);

		LoginView view = mockView(LoginView.class);

		// LoginPresenter presenter = new LoginPresenter(actionBarOwner);
		// presenter.takeView(view);
	}

	@Test
	public void shouldConfigureActionBarOnLoad() throws Exception {
		verify(actionBarOwner).setConfig(actionBarConfigCaptor.capture());

		ActionBarConfig config = actionBarConfigCaptor.getValue();
		assertThat(config.getTitle()).isEqualTo("Login");
		assertThat(config.isVisible()).isTrue();
	}


}
