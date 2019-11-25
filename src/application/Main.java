package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import gameobjects.Ball;
import gameobjects.Brick;
import gameobjects.Clock;
import gameobjects.Paddle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import objectcommands.PauseCommand;
import objectcommands.ReplayCommand;
import objectcommands.ResumeCommand;
import objectcommands.StartCommand;
import objectcommands.UndoCommand;
import objectcommands.UpdateCommand;
import savegame.SaveLayout;

public class Main extends Application {


	private Ball ball = new Ball();
	private Paddle paddle = new Paddle();
	private Clock clock = new Clock();
	private Brick brick = new Brick();

	private BorderPane borderPane = new BorderPane();
	private GridPane gridPane = new GridPane();
	private Pane pane = new Pane();
	
	private Button startButton = new Button("Start / Restart");
	private Button pauseButton = new Button(" Pause ");
	private Button resumeButton = new Button("Resume");
	private Button replayButton = new Button("Replay");
	private Button undoButton = new Button("Undo");
	private Button changeLayout = new Button("Change Layout");
	private Button saveButton = new Button("Save");
	private Button loadButton =new Button("Load");
	private HBox buttonHBox = new HBox();
	private Text gameOverText = new Text(100, 150, "GAME OVER!");
	
	private Timeline timeline;
	private Timeline replayTimeline; // The timeline used for replay button.

