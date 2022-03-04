package com.gt.copa;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class SpringbootJavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    // private static Stage primaryStage;

    @Override
    public void init() throws Exception {
        this.context = new SpringApplicationBuilder() //(1)
                .sources(CopaApplication.class)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Platform.setImplicitExit(false);

        // SpringbootJavaFxApplication.primaryStage = primaryStage;
        context.publishEvent(new StageReadyEvent(primaryStage)); //(2)
    }

    @Override
    public void stop() throws Exception { //(3)
        this.context.close();
        Platform.exit();
    }

//     public static Stage getPrimaryStage() {
//         return primaryStage;
//     }
}
