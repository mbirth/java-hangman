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
    String myword=null;  // Wort: was es mal werden soll
    char xyword[];        // Wort: xy-ungelöst
    char probed[];
    char alphab[]={'A','B','C','D','E','F','G','H','I','J','K','L','M','N',
                    'O','P','Q','R','S','T','U','V','W','X','Y','Z',
		    'Ä','Ö','Ü'};
    int mistakes=0;  // Anzahl Fehler (MIST!-akes)
    int tries=0;     // Anzahl Versuche
    KL CONTROL;
    char c;

    public hangman() {                // Hauptroutine
	String stmp=new String();
	try {
	    int wordcount=0;           // neuer Integer für Wörterzahl
	    int wordseek=0;            // ~ für Zielwort-Position
	    // und jetzt machen wir die Datei auf: "Schwester: Skalpell!"
	    RandomAccessFile f=new RandomAccessFile("hangman.dat","r");
	    while ((stmp=f.readLine())!=null) {  // solange das, was wir lesen, nicht nichts ist ...
		if (stmp.charAt(0) != '#') {      // und da auch kein "#" am Anfang klebt ...
		    wordcount++;                  // zähle es als Wort.
		}
	    }
	    System.out.println("Woerter in Datendatei: "+wordcount);  // Statusbericht
	    while (wordseek==0) {    // Solange wordseek noch 0 ist, tue ...
		wordseek=(int)(Math.random()*wordcount)+1;  // hol' Dir einen Integer-Wert
	    }
	    System.out.println("Ausgewaehltes Wort: #"+wordseek);  // Statusbericht
	    f.seek(0);      // Position auf Dateianfang setzen
	    wordcount=0;    // Wieder auf NULL
	    while ((stmp=f.readLine())!=null) {   // und das ganze wieder von vorn
		if (stmp.charAt(0) != '#') {
		    wordcount++;
		    if (wordcount==wordseek) {     // wenn an Position, die wir suchen ...
			myword=stmp;               // setze myword auf gerade gelesenen String
			break;                    // und raus hier!
		    }
		}
	    }
	    f.close();         // Datei wieder zunähen
	}
	catch(IOException ioe) {    // Falls doch mal ein Fehler auftreten sollte ...
	    System.out.println("IOException: "+ioe.toString());  // Fehlermeldung und tschüß!
	}
	CONTROL=new KL();   // neuer KeyListener: CONTROL
	addKeyListener(CONTROL);  // hinzufügen
	xyword=new char[myword.length()];      // array erstellen
	for (int i=0;i<myword.length();i++) {  // array initialisieren
	    xyword[i]='_';
	}
	probed=new char[29];                // array erstellen
	for (int i=0;i<29;i++) {            // array initialisieren
	    probed[i]='-';
	}
    }

    public void paint(Graphics g) {      // hier die Grafik ...
	// g.drawString("Datensaetze: "+maxdat,40,350);
	// g.drawString("Wort: "+myword,40,200);
	// g.drawString("Zeichen: "+c,40,230);
	g.setColor(Color.black);           // Farbe auf SCHWARZ
	g.fillRect(0,0,WND_B,WND_H);       // Fenster schön SCHWARZ machen!
	g.setColor(Color.yellow);          // und Farbe auf GELB setzen
	g.drawString("Wort: "+new String(xyword),40,215);
	if (mistakes!=-1) g.drawString("Buchstaben: "+new String(probed),40,260);
	if (mistakes!=-1) g.drawString("Fehler: "+mistakes,40,230);
	UpdateHangMan(g);    // Hangman updaten
    }
    
    public void UpdateHangMan(Graphics g) {
	Toolkit tk=Toolkit.getDefaultToolkit();   // Toolkit (für Grafikdatei-Support) zuweisen

	switch(mistakes) {   // CASE mistakes of ...
	case 6:
	    g.drawImage(tk.getImage("images/hm6.gif"),SX,SY,this);
	    g.setColor(Color.red);
	    g.drawString(">>> VERLOREN <<<",WND_B/2-100,WND_H/2+10);
	    g.setColor(Color.white);
	    g.drawString("Das gesuchte Wort war '"+myword+"'!",WND_B/2-100,WND_H/2+25);
	    removeKeyListener(CONTROL);   // Tastenkontrolle abschalten
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
	    c=e.getKeyChar();              // Taste holen
	    c=Character.toUpperCase(c);    // Buchstabe(?) evtl. GROß machen
	    int i;                        // Wir brauchen bald ein Iiiiihh
	    boolean status=false;        // Booleans
	    boolean check=false;         // für versch. Status-Werte
	    for (i=0;i<29;i++) {
		if (c==alphab[i]) {        // wenn c = einer der Buchst. des Alphabets ist ...
		    if (probed[i]!=c) probed[i]=c; else check=true;  // und der auch noch nicht vorher getippt wurde, dann ... u.s.w.
		}
	    }
	    int underscores=0;            // Integer für Anzahl der "_" im bisher gepuzzleten Wort
	    for (i=0;i<myword.length();i++) {
		if (c==Character.toUpperCase(myword.charAt(i))) {
		    xyword[i]=myword.charAt(i);
		    status=true;
		}
		if (xyword[i]=='_') underscores++;
	    }
	    if (!status && !check) mistakes++;  // wenn falscher Buchstabe und Buchst. nicht schonmal getippt: mistakes+1;
	    if (!check) tries++;   // solange nicht doppelter Tip: tries+1;
	    if (underscores==0) {   // wenn keine fehlenden Zeichen im Lösungswort ...
		mistakes=-1;        // mistakes auf -1 und blabla ausgeben
		System.out.println("Sie haben gewonnen!\n'"+myword+"' war richtig.");
	    }
	    if (mistakes>=6) {      // wenn mehr als 5 Fehler ...
		mistakes=6;          // Fehler=6 und blabla ausgeben
		System.out.println("Schön, wie sie da am Galgen baumeln ...\nWieso sind Sie auch nicht auf '"+myword+"' gekommen?");
	    }
	    repaint();        // Grafikfenster neuzeichnen
	}
    }

    public static void main(String args[]) {
	Frame frame=new hangman();       // neues Fenster
        frame.addWindowListener(new WindowAdapter() {   // WindowListener hinzufügen
	    public void windowClosing(WindowEvent e) {  // wenn auf X geklickt:
		System.exit(0);       // Programm beenden.
	    }
	});
	frame.setTitle("HangMan for Java - \u00a91998 by Markus Birth");  // Titel setzen
	frame.setSize(WND_B, WND_H);   // Größe setzen
        frame.show();                  // und ab auf den Bildschirm damit!
	/* Pictures
	   Image pic;
	   pic=Toolkit.getDefaultToolkit().getImage("image.jpg");
	   g.drawImage(pic,0,0,this);
	*/
    }
}
