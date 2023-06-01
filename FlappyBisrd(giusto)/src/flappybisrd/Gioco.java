/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flappybisrd;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

/**
 *
 * @author Roberto
 */
public class Gioco extends Task{
    private final ImageView[] tubiSu;
    private final ImageView[] tubiGiu;
    private GiocoTotale pgMov;
    private boolean perso=false;
    private boolean stop=false;
    
    Gioco(ImageView[] tubiSu, ImageView[] tubiGiu){
        this.tubiGiu = tubiGiu;
        this.tubiSu = tubiSu;
        System.out.println(this.tubiSu.length+" "+this.tubiGiu.length);
    }
    
    //capire perchè quandi si mette qualcosa al limite la finestra si allunga a caso
    
     @Override
    protected Object call() throws Exception {
        //JOptionPane.showMessageDialog(null, "nome2: "+this.getName());
        boolean vicino;
        while(!perso && !this.isCancelled()){
            try{
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {}
            
            
            //xmax = 589.0
            //xda usare = 502
            
            vicino = controlloVicino();
            if(!vicino){
                int posTuboAndato = trovaPosTuboFuoriMappa();
                if(posTuboAndato!=-1){
                    //JOptionPane.showMessageDialog(null, "entrato");
                        int resize = (new java.util.Random()).nextInt(50);
                        switch(new java.util.Random().nextInt(2)){
                            case 0:
                                resize*=-1;
                        }

                        tubiSu[posTuboAndato].setLayoutX(509);
                        tubiSu[posTuboAndato].setLayoutY(tubiSu[posTuboAndato].getLayoutY()+resize);
                        tubiSu[posTuboAndato].setFitHeight(tubiSu[posTuboAndato].getFitHeight()-resize);

                        tubiGiu[posTuboAndato].setLayoutX(509);
                        tubiGiu[posTuboAndato].setFitHeight(tubiGiu[posTuboAndato].getFitHeight()+resize);


                    //JOptionPane.showMessageDialog(null, "fatto due");
                    //bisogna fare che variano l'altezza 
                }
            }
            for(int i=0;i<5;i++){//avanzamento dei tubi
                try{
                    if(tubiSu[i].getLayoutX()>-100)tubiSu[i].setLayoutX(tubiSu[i].getLayoutX()-1);
                }catch(Exception e){}
                try{
                    if(tubiGiu[i].getLayoutX()>-100)tubiGiu[i].setLayoutX(tubiGiu[i].getLayoutX()-1);
                }catch(Exception e){}
            }
            }catch(Exception e){}
        }
        return null;
    }
    
    
    public void stopp(){
        stop=true;
    }
    
    public void ricomincia(){
        stop=false;
    }
    
    public void ferma(){
        perso = true;
    }

    private boolean controlloVicino() {
        for(int i=0;i<5;i++){
            try{
                if(502-tubiSu[i].getLayoutX()<250){// spazio che ci deve esssere tra i tubi ( in questo caso 150)
                    return true;
                }
            }catch(Exception e){}
        }
        return false;
    }

    private int trovaPosTuboFuoriMappa() {
        for(int i=0;i<5;i++){
            try{
                if(tubiSu[i].getLayoutX()<0)return i;
            }catch(Exception e){}
        }
        return -1;
    }

   
    
}
