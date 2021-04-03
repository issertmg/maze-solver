//package sample;
//
import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import java.util.*;
import java.util.concurrent.*;

public class MazeSolverFX extends Application implements Runnable {

    private static Stage pStage;

    private static int num, replayCounter;
    private static AnchorPane main;
    private static AnchorPane[][] panes;
    private static Label pathlbl, explbl, timelbl, found, notFound;
    private static ScheduledExecutorService executor;

    private static Grid grid, tempGrid;
    private static Cell start;
    private static Cell goal;
    private static int pathcount;
    private static int exploredCount;
    private static ArrayList<Cell> openList;
    private static ArrayList<Cell> closedList;
    private static ArrayList<Cell> finalPath;

    //details on final

    private static boolean bFinished;
    private static Button confirm, reset, startNew, replay;

    private void updateGUI() {

        if (bFinished) {
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                    // same as below
                    if (grid.getGrid()[i][j].getType() == Cell.END)
                        panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(242,169,81)");

                    else if (grid.getGrid()[i][j].getType() == Cell.START)
                        panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(255,159,248)");
                       
                    else if (grid.getGrid()[i][j].getType() == Cell.SPACE)
                        panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(124,5,242)");

                    else if (grid.getGrid()[i][j].getType() == Cell.WALL)
                        panes[i][j].setStyle(" -fx-border-color: rgb(5,2,89); -fx-background-color: rgb(242,56,39); " +
                                "-fx-cursor: hand");
                
                    // final path
                    else if (grid.getGrid()[i][j].getType() == Cell.VISITED)
                        panes[i][j].setStyle(" -fx-border-color: rgb(5,2,89); -fx-background-color: rgb(82,3,145)");
                       
                    else if (grid.getGrid()[i][j].getType() == Cell.PATH)
                        panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(168,226,2)");
                }
            }

        } else {
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                    // colors upon clicking, traversal
                    if (grid.getGrid()[i][j].getType() == Cell.START)
                        panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(255,159,248)");

                    else if (grid.getGrid()[i][j].getType() == Cell.END)
                        panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(242,169,81)");
                        
                    else if (grid.getGrid()[i][j].getType() == Cell.SPACE)
                        panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(124,5,242); " +
                                "-fx-cursor: hand");

                    else if (grid.getGrid()[i][j].getType() == Cell.WALL)
                        panes[i][j].setStyle(" -fx-border-color: rgb(5,2,89); -fx-background-color: rgb(242,56,39); " +
                                "-fx-cursor: hand");

                    else if (grid.getGrid()[i][j].getType() == Cell.VISITED)
                        panes[i][j].setStyle(" -fx-border-color: rgb(5,2,89); -fx-background-color: rgb(82,3,145)");
                }
            }
        }

    }

    @Override
    public void start (Stage primaryStage) {
        pStage = primaryStage;
        pStage.getIcons().add(new Image(getClass().getResourceAsStream("Alien.png")));
        goToScene1();
    }

    public static void main (String[] args) {
        launch(args);
    }

    public void goToScene1 () {
        //Scene 1
        Label lbl = new Label("Enter a number between 8 and 64");
        Button btn = new Button();
        TextField tf = new TextField();

        bFinished = false;

        lbl.setStyle("-fx-font-family: Futura; -fx-font-size: 13 px; -fx-text-fill: rgb(255,255,255)");
        tf.setPromptText("Grid Size");
        tf.setStyle("-fx-border-color: rgb(255,255,255); -fx-font-family: Futura; -fx-font-size: 13 px");
        btn.setText("PROCEED");

        Pane root = new Pane();

        btn.setPrefWidth(150);
        btn.setLayoutX(225);
        btn.setLayoutY(360);
        btn.setDefaultButton(true);
        btn.setDisable(true);
        btn.setStyle("-fx-background-color:rgb(242,5,203); " +
                "-fx-font-family: Futura; -fx-font-size: 12 px; -fx-text-fill: rgb(255,255,255); " +
                "-fx-cursor: hand");

        tf.setPrefWidth(200);
        tf.setPrefHeight(50);
        tf.setLayoutX(200);
        tf.setLayoutY(250);
        tf.setAlignment(Pos.CENTER);

        lbl.setPrefWidth(300);
        lbl.setLayoutY(320);
        lbl.setLayoutX(150);
        lbl.setAlignment(Pos.CENTER);
        lbl.setVisible(false);

        ImageView logo = new ImageView(new Image("Logo.png"));

        logo.setScaleX(logo.getScaleX() * 0.65);
        logo.setScaleY(logo.getScaleY() * 0.65);
        logo.setLayoutX(-100);
        logo.setLayoutY(50);

        logo.setVisible(true);

        root.getChildren().addAll(btn, tf, lbl, logo);
        root.setStyle("-fx-background-color: rgb(5,2,89)");
        Scene scene1 = new Scene(root, 600, 420);

        btn.setOnAction(event -> {
            num = Integer.parseInt(tf.getText());
            grid = new Grid(num);

            goToScene2();
        });

        tf.setOnKeyReleased(event -> {

            int val = -1;

            try {
                val = Integer.parseInt(tf.getText());
            } catch (NumberFormatException e) {

            }

            if (val < 8 || val > 64) {
                btn.setDisable(true);
                lbl.setVisible(true);
            }

            else {
                btn.setDisable(false);
                lbl.setVisible(false);
            }

        });

        pStage.setTitle("Space Maze");
        pStage.setScene(scene1);
        pStage.centerOnScreen();
        pStage.show();
        pStage.setResizable(false);
        pStage.setOnCloseRequest(event -> {
            System.exit(1);
        });
    }

    private void goToScene2() {

        GridPane gridPane = new GridPane();
        panes = new AnchorPane[num][num];

        main = new AnchorPane();

        gridPane.setPrefHeight((1000 - 75));
        gridPane.setPrefWidth((1000 - 75));
        gridPane.setMaxHeight((1000 - 75));
        gridPane.setMaxWidth((1000 - 75));
        gridPane.setMinHeight((1000 - 75));
        gridPane.setMinWidth((1000 - 75));

        gridPane.setTranslateY((1000) / 2 - (1000 - 75) / 2);
        gridPane.setTranslateX((1000) / 2 - (1000 - 75) / 2);

        // Background color
        main.setStyle("-fx-background-color: rgb(5, 2, 89)");

        for (int i = 0; i < num; i++)
            for (int j = 0; j < num; j++) {
                panes[i][j] = new AnchorPane();

                // Initial board state
                // start
                if (i == 0 && j == 0)
                    panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(255,159,248)");
                // goal
                else if (i == num - 1 && j == num - 1)
                    panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(242,169,81)");
                // wall
                else
                    panes[i][j].setStyle("-fx-border-color: rgb(5,2,89); -fx-background-color: rgb(124,5,242); " +
                            "-fx-cursor: hand");

                panes[i][j].setPrefWidth((1000 - 75) / num);
                panes[i][j].setPrefHeight((1000 - 75) / num);
                panes[i][j].setVisible(true);

                if (!(i == 0 && j == 0 || i == num - 1 && j == num - 1)) {

                    final int row = i;
                    final int col = j;

                    panes[row][col].setOnMouseClicked(event -> {
                        if (grid.getGrid()[row][col].getType() == Cell.SPACE) {
                            grid.getGrid()[row][col].setType(Cell.WALL);
                        } else if (grid.getGrid()[row][col].getType() == Cell.WALL) {
                            grid.getGrid()[row][col].setType(Cell.SPACE);
                        }

                        updateGUI();
                    });

                }

                gridPane.add(panes[i][j], i, j, 1, 1);
            }


        confirm = new Button("START");
        reset = new Button("RESET");
        replay = new Button("REPLAY");
        startNew = new Button("START NEW SIMULATION");

        confirm.setStyle("-fx-background-color:rgb(242,5,203); " +
                "-fx-font-family: Futura; -fx-font-size: 12 px; -fx-text-fill: rgb(255,255,255); " +
                "-fx-cursor: hand");
        reset.setStyle("-fx-background-color:rgb(242,5,203); " +
                "-fx-font-family: Futura; -fx-font-size: 12 px; -fx-text-fill: rgb(255,255,255); " +
                "-fx-cursor: hand");
        replay.setStyle("-fx-background-color:rgb(242,5,203); " +
                "-fx-font-family: Futura; -fx-font-size: 12 px; -fx-text-fill: rgb(255,255,255); " +
                "-fx-cursor: hand");
        startNew.setStyle("-fx-background-color:rgb(242,5,203); " +
                "-fx-font-family: Futura; -fx-font-size: 12 px; -fx-text-fill: rgb(255,255,255); " +
                "-fx-cursor: hand");

        //confirm.setStyle("-fx-cursor: hand");
        confirm.setDefaultButton(true);

        confirm.setPrefWidth(180);
        confirm.setPrefHeight(50);

        reset.setPrefWidth(180);
        reset.setPrefHeight(50);

        replay.setPrefWidth(180);
        replay.setPrefHeight(50);
        replay.setVisible(false);

        startNew.setPrefWidth(180);
        startNew.setPrefHeight(50);
        startNew.setVisible(false);

        confirm.setTranslateY(1000 - 175);
        confirm.setTranslateX(1092);

        reset.setTranslateY(1000 - 95);
        reset.setTranslateX(1092);

        replay.setTranslateY(1000 - 175);
        replay.setTranslateX(1092);

        startNew.setTranslateY(1000 - 95);
        startNew.setTranslateX(1092);

        found = new Label("Goal found!");
        found.setTextFill(Color.web("ffffff"));
        found.setStyle("-fx-font-family: Futura; -fx-font-size: 18 px");

        notFound = new Label("Failed to find goal.");
        notFound.setTextFill(Color.web("ffffff"));
        notFound.setStyle("-fx-font-family: Futura; -fx-font-size: 18 px");

        found.setTranslateY(650);
        found.setTranslateX(1092);
        found.setVisible(false);

        notFound.setTranslateY(650);
        notFound.setTranslateX(1092);
        notFound.setVisible(false);

//        timelbl = new Label("Time Elapsed: ");
//        timeLbl.setTextFill(Color.web("ffffff"));
//        timeLbl.setStyle("-fx-font-family: Futura; -fx-font-size: 18 px");
//
//        timeLbl.setTranslateY(550);
//        timeLbl.setTranslateX(1092);
//        timeLbl.setVisible(false);

        ImageView logo = new ImageView(new Image("Logo 2.png"));
        logo.setScaleX(logo.getScaleX() * 0.75);
        logo.setScaleY(logo.getScaleY() * 0.75);
        logo.setTranslateX(975);

        main.getChildren().addAll(gridPane, confirm, reset, replay, startNew, found, notFound, logo);

        Scene scene2 = new Scene(main, 1400, 1000);

        reset.setOnAction(event -> {
            for (int i = 0; i < num; i++)
                for (int j = 0; j < num; j++)
                    if (!(i == 0 && j == 0 || i == num - 1 && j == num - 1))
                        grid.getGrid()[i][j].setType(Cell.SPACE);

            updateGUI();
        });

        confirm.setOnAction(event -> {
            tempGrid = grid;

            confirm.setVisible(false);
            reset.setVisible(false);

            replay.setVisible(true);
            replay.setDisable(true);
            startNew.setVisible(true);
            startNew.setDisable(true);

            removeActions();
            showCounter();

            openList = new ArrayList<>();
            closedList = new ArrayList<>();
            finalPath = new ArrayList<>();

            start = grid.getTile(0, 0);
            goal = grid.getTile(num-1, num-1);
            start.setHeuristic(0);
            start.setPathCost(0);
            start.setTotal(0);
            goal.setHeuristic(0);
            goal.setPathCost(0);
            goal.setTotal(0);
            openList.add(start);

            long sTime = System.currentTimeMillis();

            Label time = new Label("Time Elapsed: ");
            time.setTextFill(Color.web("ffffff"));
            time.setStyle("-fx-font-family: Futura; -fx-font-size: 18 px");

            time.setTranslateY(575);
            time.setTranslateX(1092);
//        time.setVisible(false);

            new AnimationTimer() {

                @Override
                public void handle(long now) {

                    if (bFinished)
                        this.stop();

                    double elapsed = (System.currentTimeMillis() - sTime) / 1000.0;

                    if (elapsed < 1 || elapsed > 1)
                        time.setText(elapsed + " seconds");
                    else
                        time.setText(elapsed + " second");

                }

            }.start();

            main.getChildren().add(time);
            
            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleWithFixedDelay(this, 100, 50, TimeUnit.MILLISECONDS);

        });

        replay.setOnAction(event -> {

            grid = new Grid(num);

            for (int i = 0; i < num; i++) {
                for (int j = 0; j < num; j++) {
                    if (tempGrid.getTile(i, j).getType() == Cell.WALL)
                        grid.getTile(i, j).setType(Cell.WALL);
                }
            }

            replayCounter = 0;
            updateGUI();

            replay.setDisable(true);
            startNew.setDisable(true);

            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleWithFixedDelay(this::replayMove, 100, 50, TimeUnit.MILLISECONDS);
        });

        startNew.setOnAction(event -> goToScene1());

        pStage.setScene(scene2);
        pStage.centerOnScreen();
        pStage.setResizable(false);
    }

    private void replayMove() {

        if (replayCounter == pathcount) {
            executor.shutdownNow();
            displayReset(true);
        }

        Cell temp = finalPath.get(replayCounter);

        grid.getTile(temp.getRow(), temp.getCol()).setType(Cell.PATH);
        updateGUI();
        replayCounter++;

    }

    private void removeActions() {

        for (int i = 0; i < num; i++)
            for (int j = 0; j < num; j++) {

                panes[i][j].setStyle("-fx-cursor: default");

                panes[i][j].setOnMouseClicked(event -> {});
            }
        updateGUI();
    }

    private void showCounter() {

        FlowPane fp = new FlowPane();

        pathlbl = new Label("Path Count: 0");
        explbl = new Label("Cells Explored: 0");
        timelbl = new Label("Time Elapsed: ");

        pathlbl.setTextFill(Color.web("ffffff"));
        explbl.setTextFill(Color.web("ffffff"));
        timelbl.setTextFill(Color.web("ffffff"));
        pathlbl.setStyle("-fx-font-family: Futura; -fx-font-size: 18 px");
        explbl.setStyle("-fx-font-family: Futura; -fx-font-size: 18 px");
        timelbl.setStyle("-fx-font-family: Futura; -fx-font-size: 18 px");

        pathcount = 0;
        exploredCount = 0;

        fp.getChildren().addAll(pathlbl, explbl, timelbl);

        fp.setPrefWidth(10);

        fp.setTranslateY(500);
        fp.setTranslateX(1092);

        main.getChildren().addAll(fp);
        main.setVisible(true);
    }

    public void run () {

        if (!openList.isEmpty()) {

            Cell current = openList.get(0);
            int currentIndex = 0;

            for (Cell item : openList) {
                if (item.getTotal() < current.getTotal()) {
                    current = item;
                    currentIndex = openList.indexOf(item);
                }
            }
            
            if (grid.getGrid()[current.getRow()][current.getCol()].getType() != Cell.VISITED) {
                grid.getGrid()[current.getRow()][current.getCol()].setType(Cell.VISITED);
                exploredCount++;
                pathcount = current.getPathCost();
                Platform.runLater(() -> explbl.setText("Cells explored: " + exploredCount));
                Platform.runLater(() -> pathlbl.setText("Path cost: " + pathcount));
            }

            openList.remove(currentIndex);
            closedList.add(current);

            if (current.equals(goal)) {
                bFinished = true;
                do {
                    grid.getGrid()[current.getRow()][current.getCol()].setType(Cell.PATH);
                    updateGUI();
                    finalPath.add(current);
                    current = current.parent;
                } while (current != null);
                Collections.reverse(finalPath);
                executor.shutdownNow();
                updateGUI();
                displayReset(true);
                return;                
            }

            ArrayList<Cell> adjacents = grid.getAdjacentCells(current);
            outer : for (Cell child : adjacents) {
                    child = new Cell(current, child.getRow(), child.getCol());
                    if (closedList.contains(child)) {//
                        continue;
                    }

                    child.setPathCost(current.getPathCost() + 1);
                    child.computeHeuristic(goal);
                    child.setTotal(child.getPathCost() + child.getHeuristic());

                    for (Cell openNode : openList) {
                        if (child.equals(openNode) && child.getPathCost() > openNode.getPathCost())
                            continue outer;
                    }

                    openList.add(child);
            }
        }
        else {
            executor.shutdownNow();
            bFinished = true;
            updateGUI();
            displayReset(false);
        }
            updateGUI();
    }
    

    private void displayReset(boolean isFound) {

        if (isFound) {
            startNew.setDisable(false);
            replay.setDisable(false);
            found.setVisible(true);
            replay.setDefaultButton(true);
        } else {
            startNew.setDisable(false);
            notFound.setVisible(true);
            startNew.setDefaultButton(true);
        }

    }

     
}