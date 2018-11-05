package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.View;

/**
 * biến s : giây; biến m : phút; biến h : giờ; biến bools : kiểm tra luồng giây
 * có được phép thực hiện; biến boolm : kiểm tra luồng phút có được phép thực
 * hiện; biến boolh : kiểm tra luồng giờ có được phép thực hiện;
 *
 */
/**
 * khởi tạo lúc đầu : s = 0; m = 0 ; h = 0; bools = true; luồng giây được phép
 * chạy boolm = false; luồng phút không được phép chạy boolh = false; luồng giờ
 * không được phép chạy
 *
 */
public class ShareData  {
    private int countTime = 1;
    private int countShowTextArea = 1;
    
    private View view = null;
    private int s =55;
    private int m = 59;
    private int h = 23;
    private boolean bools, boolm, boolh;

    public ShareData(boolean s, boolean m, boolean h,View view) {
        this.view = view;
        view.setVisible(true);
        view.getLblHour().setText(Integer.toString(this.h));
        view.getLblMinute().setText(Integer.toString(this.m));
        view.getLblSecond().setText(Integer.toString(this.s));
        this.bools = s;
        this.boolm = m;
        this.boolh = h;
        flag();
        //reset();
    }

    
     
    

    private class ResetListener implements ActionListener {

        @Override
        public  void actionPerformed(ActionEvent ae) {
            h = 0;
            m = 0;
            s = 0;
            
            view.getLblHour().setText(Integer.toString(h));
            view.getLblMinute().setText(Integer.toString(m));
            view.getLblSecond().setText(Integer.toString(s));
            view.getTxtAreaShow().setText("");
            countShowTextArea = 1;
        }

    }
    private class FlagListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {
            String time = "";
            time += view.getLblHour().getText();
            time += " : ";
            time += view.getLblMinute().getText();
            time += " : ";
            time += view.getLblSecond().getText();
            view.getTxtAreaShow().append(countShowTextArea + " : " + time + "\n");
            countShowTextArea ++;
        }
        
    }
    public synchronized void flag(){
        view.addFlagListener(new  FlagListener());
        notifyAll();
    }

    public synchronized void reset() {
        view.addResetListener(new ResetListener());
        countTime = 1;
        notifyAll();
    }

    // phương thức để chạy luồng giây
    public synchronized void second() {
        while (true) {
            // nếu bools = false : luồng giây không được phép chạy,
            if (!bools) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ShareData.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                    countTime = 1;
                    while(countTime <= 1000){
                        Thread.sleep(1);
                        countTime = countTime + 1;
                    }
                    countTime = 1;
                    s = s + 1;
                    if (s <= 59) {
                        bools = true;
                        boolm = false;
                        boolh = false;

                    } else {
                        s = 0;
                        bools = false;
                        boolm = true;
                        boolh = false;
                    }

                    view.getLblSecond().setText(Integer.toString(s));
                    notifyAll();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ShareData.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
// phương thức để chạy luồng phút

    public synchronized void minute() {
        while (true) {
            // nếu boolm = false : luồng phút không được phép chạy
            if (!boolm) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ShareData.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                m = m + 1;
                if (m <= 59) {
                    boolm = false;
                    bools = true;
                    boolh = false;
                } else {
                    m = 0;
                    boolm = false;
                    bools = false;
                    boolh = true;
                }
                view.getLblMinute().setText(Integer.toString(m));
                notifyAll();
            }

        }
    }
//phương thức để chạy luồng giờ

    public synchronized void hour() {
        while (true) {
            // boolh = true : luồng giờ không được phép chạy
            if (!boolh) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ShareData.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                h = h + 1;
                if (h == 24) {
                    h = 0;
                    m = 0;
                    s = 0;
                }
                boolh = false;
                boolm = false;
                bools = true;
                view.getLblHour().setText(Integer.toString(h));
                notifyAll();
            }

        }
    }
}
