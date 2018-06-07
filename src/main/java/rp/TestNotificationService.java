package rp;

import java.util.Scanner;

public class TestNotificationService {

    public static void main(String [] args)
    {
        NotificationService ns = new NotificationService();
        ns.addServiceProvider(new ServiceProvider("P1", 80));
        ns.addServiceProvider(new ServiceProvider("P2", 50));
        ns.addServiceProvider(new ServiceProvider("P3", 70));
        ns.addServiceProvider(new ServiceProvider("P4", 90));

        boolean exitNow = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please Enter Msg to send: ");
        while (!exitNow) {

            String msg = scanner.next();

            if (msg.equals("EXITNOW"))
            {
                exitNow = true;
                System.out.println("Existing the Notification Service");
                break;
            }

            String result = ns.sendNotification(msg);
            System.out.println(result);
        }
    }
}
