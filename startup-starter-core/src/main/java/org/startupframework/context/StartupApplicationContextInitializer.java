package org.startupframework.context;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.startupframework.WaitForService;
import org.startupframework.exception.StartupException;

public class StartupApplicationContextInitializer
		implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	final static String DEPENDENCIES = "startup.dependencies";
	
	static boolean readyDependencies = false;

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {

		if(readyDependencies) {
			return;
		}
		
		String dependencies = applicationContext.getEnvironment().getProperty(DEPENDENCIES);
		String[] profiles = applicationContext.getEnvironment().getActiveProfiles();

		if (dependencies == null) {
			for (String profile : profiles) {
				try {
					String resourceName = String.format("classpath:application-%s.yml", profile);
					Resource resource = applicationContext.getResource(resourceName);
					YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
					List<PropertySource<?>> yamlProperties = sourceLoader.load("startup", resource);
					for (PropertySource<?> propertySource : yamlProperties) {
						applicationContext.getEnvironment().getPropertySources().addLast(propertySource);
					}
				} catch (Exception e) {
					continue;
				}
			}
			dependencies = applicationContext.getEnvironment().getProperty(DEPENDENCIES);
		}

		if (dependencies != null) {
			waitForDependencies(dependencies);
			readyDependencies = true;
		}
	}

	void waitForDependencies(String dependencies) {

		Map<String, Integer> buffer;
		try {
			buffer = Arrays.stream(dependencies.split(",")).map(next -> next.split(":"))
					.collect(Collectors.toMap(entry -> entry[0] + entry[1], entry -> Integer.parseInt(entry[1])));

		} catch (Exception ex) {
			throw new StartupException("Invalid format for " + DEPENDENCIES, ex);
		}

		buffer.forEach((host, port) -> {
			try {
				host = host.replace(String.valueOf(port), "");
				WaitForService.waitForIt(host, port);
			} catch (InterruptedException ex) {
				throw new RuntimeException(ex);
			}
		});
	}

}
