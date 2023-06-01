/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package flappybisrd;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import static javax.swing.text.StyleConstants.Background;


/**
 *
 * @author Roberto
 */

/*
per diminuire la distanza tra i tubi (x) andare nell'inizializza tubi e trovare il commento,
per diminuire la distranza tra i tubi (y) andare nell'inizializza tubi e trovare il commento


allora la label dello score...
funzionano così gli oggetti in fxml
se l'oggetto che è stato inserito dopo di un altro sarà sopra ad altri ma se quell'oggetto alla fine degli inserimenti o anche nel mentre
si mette di fronte ovvero facendo .toFront allora esso sarà davanti a tutti quelli che sono stati creati precedentemente (probabilente nel suo contenitore)

*/
public class FXMLDocumentController implements Initializable {
    
    
    private ImageView player;
    @FXML
    private AnchorPane schermo;
    @FXML
    private Button bottone;
    @FXML
    private Label inizio, pausa, pausaScore, maxScore, gameOver;
    @FXML
    private Rectangle erba;
    @FXML
    private ImageView sfondo;
    private ImageView imbianchino;
    private static Label score;//visualizza lo score
    private ImageView background;
    private final ImageView[] tubiSu = new ImageView[5];
    private final ImageView[] tubiGiu = new ImageView[5];
    public GiocoTotale pgMovimenti;
    //public Thread gioco;
    private Thread controllo;
    private Image su = new Image("tuboSu.png");
    private Image giu = new Image("tuboGiu.png");
    private Image im = new Image("uccellino.png");
    
    private boolean partitTerminata=false;
    private int maxScoreNum=0;
    /*private double width = Toolkit.getDefaultToolkit().getScreenSize().width/2.15;
    private double height = Toolkit.getDefaultToolkit().getScreenSize().height/2;*/
    
    
    @FXML 
    private void inputFinestra(KeyEvent e){//anchorePane    avviso ancora da mandare sulla classe virtuale
        //if(e.getText().equals("SPACE")){
        int a=e.getCharacter().charAt(0);
        
        //System.out.println("pippo"+e.getCharacter()+"ciao "+" val "+a);
        if(e.getCharacter().equals(" ") && !pgMovimenti.isStopped()){//fa saltare il pg
            if(!this.gameOver.isVisible()){
                pgMovimenti.salto();
            }else{
                if(pausaScore.isVisible())this.ricominciaGioco();
            }
        }else if(e.getCharacter().charAt(0)==27 && pgMovimenti.isAlive()){//se ha cliccato esc
            
            pgMovimenti.stoppaRiparti(!pgMovimenti.isStopped());
            visualizzaNascondiPausa(pgMovimenti.isStopped());
            
        }
        //}
    }
    
    private void visualizzaNascondiPausa(boolean visualizza){//true = in pausa , flase = tolta la pausa
        if(visualizza){//fa diventaretrasparente
            
            imbianchino.setOpacity(0.3);
        }else{//diventa visibile normalmente
            
            imbianchino.setOpacity(0);
        }
        pausa.setVisible(visualizza);
        if(visualizza) pausaScore.setText("Score: "+pgMovimenti.getScore());
         pausaScore.setVisible(visualizza);
         maxScore.setVisible(visualizza);
    }
    
    private void visualizzaNascondiGioco(boolean visualizza){
        player.setVisible(visualizza);
        for(int i=0;i<5;i++){
            tubiSu[i].setVisible(visualizza);
            tubiGiu[i].setVisible(visualizza);
        }
        score.setVisible(visualizza);
    }
    
    private void partitaFinita(){
        this.partitTerminata=true;
        System.out.println("partita finita "+inizio.getText());
        //JOptionPane.showMessageDialog(null, "GAME OVER\nPunteggio: "+pgMovimenti.getScore());
        visualizzaNascondiGioco(false);
        gameOver.setLayoutY(-100);
        this.gameOver.setVisible(true);
        int app=200;//di quante y va giu
        do{
            Platform.runLater(()->{gameOver.setLayoutY(gameOver.getLayoutY()+1);});
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {}
            app--;
        }while(app>0);
        try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
        
        this.pausaScore.setVisible(true);
        
        Platform.runLater(()->{this.pausaScore.setText("Score: "+pgMovimenti.getScore());});
        Platform.runLater(()->{schermo.requestFocus();});
        //TextMessageDialog
        //JOptionPane.showMessageDialog(, "GAME OVER\nPunteggio: "+pgMovimenti.getScore());
        
    }
    
    private void ricominciaGioco(){
        this.gameOver.setVisible(false);
        this.pausaScore.setVisible(false);
        bottone.setVisible(true);
        Platform.runLater(()->{bottone.requestFocus();});
        inizio.setVisible(true);
        inizializzaPlayer();
        inizializzaTubi();
        Platform.runLater(()->{score.setText("0");});
        maxScore.setVisible(true);
        maxScoreNum = Math.max(maxScoreNum, pgMovimenti.getScore());
        Platform.runLater(()->{maxScore.setText("Max Score: "+maxScoreNum);});
        //non so perchè ma i thread devo reinstanziarli sennò non funzionano
        Platform.runLater(()->{pgMovimenti.interrupt();});
        Platform.runLater(()->{controllo.interrupt();});
        
        pgMovimenti = new GiocoTotale(player, tubiSu, tubiGiu, score);
        pgMovimenti.setDaemon(true);
        inizializzaControllo();
    }
    
