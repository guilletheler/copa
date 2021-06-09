package com.gt.copa;

import java.io.File;
import java.io.IOException;
import java.security.CodeSource;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javafx.application.Application;
import javafx.application.Platform;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.spring.SpringFxWeaver;

@SpringBootApplication
public class CopaApplication {

	static ConfigurableApplicationContext currentContext;

	public static void main(String[] args) {
		setAppHome();
		Application.launch(SpringbootJavaFxApplication.class, args); // (1)
	}

	@Bean
	public FxWeaver fxWeaver(ConfigurableApplicationContext applicationContext) {
		// Would also work with javafx-weaver-core only:
		// return new FxWeaver(applicationContext::getBean, applicationContext::close);
		CopaApplication.currentContext = applicationContext;
		return new SpringFxWeaver(applicationContext); // (2)
	}

	public static void restart() {
		try {

			ProcessHandle processHandle = ProcessHandle.current();
			String javaPath = processHandle.info().command().orElse("java");

			String command = javaPath + " -jar " + System.getProperty("app.home") + File.separator + "copasp.jar";

			Logger.getLogger(CopaApplication.class.getName()).log(Level.INFO, "ejecutando: '" + command + "'");

			Runtime rt = Runtime.getRuntime();
			rt.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentContext.close();
		Platform.exit();
	}

	public static void setAppHome() {
		CodeSource codeSource = CopaApplication.class.getProtectionDomain().getCodeSource();

		String jarDir = "";
		try {
			File jarFile;
			String path = codeSource.getLocation().getPath();
			if (path.startsWith("file:/") && path.contains("!")) {
				jarFile = new File(path.substring(6, path.indexOf("!") - 1));
			} else {
				jarFile = new File(codeSource.getLocation().toURI().getPath());
			}
			jarDir = jarFile.getParentFile().getPath();
		} catch (Exception ex) {
			Logger.getLogger(CopaApplication.class.getName()).log(Level.SEVERE,
					"Error buscando jar path " + codeSource.getLocation().toString(), ex);
		}

		if (!System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			if (!jarDir.startsWith("/")) {
				jarDir = "/" + jarDir;
			}
		}
		//
		// Logger.getLogger(SpringBootConsoleApplication.class.getName()).log(Level.INFO,
		// "seteando app.home= '" + jarDir + "'");

		System.setProperty("app.home", jarDir);

	}
}