	private StartCommand startCommand = new StartCommand(ball, paddle, clock, brick);
	private UpdateCommand updateCommand = new UpdateCommand(ball, paddle, clock, brick);
	private PauseCommand pauseCommand = new PauseCommand(ball, paddle, clock, brick);
	private ResumeCommand resumeCommand = new ResumeCommand(ball, paddle, clock, brick);
	private ReplayCommand replayCommand = new ReplayCommand(ball, paddle, clock, brick);
	private UndoCommand undoCommand = new UndoCommand(ball, paddle, clock, brick);
	boolean layflag=false;

	
	/**
	 * This method sets the GUI for the game.
	 * @param primaryStage - the javafx.stage.Stage which is currently running in the game instance.
	 */
	@Override
	public void start(Stage primaryStage) {
		pane.getChildren().add(ball.getCircle());
		pane.getChildren().add(paddle.getRectangle());
		pane.getChildren().add(brick.getRectangle());
		pane.setMaxHeight(350);
		startButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		pauseButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		resumeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		replayButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		undoButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		changeLayout.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		saveButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
		loadButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));


		buttonHBox.getChildren().addAll(startButton, pauseButton, resumeButton, replayButton, undoButton, changeLayout, saveButton, loadButton);
		buttonHBox.setSpacing(2);

		borderPane.setCenter(pane);
		borderPane.setBottom(clock.getClockLabel());

		topPane();
	
		Scene scene = new Scene(borderPane, 700, 700);
		primaryStage.setTitle("                                            DIAMOND SAVER");
		primaryStage.initStyle(StageStyle.UTILITY);
		primaryStage.setScene(scene);
		primaryStage.show();

		ball.getClockFromMain(clock);
		clock.setLabel(clock.getClockLabel());

		gameOverText.setFont(Font.font("Verdana", FontWeight.BOLD, 30));


		timeline = new Timeline(new KeyFrame(Duration.millis(15), (ActionEvent event) -> {
			updateCommand.execute();
			if (ball.collideWith(paddle.getRectangle())) {
				ball.setSpeedY(ball.getSpeedY() * -1);
			}
			if (ball.collideWith(brick.getRectangle())) {
				brick.explode();
				pane.getChildren().add(gameOverText);
				pauseButton.setDisable(true);
				timeline.stop();

			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		setButtonActions(primaryStage, scene);

	}
	
	
	/**
	 * This method sets the actions for all the buttons in the game.
	 * @param primaryStage - the javafx.stage.Stage which is currently running in the game instance.
	 * @param scene - javafx.scene.Scene which is currently running in the game instance. 	
	 */
	public void setButtonActions(Stage primaryStage, Scene scene)
	{
		pauseButton.setDisable(true);
		resumeButton.setDisable(true);
		replayButton.setDisable(true);
		undoButton.setDisable(true);

		changeLayout.setOnAction(new EventHandler<ActionEvent>() { 
			public void handle(ActionEvent e) 
			{ 
				if (!layflag) {
					layoutPane();
					layflag = !layflag;
				}
				else {
					gridPane.getChildren().clear();
					topPane();
					layflag = !layflag;
				}
			}
		});

		
		startButton.setOnAction(e-> {				
			if (replayTimeline != null) // check if replay timeline is not null, stop it
				replayTimeline.stop();
			pane.setDisable(false);
			pane.getChildren().remove(gameOverText);
			startCommand.execute();
			timeline.playFromStart();
			pauseButton.setDisable(false);
			resumeButton.setDisable(true);
			replayButton.setDisable(true);
			undoButton.setDisable(true);

		}); 

		pauseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (replayTimeline != null) // check if replay timeline is not null, stop it
					replayTimeline.stop();
				pane.setDisable(true);
				pauseCommand.execute();
				timeline.pause();
				resumeButton.setDisable(false);
				replayButton.setDisable(false);
				undoButton.setDisable(false);
			}
		}); 

		resumeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (replayTimeline != null) // check if replay timeline is not null, stop it
					replayTimeline.stop();
				pane.setDisable(false);
				resumeCommand.execute();
				timeline.play();
				pauseButton.setDisable(false);
				resumeButton.setDisable(true);
				replayButton.setDisable(true);
				undoButton.setDisable(true);
			}
		});

		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				saveGame();
			}
		});

		loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				loadGame(primaryStage);
			}
		});

		replayButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				Stage secondStage = new Stage();
				BorderPane secondBorderPane = new BorderPane();
				Pane secondPane = new Pane();
				Text replayingText = new Text(60, 80, "REPLAYING");
				replayingText.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
				replayingText.setFill(Color.GRAY);
				secondPane.getChildren().add(ball.getCircle());
				secondPane.getChildren().add(paddle.getRectangle());
				secondPane.getChildren().add(brick.getRectangle());
				secondPane.getChildren().add(replayingText);
				secondBorderPane.setCenter(secondPane);
				secondBorderPane.setTop(clock.getClockLabel());
				Scene secondScene = new Scene(secondBorderPane, 700, 700);
				secondStage.setScene(secondScene);
				secondStage.setTitle("Replaying");
				secondStage.show();
				pane.getChildren().remove(gameOverText);

				replayTimeline = new Timeline(new KeyFrame(Duration.millis(15), new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent t) {
						pane.setDisable(false);
						replayCommand.execute();
					}
				}));
				int queueSize = ball.getReplayQueueSize();
				replayTimeline.setCycleCount(queueSize);
				replayTimeline.play();

				// Close the replaying window and recover the primary stage
				secondStage.setOnCloseRequest(event -> {
					replayTimeline.stop();
					pane.getChildren().add(ball.getCircle());
					pane.getChildren().add(paddle.getRectangle());
					pane.getChildren().add(brick.getRectangle());
					borderPane.setCenter(pane);
					borderPane.setBottom(clock.getClockLabel());
					borderPane.setTop(gridPane);
					primaryStage.setScene(scene);
					primaryStage.show();
				});
			}
		});

		undoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				undoCommand.execute();
				pauseButton.setDisable(true);
				int queueSize = ball.getReplayQueueSize();
				if (queueSize == 0) 
					replayButton.setDisable(true);
			}
		}); 

	}
	
	
	/**
	 * Sets layout of the game.
	 */
	public  void layoutPane() {
		gridPane.setStyle("-fx-background-color: #FFEBCD;");
		gridPane.getChildren().clear();
		gridPane.add(pauseButton, 7, 0);
		gridPane.add(replayButton, 6, 1);
		gridPane.add(undoButton, 8, 1);
		gridPane.add(resumeButton, 7, 2);
		gridPane.add(startButton, 3, 4);
		gridPane.add(changeLayout, 19, 4);
		gridPane.add(saveButton, 4, 4);
		gridPane.add(loadButton, 18, 4);
		borderPane.setTop(gridPane);

	}
	
	
	/**
	 * Sets layout of the game.
	 */
	public  void topPane() {
		gridPane.setStyle("-fx-background-color: #FFEBCD;");
		gridPane.add(saveButton, 1, 0);
		gridPane.add(pauseButton, 2, 0);
		gridPane.add(loadButton, 3, 0);
		gridPane.add(undoButton, 4, 0);
		gridPane.add(replayButton, 5, 0);
		gridPane.add(changeLayout, 6, 0);
		gridPane.add(startButton, 7, 0);
		gridPane.add(resumeButton, 8, 0);
		borderPane.setTop(gridPane);

	}
	
	
	/**
	 * Saves the instance of SaveLayout in the file system.
	 */

	public void saveGame()
	{
		Object[] batlist= paddle.getReplayQueue().toArray();
		Object[] balllist= ball.getReplayQueue1().toArray();
		

		double batX = (double) batlist[batlist.length - 1];
		
		List<Double> lastBallCords = (List<Double>) balllist[balllist.length-1];
		double ballX = lastBallCords.get(0);
		double ballY = lastBallCords.get(1);
		double time = lastBallCords.get(2);


		
		SaveLayout sl = new SaveLayout(ballX, ballY, batX, time);
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now(); 
		String filename = dtf1.format(now);
		filename = filename.replace(':', '-');
		System.out.println(filename);
		try
		{
			FileOutputStream fos = new FileOutputStream("C:/Users/manuj/Desktop/test/"+filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(sl);    
			oos.close();
			fos.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return;
		}

	}

	
	/**
	 * Loads the game stored previously in the file system.
	 * @param Stage - the object of javafx.stage.Stage that we are currently using to load the game  back in same stage.
	 */
	public void loadGame(Stage primaryStage) 
	{
		if(pane.getChildren().contains(gameOverText))
		{
			pane.getChildren().remove(gameOverText);
		}
		FileChooser  fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(primaryStage);

		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile)))
		{
			SaveLayout sl1 = (SaveLayout)ois.readObject();
			ball.setBallCenterX(sl1.getBallX());
			ball.setBallCenterY(sl1.getBallY());
			clock.setTime(sl1.getTime());
			paddle.setPaddleX(sl1.getBatX());
			pane.setDisable(false);
			//resumeCommand.execute();
			timeline.play();
			pauseButton.setDisable(false);
			resumeButton.setDisable(true);
			replayButton.setDisable(true);
			undoButton.setDisable(true);
			brick.getBrickRectangle().setFill(Color.GRAY);
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return;
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		launch(args);
	}
}
