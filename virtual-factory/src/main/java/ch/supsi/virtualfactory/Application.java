package ch.supsi.virtualfactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import ch.supsi.isteps.virtualfactory.realtodigitalsync.data.ConfigurationData;


@ComponentScan(basePackages={"ch.supsi.isteps.virtualfactory"})
@EnableJpaRepositories(basePackages={"ch.supsi.isteps.virtualfactory.openapi.persistence"})
@EntityScan(basePackages={"ch.supsi.isteps.virtualfactory.openapi.persistence"})
@SpringBootApplication
public class Application {


	public static void main(String[] args) throws Exception {
		try {
			/*server.port and ConfigurationData.REAL_TO_DIGITAL_SYNCHRONIZATION_COMPONENT_PORT) 
			 * not used anymore due to profile configuration in the application.properties file
			 */
			//System.getProperties().put("server.port", ConfigurationData.REAL_TO_DIGITAL_SYNCHRONIZATION_COMPONENT_PORT);
			System.getProperties().put("log4j.rootLogger", "stout");
			System.getProperties().put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
			System.getProperties().put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayou");
			SpringApplication.run(Application.class, args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
