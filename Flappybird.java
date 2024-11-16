import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class FlappyBird extends JPanel  implements ActionListener,  KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    // Images
    Image setBackgroundimage;
    Image birdimage;
    Image toppipeimage;
    Image bottompipeimage;

     // Bird 
     int birdx = boardWidth/7;
     int birdy = boardHeight/2;
     int birdWidth =44 ;
     int birdHeight = 34;
    

      class Bird {
        int x = birdx;
        int y = birdy;
        int Width = birdWidth;
        int Height = birdHeight;
        Image img;

        Bird (Image img){
        this.img = img ; 
        }
    }

       //Pipes
       int pipeX = boardWidth;
       int pipeY= 0;
       int pipeWidth = 64;
       int pipeHeight = 512;

       class Pipe {
        int x = pipeX;
        int y = pipeY;
        int Width = pipeWidth;
        int Height = pipeHeight;
        Image img;
        boolean passed = false ;

        Pipe(Image img){
            this.img = img ;
        }
       }

       //Game logic 
        Bird bird ;
        int velocityY = 0 ; // Move bird up and down 
        int velocityX = -4; // Move pipes to the left speed (simulates bird moving right )
        int gravity = 1;

       ArrayList<Pipe> pipes ;
       Random random = new Random();


        Timer gameloop;
        Timer placePipesTimer;
        
        boolean gameOver = false; 
        double score = 0 ;
    // Constructor
    public FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
          
        setFocusable(true);
        addKeyListener(this);  

        // Load images
        setBackgroundimage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdimage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        toppipeimage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompipeimage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        
        //bird
        bird = new Bird(birdimage);
        pipes = new ArrayList<Pipe>();


        // place pipe timer 
        placePipesTimer = new Timer(1500,new ActionListener() {
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        });
        placePipesTimer.start();
        
        // game timer 
        gameloop = new Timer(1000/60,this);
        gameloop.start();
    }

    public void placePipes (){
        
        int randomPipeY =   (int ) (pipeY - pipeHeight/4  - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;

        // Top pipe 
        Pipe topPipe = new Pipe (toppipeimage);
        topPipe.y =randomPipeY;
        pipes.add(topPipe);

        // Buttom  pipe 
        Pipe bottomPipe = new Pipe(bottompipeimage);
        bottomPipe.y = topPipe.y +pipeHeight +openingSpace;
        pipes.add(bottomPipe);

    }                             


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Draw background


        g.drawImage(setBackgroundimage, 0, 0, boardWidth, boardHeight, null);
       
        //Bird 
        g.drawImage(birdimage, bird.x, bird.y, bird.Width,bird.Height,null) ;

       // pipes 
       for (int i = 0 ; i< pipes.size(); i++){
       Pipe pipe = pipes.get(i);
       g.drawImage(pipe.img,pipe.x,pipe.y , pipe.Width , pipe.Height,null);
       }

       // Score 
       g.setColor(Color.white);
       g.setFont(new Font ("Arial", Font.PLAIN,32));

       if(gameOver){
            g.drawString("Game Over:"+ String.valueOf((int ) score ),10,35);
       }else {
        g.drawString(String.valueOf((int ) score),10,35);
       }
       
    }

    // move function 
    public void move (){
        // bird 
        velocityY += gravity;  
        bird.y +=velocityY;
        bird.y = Math.max(bird.y,0); 


        // pipes 
        for (int i =0 ; i< pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;
       
         if (!pipe.passed && bird.x > pipe.x + pipeWidth){
            pipe.passed = true ;
            score += 0.5 ;
         }

        if(collision(bird, pipe)){
            gameOver = true ;
        }    
        
     }
        // Game over 
        if (bird.y > boardHeight){

            gameOver = true ;
        }    
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            placePipesTimer.stop();
            gameloop.stop();
        }
    }

   
     public boolean collision(Bird a , Pipe b){
      return a.x <b.x + b.Width &&
             a.x + a.Width > b.x &&
             a.y< b.y + b.Height &&
             a.y + a.Height > b.y ;
     }

    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9; 
            if (gameOver){
                // restarat the game 
                bird.y = birdy;
                velocityY = 0 ;
                pipes.clear();
                score = 0;
                gameOver = false ;
                gameloop.start();
                placePipesTimer.start();
                
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }


    @Override
    public void keyReleased(KeyEvent e) {}

}
