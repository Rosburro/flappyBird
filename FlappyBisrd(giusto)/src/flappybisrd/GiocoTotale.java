/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flappybisrd;

import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

/**
 *
 * @author Roberto
 */


/*

ho fatto tutto in un solo thread (quasto) poichè con due mi diceva che c'era un nulla pointer ad un arrayList
nel thread "thread" che presumibilmente è quello della finesta (l'errore non sussisteva se si faceva partire solo uno dei due thread.
quindi per comodità ho optato per farne solo uno.
vorrei capire perchè non va.

*/

/*legenda:
tuboSu = tubo che è un basso ed ha il tubo che punta verso l'alto
opposto è il tuboGiu
*/
public class GiocoTotale extends Thread{
    private ImageView pg;
    private final ImageView[] tubiSu;
    private final ImageView[] tubiGiu;
    private static Label  vScore;
    private boolean stop=false;
    private boolean perso=false;
    private int salto=0;
    private int score=0;
    private float anvanzoDifficlta=0;
    private int vicino=270;//indica quanto lontadi devono essere le colonne di tubi l'una dall'altra
    private int resize;
    
    private int xPosMaxTubi = 710;
    private int a;
    
    GiocoTotale(ImageView pg, ImageView[] tubiSu, ImageView[] tubiGiu, Label vScore){
        this.pg=pg;
        this.tubiGiu = tubiGiu;
        this.tubiSu = tubiSu;
        this.vScore=vScore;
    }
    
    @Override
    public void run(){
        //JOptionPane.showMessageDialog(null, "nome: "+this.getName());
        
        while(!perso()) {//fa muovere il pg
            
            /*while(stop && !perso){//per stoppare il tutto
                System.out.print("");
            }// per quando il gioco si mette in pausa*/
            
            //parte del salto
            //movimento pg
            while(stop){
                System.out.print("");
            }
            
            movimentoPg();
            if(!perso() && !stop)movimentoScena();
            if(!perso() && !stop)aggiornaScore();
           /* while(!vScore.getText().equals(Integer.toString(score))){//per far aspettare che lo score venga cambiato
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GiocoTotale.class.getName()).log(Level.SEVERE, null, ex);
                }
            }*/
            //System.out.print(score);
            //fine movimento pg
        }
        //JOptionPane.showMessageDialog(null, "GAME OVER\nPunteggio: "+score);
        
    }
    
    public void stoppaRiparti(boolean stoppa){//stoppa e unstppa
        stop=stoppa;
    }
    
    public int getScore(){return this.score;}
    
    public void ferma(){
        perso = true;
    }
    public boolean isStopped(){return stop;}

    private boolean controlloPerso() {//analizza tutta la situa
        return false;
    }
    public void salto(){
        salto=50;
    }

    private void movimentoPg() {
        if(salto>0)Platform.runLater(()->{pg.setRotate(-40);});// così l'animazione è più fluida; magari fare che fa uno scattino tra le rotazioni cos' che sembri non troppo statico
            
            while(salto>0 && !perso() && !stop){
                
                Platform.runLater(()->{pg.setLayoutY(pg.getLayoutY()-1.2);});
                
                salto--;
                movimentoScena();
                aggiornaScore();
                try {
                    Thread.sleep(3);
                } catch (InterruptedException ex) {}
            }
            if(perso() || stop)return;
            //parte in cui cade
            
            
            if(salto<=-30){
                Platform.runLater(()->{pg.setLayoutY(pg.getLayoutY()+1.3);});
                
                if(pg.getRotate()<50){
                    Platform.runLater(()->{pg.setRotate(pg.getRotate()+1);});
                    
                }
            }else{
                salto--;
            }
            try {
                Thread.sleep(3);
            } catch (InterruptedException ex) {}
    }

