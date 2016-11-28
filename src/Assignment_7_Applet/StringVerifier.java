/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment_7_Applet;

import java.awt.Color;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Mark Pendergast
 */
public class StringVerifier extends InputVerifier {
    int minLength = 0;
    int maxLength = 256;
    Color invalidColor = Color.RED;
    Color validColor = Color.WHITE;
    
    public StringVerifier()
    {
        
    }
    public StringVerifier(int min, int max)
    {
      minLength = min;
      maxLength = max;
    }
    
    public StringVerifier(int min, int max, Color invalid, Color valid)
    {
      minLength = min;
      maxLength = max;
      invalidColor = invalid;
      validColor = valid;
    }
    
     public boolean verify(JComponent input) {
             String text = ((JTextComponent)input).getText();
             if(text.length() >= minLength && text.length() <= maxLength)
             {
               input.setBackground(validColor);
               return true;
             }
              else
               {
                 input.setBackground(invalidColor);  
                 return false; 
               }
         }
}
