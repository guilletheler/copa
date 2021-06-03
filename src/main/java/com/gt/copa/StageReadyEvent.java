package com.gt.copa;

import org.springframework.context.ApplicationEvent;

import javafx.stage.Stage;

/**
 * @author <a href="mailto:rene.gielen@gmail.com">Rene Gielen</a>
 * @noinspection WeakerAccess
 */
public class StageReadyEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final Stage stage;

	public StageReadyEvent(Stage stage) {
		super(stage);
		this.stage = stage;
	}
}