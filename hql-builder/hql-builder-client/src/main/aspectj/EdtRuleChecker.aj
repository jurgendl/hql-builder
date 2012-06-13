import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;


//JTextPane
//replaceSelection()
//insertComponent()
//insertIcon()
//setLogicalStyle()
//setCharacterAttributes()
//setParagraphAttributes()
//
//JTextArea
//insert()
//append()
//replaceRange()
//
//JTextComponent
//replaceSelection()
//setText()
//print()
//getPrintable()
//
//UndoManager
//All methods.
//
//DefaultStyledDocument
//insert()
//setLogicalStyle()
//setCharacterAttributes()
//setParagraphAttributes()
//
//StyleContext
//addAttribute()
//addAttributes()
//removeAttribute()
//removeAttributes()
//reclaim()
//
//AbstractDocument
//render()
//remove()
//insertString()
//createPosition()
//
//PlainDocument
//insertString()
//
//HTMLDocument
//setParagraphAttributes()


/**
 * 
 * Controleert of swing-methoden wel vanaf de event thread opgeroepen worden.
 * Een aantal thread-safe methode wordt overgeslagen.
 * Gebaseerd op {@linkplain http://weblogs.java.net/blog/alexfromsun/archive/2005/11/debugging_swing_1.html}.
 * 
 * @author bnootaer
 *
 */
public aspect EdtRuleChecker {

    private static final Logger logger = Logger.getLogger(EdtRuleChecker.class);
    
    public pointcut anySwingMethods(JComponent c):
         target(c) && call(* *(..));

    public pointcut threadSafeMethods():
        call(* is*()) || 
        call(* get*()) || 
        call(* setText(String)) || 
        call(* repaint(..)) || 
         call(* revalidate()) ||
         call(* invalidate()) ||
         call(* getListeners(..)) ||
         call(* add*Listener(..)) ||
         call(* remove*Listener(..));

    //calls of any JComponent method, including subclasses
    before(JComponent c): anySwingMethods(c) && 
                          !threadSafeMethods() &&
                          !within(EdtRuleChecker) {
     if(!SwingUtilities.isEventDispatchThread()) 
     {
         String signature = null;
         try {
             signature = thisJoinPoint.getSignature().toString();
         } catch (Exception e) {
             //negeren
         }
         logger.error("Swing-methode vanuit thread " + Thread.currentThread().getName() + ": " + thisJoinPoint.getSourceLocation() + ", " + signature);
         for (StackTraceElement se : Thread.currentThread().getStackTrace()) {
             logger.error(" - " + se);
         }
      }
    }

    //calls of any JComponent constructor, including subclasses
    before(): call(JComponent+.new(..)) {
      if (!SwingUtilities.isEventDispatchThread()) {
          String signature = null;
          try {
              signature = thisJoinPoint.getSignature().toString();
          } catch (Exception e) {
              //negeren
          }
          logger.error("Swing-methode vanuit thread " + Thread.currentThread().getName() + ": " + thisJoinPoint.getSourceLocation() + ", " + signature);
          for (StackTraceElement se : Thread.currentThread().getStackTrace()) {
              logger.error(" - " + se);
          }
       }
      }

}
