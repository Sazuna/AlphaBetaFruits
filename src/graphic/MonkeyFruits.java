package graphic;

import java.awt.Toolkit;
import java.util.concurrent.CompletableFuture;

import game.Board;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import stack.BoardStack;
import tree.Program;
import tree.TC2;
import tree.TreeCumul;
import tree.TreeIut;
import tree.TreeIutLong;
import tree.TreeIutShort;
import tree.TreeSpeed;

public class MonkeyFruits extends Application {

    private final byte MAXDEPTH = 7;
    private int SIZE = 100;
    private Board b;

    private Canvas[][] grid ;
    private GraphicsContext[][] gc ;
    private VBox[] col;
    private HBox cols = new HBox();

    private Canvas[] line = new Canvas[9];
    private GraphicsContext[] gc2 = new GraphicsContext[9];
    private HBox next = new HBox();

    private Text score = new Text();

    private Automatic automatic;
    private PlayAgainst playAgainst;
    Thread play;
    private boolean isComputerThinking = false;

    public Program tree;
    public Program tree1;
    public Program tree2;
    private Text eval;

    public BoardStack bs;
    private Stage primary;
    //private final Alert alert = new Alert(primary);

    @Override
    public void start(Stage primaryStage) {
        primary = primaryStage;
        this.SIZE = (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 15);
        //init global
        bs = new BoardStack();
        b = new Board(false, (byte) 9);
        b.aleaNexts();
        bs.stack.add(b.copy());
        grid = new Canvas[b.WIDTH][b.HEIGHT];
        gc = new GraphicsContext[b.WIDTH][b.HEIGHT];
        col = new VBox[b.WIDTH];
        tree = new TreeSpeed(); // Best current tree.
        tree1 = new TC2();
        tree2 = new TreeIutShort();
        playAgainst = new PlayAgainst(b, tree, this);
        automatic = new Automatic(b, tree, tree, this);

        score.textProperty().addListener((observable, oltText, newText) -> {
            //    System.out.println(Thread.currentThread());
            if (b.getScore().win())
                alertWin();
            else if (b.getScore().lose()) {
                alertLose();
            }
        });

        for (byte i = 0; i < grid.length; i++)
            for (byte j = 0; j < grid[0].length; j++) {
                Byte x = new Byte(i);
                Byte y = new Byte(j);
                grid[i][j] = new Canvas();
                grid[i][j].setWidth(SIZE);
                grid[i][j].setHeight(SIZE);
                gc[i][j] = grid[i][j].getGraphicsContext2D();
                gc[i][j].setFill(Color.web("#888888"));
                gc[i][j].fillRect(0, 0, SIZE, SIZE);
                grid[i][j].setOnMouseClicked(new EventHandler<MouseEvent>()  {

                    @Override
                    public void handle(MouseEvent e) {
                        if (isComputerThinking) {
                            alertThinking();
                            return;
                        }
                        MouseButton btn = e.getButton();
                        if (btn == MouseButton.PRIMARY) {
                            b.play(x, b.getNexts()[0]);
                            bs.stack.push(b.copy());
                            actualize();
                        }

                        else {
                            showFruits(primaryStage, b, x, y);
                        }

                    }

                });
            }
        for (int i = 0; i < col.length; i++)
            col[i] = new VBox();
        for (int i = 0; i < b.WIDTH; i++)
            for (int j = 0; j < b.HEIGHT; j++)
                col[i].getChildren().add(grid[i][j]);
        BorderPane bp = new BorderPane();
        for (int i = 0; i < col.length; i++)
            cols.getChildren().add(col[i]);
        for (byte i = 0; i < line.length; i++) {
            Byte x = i;
            Byte y = -1;
            line[i] = new Canvas();
            line[i].setWidth(SIZE);
            line[i].setHeight(SIZE);
            line[i].setOnMouseClicked(new EventHandler<MouseEvent>()  {
                @Override
                public void handle(MouseEvent e) {
                    if (isComputerThinking) {
                        alertThinking();
                        return;
                    }
                    MouseButton btn = e.getButton();
                    if (btn == MouseButton.SECONDARY) {
                        showFruits(primaryStage, b, x, y);
                    }

                }
            });
            gc2[i] = line[i].getGraphicsContext2D();
            if (i == 0)
                gc2[i].setFill(Color.web("#ffffff"));
            else
                gc2[i].setFill(Color.web("#888888"));
            gc2[i].fillRect(0, 0, SIZE, SIZE);
            next.getChildren().add(line[i]);
        }
        HBox menu = new HBox();
        menu.getChildren().add(score);
        Button m1 = new Button("Automatic");
        m1.setOnMouseClicked(new EventHandler( ) {
            public void handle(Event arg0) {
                if (!isComputerThinking) {
                    new Thread(automatic).start();
                }
                else {
                    alertThinking();
                    return;
                }
            }

        });


        EventHandler ev = new EventHandler() {
            @Override
            public void handle(Event arg0) {
                class Run implements Runnable {
                    @Override
                    public synchronized void run() {
                        if (isComputerThinking) {
                            alertThinking();
                            return;
                        }
                        protect();
                        byte bestMove = tree.bestMove(b);
                        b.play(bestMove);
                        bs.stack.push(b.copy());
                        actualize();
                        unprotect();
                    }

                }
                new Thread(new Run()).start();
            }
        };


        Button m2 = new Button("Generate");
        m2.setOnMouseClicked(new EventHandler( ) {
            @Override
            public void handle(Event arg0) {
                class Run implements Runnable {
                    @Override
                    public synchronized void run() {
                        if (isComputerThinking) {
                            alertThinking();
                            return;
                        }
                        protect();
                        byte bestMove = tree.bestMove(b);
                        b.play(bestMove);
                        bs.stack.push(b.copy());
                        actualize();
                        unprotect();
                    }

                }
                new Thread(new Run()).start();
            }
        });
        Button m3 = new Button("New Game");
        m3.setOnMouseClicked(new EventHandler( ) {
            @Override
            public void handle(Event arg0) {
                class Run implements Runnable {
                    @Override
                    public synchronized void run() {
                        if (isComputerThinking) {
                            alertThinking();
                            return;
                        }
                        byte[] nexts = b.getNexts();
                        score.setText(" 0-0 ");
                        b = new Board(false, b.NEXTS);
                        b.setNexts(nexts);
                        bs.stack.add(b.copy());
                        playAgainst.setBoard(b);
                        //b.aleaNexts();
                        newAutomatic();
                        actualize();
                    }

                }
                new Thread(new Run()).start();
            }
        });

        Button m4 = new Button("Play");
        m4.setOnMouseClicked(new EventHandler() {
            public void handle(Event arg0) {
                if (isComputerThinking) {
                    alertThinking();
                    return;
                }
                playAgainst.setComputer(b.getToMove());
                play = new Thread(playAgainst);
                play.start();
            }
        });

        Button m5 = new Button("Undo");
        m5.setOnMouseClicked(new EventHandler() {
            public void handle(Event arg0) {
                if (isComputerThinking) {
                    alertThinking();
                    return;
                }
                undo();
            }
        });

        menu.getChildren().add(m1);
        menu.getChildren().add(m2);
        menu.getChildren().add(m3);
        menu.getChildren().add(m4);
        menu.getChildren().add(m5);
        bp.setCenter(cols);
        bp.setBottom(next);
        bp.setTop(menu);
        bp.setPadding(new Insets(20));
        eval = new Text(tree.toString());
        bp.setRight(eval);
        actualize();
        Scene sc = new Scene(bp);
        primaryStage.setScene(sc);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {
                if (play != null && play.isAlive()) {
                    System.out.println("alive");
                    primaryStage.close();
                }
            }

        });

        sc.setOnKeyPressed((final KeyEvent keyEvent )-> {
            if (KeyCode.G == keyEvent.getCode()) {//generate
                if (isComputerThinking) {
                    alertThinking();
                    return;
                }
                protect();
                byte bestMove = tree.bestMove(b);
                b.play(bestMove);
                bs.stack.push(b.copy());
            }
            else if (KeyCode.R == keyEvent.getCode()) {
                b.setLast((byte)2);
            }
            else if (KeyCode.V == keyEvent.getCode()) {
                b.setLast((byte)3);
            }
            else if (KeyCode.J == keyEvent.getCode()) {
                b.setLast((byte)1);
            }
            else if (KeyCode.B == keyEvent.getCode()) {
                b.setLast((byte)0);
            }
            actualize();
            unprotect();
        });
    }



    public static void main(String[] args) {
        launch(args);
    }


    public void actualize() {
        score.setText(b.getScore().toString());
        byte[][] position = b.getPosition();
        for (int i = 0; i < b.WIDTH; i++)
            for (int j = 0; j < b.HEIGHT; j++) {
                GraphicsContext g = gc[i][j];
                byte fruit = position[b.HEIGHT - j - 1][i];
                fillrect(fruit, g, 1);
            }
        byte[] nexts = b.getNexts();
        for (int i = 0; i < b.NEXTS; i++) {
            GraphicsContext g = gc2[i];
            byte fruit = nexts[i];
            fillrect(fruit, g, i == 0 ? 5 : 1);
        }
        eval.setText(tree.toString());
    }

    private void fillrect(byte fruit, GraphicsContext g, int margin) {
        if (fruit == -1) {
            g.setFill(Color.WHITE);
            g.fillRect(margin, margin, SIZE - 2* margin, SIZE - 2* margin);
        }
        else if (fruit == 0) {
            g.setFill(Color.web("#7755ff"));
            g.fillRect(margin, margin, SIZE - 2* margin, SIZE - 2* margin);
        }
        else if (fruit == 1) {
            g.setFill(Color.web("#ffff33"));
            g.fillRect(margin, margin, SIZE - 2* margin, SIZE - 2* margin);
        }
        else if (fruit == 2) {
            g.setFill(Color.web("#ff70b0"));
            g.fillRect(margin, margin, SIZE - 2* margin, SIZE - 2* margin);
        }
        else {
            g.setFill(Color.web("#33ff55"));
            g.fillRect(margin, margin, SIZE - 2* margin, SIZE - 2* margin);
        }
    }

    public void protect() {
        this.isComputerThinking = true;
    }

    public void unprotect() {
        this.isComputerThinking = false;
    }


    private void showFruits(Stage primaryStage, Board board, final Byte x, final Byte y) {
        HBox hb = new HBox();
        for (byte i = -1; i <= 3; i++) {
            final Byte fruit = i;
            Canvas can = new Canvas();
            can.setWidth(SIZE);
            can.setHeight(SIZE);
            GraphicsContext gc = can.getGraphicsContext2D();
            fillrect(i, gc, 1);
            hb.getChildren().add(can);
            can.setOnMouseReleased(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent arg0) {
                    if (!isComputerThinking) {
                        if (y >= 0) {
                            board.pop(fruit, x, (byte) (b.HEIGHT - y - 1));
                            board.fallPop();
                            actualize();
                        }
                        else {
                            board.popNext(fruit, x);
                            actualize();
                        }
                    }
                    else {
                        alertThinking();
                    }
                }

            });
        }
        Scene sc = new Scene(hb);
        Stage st = new Stage();

        st.initModality(Modality.WINDOW_MODAL);
        st.initOwner(primaryStage);
        sc.setOnKeyPressed((final KeyEvent keyEvent )-> {
            if (KeyCode.SPACE == keyEvent.getCode() || KeyCode.ESCAPE == keyEvent.getCode())
                st.close();
        });
        sc.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                st.close();
            }

        });

        st.setScene(sc);
        st.setX(primaryStage.getX() + (x - 2) * SIZE );
        byte y2 = y;
        if (y < 0) {
            y2 = (b.HEIGHT);
        }
        st.setY(primaryStage.getY() + y2 * SIZE);
        st.show();
    }

    private void newAutomatic() {
        automatic = new Automatic(b, tree, tree, this);
    }

    private void undo() {
        System.out.println(bs.stack.size());
        b = bs.stack.pop();
        // b = bs.stack.lastElement();
        actualize();
    }



    public void alertThinking() {
        alert("Computer is thinking.");
    }

    public void alertWin() {
        /*CompletableFuture.runAsync(() -> {
            alert("Congratulations, you won !");
        }, Platform::runLater).join();*/
        System.out.println("Won");
    }

    public void alertLose(){
        /*CompletableFuture.runAsync(() -> {
            alert("You lost... Try again !");
        }, Platform::runLater).join();*/
        System.out.println("Lost");
    }

    public void alert(String message) throws java.lang.IllegalStateException  {
        Text t = new Text(message);
        HBox hb = new HBox(t);
        hb.setPadding(new Insets(20));
        Scene sc = new Scene(hb);
        Stage st = new Stage();
        st.initModality(Modality.WINDOW_MODAL);
        st.initOwner(primary);
        sc.setOnKeyPressed((final KeyEvent keyEvent )-> {
            if (KeyCode.SPACE == keyEvent.getCode() || KeyCode.ESCAPE == keyEvent.getCode())
                st.close();
        })
        ;
        sc.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                st.close();
            }

        });
        st.setScene(sc);
        st.show();
    }

}
