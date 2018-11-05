/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package run;

import control.Hour;
import control.Minute;
import control.Reset;
import control.Second;
import control.ShareData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.View;

/**
 *
 * @author Admin
 */
public class Test {

    private boolean state = true;
    private ShareData shareData = null;
    private Thread threadSecond;
    private Thread threadMinute;
    private Thread threadHour;
    private Thread threadReset;

    public Test() {
        View view = new View();
        shareData = new ShareData(true, false, false, view);
        threadSecond = new Thread(new Second(shareData));
        threadMinute = new Thread(new Minute(shareData));
        threadHour = new Thread(new Hour(shareData));
        threadReset = new Thread(new Reset(shareData));
        view.getBtnFlag().setEnabled(false);
        view.getBtnReset().setEnabled(false);
        view.getBtnStop().setEnabled(false);
        view.getBtnStart().setEnabled(true);

        view.addStartListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                view.getBtnFlag().setEnabled(true);
                view.getBtnReset().setEnabled(false);
                view.getBtnStop().setEnabled(true);
                view.getBtnStart().setEnabled(false);
                try {
                    threadReset.start();
                    threadSecond.start();
                    threadMinute.start();
                    threadHour.start();
                } catch (Exception e) {
                    threadSecond.resume();
                    threadMinute.resume();
                    threadHour.resume();
                }
            }
        });
        view.addStopListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                view.getBtnFlag().setEnabled(true);
                view.getBtnReset().setEnabled(true);
                view.getBtnStop().setEnabled(false);
                view.getBtnStart().setEnabled(true);
                try {
                    threadHour.suspend();
                    threadMinute.suspend();
                    threadSecond.suspend();
                } catch (Exception ex) {
                    Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public static void main(String[] args) {
        new Test();

    }
}