    @FXML
    private void ciao(ActionEvent e){//bottone di inizio
        this.partitTerminata=false;
        bottone.setVisible(false);
        inizio.setVisible(false);
        maxScore.setVisible(false);
        visualizzaNascondiGioco(true);
        pgMovimenti.start();
        controllo.start();
        //gioco.start();
        schermo.requestFocus();
        
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //schermo.setBackground( new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY , Insets.EMPTY), new BackgroundImage(new Image(""), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT));
        
        System.out.println(schermo.getPrefWidth()+" altezza: "+schermo.getPrefHeight());
        //background.toBack();
        //set del player
        schermo.requestFocus();
        player = new ImageView();
        inizializzaPlayer();
        schermo.getChildren().add(player);
        //fine set del player
        
        inizializzaControllo();
        
        maxScore.setText("Max Score: 0");

        
        score = new Label();
        score.setVisible(false);
        score.setLayoutX(202);//più o meno al centro
        score.setLayoutY(20);
        score.setPrefSize(300, 50);
        score.setAlignment(Pos.CENTER);
        //score.setFont(Font.font(24));
        score.setText("0");
        score.toFront();//mette la label ontop
        score.setFont(new Font("arial", 30));
        
        
        imbianchino = new ImageView(new Image("imbianchino.png"));
        imbianchino.setLayoutX(-100);
        imbianchino.setLayoutY(-100);
        imbianchino.setPreserveRatio(false);
        imbianchino.setFitWidth(1000);
        imbianchino.setFitHeight(800);
        imbianchino.setOpacity(0);
        schermo.getChildren().add(imbianchino);
        
        schermo.getChildren().add(score);
        for(int i=0;i<5;i++){
            tubiSu[i] = new ImageView();//all'insu il tubo
            tubiGiu[i] = new ImageView();//all'ingiù il tubo
            
        }
        inizializzaTubi();
        for(int i=0;i<5;i++){
            schermo.getChildren().add(tubiSu[i]);
            schermo.getChildren().add(tubiGiu[i]);
        }
        /*Gioco t2 = new Gioco(tubiSu, tubiGiu);
        gioco = new Thread(t2);
        gioco.setDaemon(true);*/
        pgMovimenti = new GiocoTotale(player, tubiSu, tubiGiu, score);
        //this.pgMovimenti = new Thread(t1);
        pgMovimenti.setDaemon(true);
        
        score.toFront();
        imbianchino.toFront();
        inizio.toFront();
        pausa.toFront();
        pausaScore.toFront();
        this.maxScore.toFront();
        
        erba.toFront();
        
        /*gioco = new Gioco(tubiSu, tubiGiu);
        gioco.setPriority(7);
        gioco.start();*/
        
        // TODO
    }    
    
    private void inizializzaPlayer(){
        player.setVisible(false);
        player.setImage(im);
        player.setPreserveRatio(false);
        player.setFitHeight(100);
        player.setFitWidth(100);
        player.setLayoutX(50);
        player.resize(10, 10);
        player.setLayoutY(200);
    }
    
    private void inizializzaControllo(){//serve a controllare quando la partita finisce
        Task contr = new Task(){
            @Override
            protected Object call() throws Exception {

                pgMovimenti.join();
                partitaFinita();
                return null;
            }
        };
        controllo = new Thread(contr);
        controllo.setDaemon(true);
    }
    
    private void inizializzaTubi(){
        //JOptionPane.showMessageDialog(null, "inizio");
        
        
        for(int i=0;i<5;i++){// se si cambiano i fitHeight delle due immagini si ha un aumento dello spazio tra i tubi
            
            //JOptionPane.showMessageDialog(null, "creazione");
            
            //tubiSu[i].toBack();
            //tubiGiu[i].toBack();
            //tubiSu
            //JOptionPane.showMessageDialog(null, "tubo su");
            
            
            
            tubiSu[i].setImage(su);
            tubiSu[i].setPreserveRatio(true);
            tubiSu[i].setFitWidth(84);
            
            tubiSu[i].setLayoutX(-100);//si mette prima della finesgtra così che non viene visualizzato
            //cambiare il fitheyght e layouty per cambiare lo spazio tra i tubi 
           // tubiSu[i].setFitHeight(260);//aggiungi (per diminuire ) sottrai (per aumentare) 
            tubiSu[i].setLayoutY(340);//sottrai (per diminuire) aggiungi (per aumentare)
            //tubiGiu
            //JOptionPane.showMessageDialog(null, "tubo giu");
            tubiGiu[i].setImage(giu);
            tubiGiu[i].setPreserveRatio(true);
            tubiGiu[i].setFitWidth(84);
           // tubiGiu[i].setFitHeight(150);
            tubiGiu[i].setLayoutX(-100);//si mette prima della finesgtra così che non viene visualizzato
            tubiGiu[i].setLayoutY(-200);
            
            //aggiunta
            //JOptionPane.showMessageDialog(null, "aggiunta");
            
        }
        tubiSu[4].getLayoutX();
        tubiGiu[4].getLayoutX();
    }
    
    
    
    /*public static void aggiornaScore(int nScore){
        if(score.isManaged())score.setText(nScore+"");
    }*/
   
}
