/*
HANGMAN FOR JAVA
(c)1998/99 by Markus Birth <Robo.Cop(a)gmx.net>

This is the first program I wrote in Java. Thanks to
Mr. Fröbel for making me learning Java so quick (We had to
finish our projects for the computer science lessons.)

Things used for the making of this:
-Xemacs with its Revision Control System
-hangman.java from Carlos von Hoyningen-Huene
-some bottles of PEPSI Coke
-hints & tips from Carlos von Hoyningen-Huene
*/

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Character.*;
import java.math.*;    // eigentlich nur für die eine Zufallszahl
import java.io.*;      // für Dateioperationen ("Tupfer, Schere ...")

public class hangman extends Frame {
    // Globale Variablen
    final static int WND_B=400, WND_H=300;
    final int SX=50, SY=50;
    RandomAccessFile file;
    int maxdat=0;
    String words[];
    String myword=null;  // Wort: was es mal werden soll
    char xyword[];        // Wort: xy-ungelöst
    char probed[];
    char alphab[]={'A','B','C','D','E','F','G','H','I','J','K','L','M','N',
                    'O','P','Q','R','S','T','U','V','W','X','Y','Z',
		    'Ä','Ö','Ü'};
    int mistakes=0;  // Anzahl Fehler (MIST!-akes)
    KL CONTROL;
    char c;

    public hangman() {
	String stmp=new String();
	try {
	    RandomAccessFile f=new RandomAccessFile("hangman.dat","r");
	    words=new String[50];
	    while ((stmp=f.readLine())!=null && maxdat<50) {
		if (stmp.charAt(0) != '#') {
		    maxdat++;
		    words[maxdat]=stmp;
		}
	    }
	    f.close();
	    while (myword==null) {
		myword=words[(int)(Math.random()*maxdat)+1];
	    }
	}
	catch(IOException ioe) {
	    System.out.println("IOException: "+ioe.toString());
	}
	CONTROL=new KL();
	addKeyListener(CONTROL);
	xyword=new char[myword.length()];
	for (int i=0;i<myword.length();i++) {
	    xyword[i]='_';
	}
	probed=new char[29];
	for (int i=0;i<29;i++) {
	    probed[i]='-';
	}
    }

    public void paint(Graphics g) {
	// g.drawString("Datensaetze: "+maxdat,40,350);
	// g.drawString("Wort: "+myword,40,200);
	// g.drawString("Zeichen: "+c,40,230);
	g.setColor(Color.black);
	g.fillRect(0,0,WND_B,WND_H);
	g.setColor(Color.yellow);
	g.drawString("Wort: "+new String(xyword),40,215);
	if (mistakes!=-1) g.drawString("alpha: "+new String(probed),40,260);
	if (mistakes!=-1) g.drawString("mist: "+mistakes,40,230);
	UpdateHangMan(g);
    }
    
    public void UpdateHangMan(Graphics g) {
	Toolkit tk=Toolkit.getDefaultToolkit();

	switch(mistakes) {
	case 6:
	    g.drawImage(tk.getImage("images/hm6.gif"),SX,SY,this);
	    g.setColor(Color.red);
	    g.drawString(">>> VERLOREN <<<",WND_B/2-100,WND_H/2+10);
	    g.setColor(Color.white);
	    g.drawString("Das gesuchte Wort war '"+myword+"'!",WND_B/2-100,WND_H/2+25);
	    removeKeyListener(CONTROL);
	    break;
	case 5:
	    g.drawImage(tk.getImage("images/hm5.gif"),SX,SY,this);
	    break;
	case 4:
	    g.drawImage(tk.getImage("images/hm4.gif"),SX,SY,this);
	    break;
	case 3:
	    g.drawImage(tk.getImage("images/hm3.gif"),SX,SY,this);
	    break;
	case 2:
	    g.drawImage(tk.getImage("images/hm2.gif"),SX,SY,this);
	    break;
	case 1:
	    g.drawImage(tk.getImage("images/hm1.gif"),SX,SY,this);
	    break;
	case 0:
	    g.drawImage(tk.getImage("images/hm0.gif"),SX,SY,this);
	    break;
	case -1:
	    g.drawImage(tk.getImage("images/hm.gif"),SX,SY,this);
	    g.setColor(Color.green);
	    g.drawString(">>> GEWONNEN <<<",WND_B/2-100,WND_H/2+20);
	    removeKeyListener(CONTROL);
	    break;
	}

    }

    class KL implements KeyListener {
	public void keyPressed(KeyEvent e) { }
	public void keyReleased(KeyEvent e) { }
	public void keyTyped(KeyEvent e) {
	    c=e.getKeyChar();
	    c=Character.toUpperCase(c);
	    int i;
	    boolean status=false;
	    boolean check=false;
	    for (i=0;i<29;i++) {
		if (c==alphab[i]) {
		    if (probed[i]!=c) probed[i]=c; else check=true;
		}
	    }
	    int underscores=0;
	    for (i=0;i<myword.length();i++) {
		if (c==Character.toUpperCase(myword.charAt(i))) {
		    xyword[i]=myword.charAt(i);
		    status=true;
		}
		if (xyword[i]=='_') underscores++;
	    }
	    if (!status && !check) {
		mistakes++;
	    }
	    if (underscores==0) {
		mistakes=-1;
		System.out.println("Sie haben gewonnen!\n'"+myword+"' war richtig.");
	    }
	    if (mistakes>=6) {
		mistakes=6;
		System.out.println("Schön, wie sie da am Galgen baumeln ...\nWieso sind Sie auch nicht auf '"+myword+"' gekommen?");
	    }
	    repaint();
	}
    }

    public static void main(String args[]) {
	Frame frame=new hangman();
	frame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});
	frame.setTitle("HangMan for Java - \u00a91998 by Markus Birth");
	frame.setSize(WND_B, WND_H);
	frame.show();
	/* Pictures
	   Image pic;
	   pic=Toolkit.getDefaultToolkit().getImage("image.jpg");
	   g.drawImage(pic,0,0,this);
	*/
    }
}
