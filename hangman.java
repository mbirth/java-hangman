import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.math.*;
import java.io.*;                  // für Dateioperationen ("Tupfer, Schere ...")

public class hangman extends Frame
{
    // Globale Variablen
    final static int WND_B=400, WND_H=400;
    RandomAccessFile file;
    int maxdat=0;
    String words[];
    String myword=null;
    char probed[]; // "-----------------------------";
    char alphab[]={'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T',
		    'U','V','W','X','Y','Z','Ä','Ö','Ü'};
    int mistakes=0;
    KL CONTROL;
    char c;

    public hangman()
    {
	String stmp=new String();
	try
	{
	    RandomAccessFile f=new RandomAccessFile("hangman.dat","r");
	    words=new String[50];
	    while ((stmp=f.readLine())!=null && maxdat<50) {
		if (stmp.charAt(0) != '#') {
		    maxdat++;
		    words[maxdat]=stmp;
		}
	    }
	    f.close();
	    while (myword==null)
	    {
		myword=words[(int)(Math.random()*maxdat)+1];
	    }
	}
	catch(IOException ioe)
	{
	    System.out.println("IOException: "+ioe.toString());
	}
	CONTROL=new KL();
	addKeyListener(CONTROL);
	probed=new char[29];
	for (int i=1;i<29;i++) {
	    probed[i]='-';
	}
    }

    public void paint(Graphics g)
    {
	g.setColor(Color.black);
	g.fillRect(0,0,WND_B,WND_H);
	g.setColor(Color.yellow);
	g.drawString("Datensaetze: "+maxdat,40,350);
	g.drawString("Wort: "+myword,40,220);
	g.drawString("Zeichen: "+c,40,230);
	g.drawString("alpha: "+new String(probed),40,250);
    }
    

    class KL implements KeyListener
    {
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e)
	{
	    c=e.getKeyChar();
	    int i;
	    for (i=0;i<29;i++) {
		if (c==alphab[i]) {
		    probed[i]=c;
		}
	    }
	    repaint();
	}
    }

    public static void main(String args[])
    {
	Frame frame=new hangman();
	frame.addWindowListener(new WindowAdapter()
	{
	    public void windowClosing(WindowEvent e)
	    {
		System.exit(0);
	    }
	});
	frame.setTitle("HangMan for Java - © 1998 by Markus Birth");
	frame.setSize(WND_B, WND_H);
	frame.show();
    }
}