    private void movimentoScena() {
        boolean vicino;
        vicino = controlloVicino();
            if(!vicino){
                int posTuboAndato = trovaPosTuboFuoriMappa();
                if(posTuboAndato>=0){
                    //JOptionPane.showMessageDialog(null, "entrato");
                        
                        
                        resize = (new java.util.Random()).nextInt(100);//di quanto vanno su e giù
                        switch(new java.util.Random().nextInt(2)){
                            case 0:
                                resize*=-1;
                        }
                        if(anvanzoDifficlta<100)anvanzoDifficlta+=0.5;//diminuzione dello spazio tra i tubi (colonna(tuboSu e tuboGiu))
                        if(this.vicino>150)this.vicino-=1;//di quanto diminuisce lo spazio ch deve intercorrere tra il tubo e il bordo per la comparsa di un altro tubo
                        
                        //Platform.runLater(()->{tubiGiu[posTuboAndato].setLayoutY(0);});
                        //Platform.runLater(()->{tubiSu[posTuboAndato].setFitHeight(129+anvanzoDifficlta);});//aggiungi (per diminuire ) sottrai (per aumentare) P.S. non funziona troppo bisogna modificare il fitheight per far si che il tutto sia messo per bene
                        //Platform.runLater(()->{tubiSu[posTuboAndato].setLayoutY(340-anvanzoDifficlta);});//sottrai (per diminuire) aggiungi (per aumentare)
                        //129
                        Platform.runLater(()->{tubiSu[posTuboAndato].setLayoutX(xPosMaxTubi);});
                        //mantieni le proorzioni per l'incremento della difficoltà
                        Platform.runLater(()->{tubiSu[posTuboAndato].setLayoutY(340-anvanzoDifficlta+resize);});
                        //Platform.runLater(()->{tubiSu[posTuboAndato].setFitHeight(tubiSu[posTuboAndato].getFitHeight()-resize);});
                        
                        Platform.runLater(()->{tubiGiu[posTuboAndato].setLayoutY(-400+anvanzoDifficlta+resize);});
                        Platform.runLater(()->{tubiGiu[posTuboAndato].setLayoutX(xPosMaxTubi);});
                       // Platform.runLater(()->{tubiGiu[posTuboAndato].setFitHeight(tubiGiu[posTuboAndato].getFitHeight()+resize);});
                        
                        
                    //JOptionPane.showMessageDialog(null, "fatto due");
                    //bisogna fare che variano l'altezza 
                }
            }
            a=0;
            boolean fallito=false;
            for( a=0;a<5;a++){//avanzamento dei tubi
                if(tubiSu[a].getLayoutX()>-100){
                    //Platform.runLater(()->{});
                    //Platform.runLater(()->{});
                    
                    do{
                        fallito=false;
                    try{
                    tubiGiu[a].setLayoutX(tubiGiu[a].getLayoutX()-1);
                    
                    }catch(Exception e){
                        fallito=true;
                        System.out.println("presa");
                    }
                    }while(fallito);
                    do{
                        fallito=false;
                    try{
                        tubiSu[a].setLayoutX(tubiSu[a].getLayoutX()-1);
                    }catch(Exception e){
                        fallito=true;
                        System.out.println("presa");
                    }
                    }while(fallito);
                }

            }
    }
    private boolean controlloVicino() {//controlla se ci sono tubi vicino al bordo destro
        for(int i=0;i<5;i++){
            
                if(xPosMaxTubi-tubiSu[i].getLayoutX()<vicino){// spazio che ci deve esssere tra i tubi ( min circa 100) (P.S. se il tubo è vicino al bordo (destro) di meno del contenuto della variabile vicino allora returna true
                    return true;
                }
            
        }
        return false;
    }

    private int trovaPosTuboFuoriMappa() {
        for(int i=0;i<5;i++){
            
                if(tubiSu[i].getLayoutX()<-75)return i;//quanto il tubo scompare (30 in questo caso)
           
        }
        return -1;
    }
    
    private boolean perso(){
        for(int i=0;i<5;i++){
            if(pg.getLayoutX()>=tubiSu[i].getLayoutX()-tubiSu[i].getFitWidth()/1.5 && pg.getLayoutX()<=(tubiSu[i].getLayoutX()+tubiSu[i].getFitWidth()/1.5)){
                
                    return (controlloHitBox(tubiSu[i], true) || controlloHitBox(tubiGiu[i], false));
            }
        }
        if(pg.getLayoutY()>=420.0)return true;
        return false;
    }

    private void aggiornaScore() {
        for(int i=0;i<5;i++){
            /*FileWriter fw;
            try {
                fw = new FileWriter("dati.txt", true);
                fw.write(tubiSu[i].getLayoutX()+"\n");
                fw.close();
            } catch (IOException ex) {
            }*/
            
            //System.out.println("tubo: "+i+" x: "+tubiSu[i].getLayoutX());
            if(tubiSu[i].getLayoutX()==75){
                this.score++;
                System.out.println(score);
                
                //FXMLDocumentController.aggiornaScore(score);
                //FXMLDocumentController.aggiornaScore(score);
                //System.out.println("\n"+score);
                Platform.runLater(() ->{
                        vScore.setText(score+"");
                });
                
                i=5;
            }
        }
    }

    private boolean controlloHitBox(ImageView tubo, Boolean suGiu) {//true = tubosu // false = tuboGiu ; true = colpito
         if(!suGiu){
            if(pg.getLayoutY()<=551+tubo.getLayoutY()-55){//come numero (in questo caso 551) mettere l'altezza dell'immagine che si è messa proporzionata per il fitWidth che si è messo
                return true;
            }
        }else{
            if(pg.getLayoutY()>=tubo.getLayoutY()-55){
                return true;
            }
        }
        if(pg.getLayoutY()>=420.0)return true;//grass
        return false;
    }
    
}
