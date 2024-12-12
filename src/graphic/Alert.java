package graphic;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert {
    
    Stage owner;
    public Alert() {
        
    }

    public void alertThinking() {
        alert("Computer is thinking.");
    }

    public void alertWin() {
        alert("Congratulations, you won !");
    }
    
    public void alertLose() {
        alert("You lost... Try again !");
    }

    public void alert(String message) {
        Text t = new Text(message);
        HBox hb = new HBox(t);
        Scene sc = new Scene(hb);
        Stage st = new Stage();
        
        st.initModality(Modality.WINDOW_MODAL);
        st.initOwner(owner);
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
        st.show();
    }

}
