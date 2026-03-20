import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@SpringBootTest
@ActiveProfiles("test")
@Import(LifestageApplicationTests.TestConfig.class)
class LifestageApplicationTests {

  @TestConfiguration
  static class TestConfig {
    @Bean
    public JwtDecoder jwtDecoder() {
      return mock(JwtDecoder.class);
    }
  }
}