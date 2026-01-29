/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package oopwj.AdministrativeStaff;

/**
 *
 * @author kwany
 */
import java.awt.Color;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class PlaceHolder {
    private static final char BULLET = '\u2022';

    public static void apply(JTextComponent field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        if (field instanceof JPasswordField pwd) {
            pwd.setEchoChar((char) 0);
        }

        field.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);

                    if (field instanceof JPasswordField pwd) {
                        pwd.setEchoChar(BULLET);
                    }
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);

                    if (field instanceof JPasswordField pwd) {
                        pwd.setEchoChar((char) 0);
                    }
                }
            }
        });
    }
}
