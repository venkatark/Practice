package rp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationService {

    List<ServiceProvider> serviceProviderList = new ArrayList();

    // historical data
    HashMap<ServiceProvider, Integer> successRateMap = new HashMap<>();


    public NotificationService()
    {
        System.out.print("Starting Notification Service");
    }

    public boolean addServiceProvider(ServiceProvider sp)
    {
        if (serviceProviderList.contains(sp))
            return true;
        serviceProviderList.add(sp);
        return true;
    }

    public boolean removeServiceProvider(ServiceProvider sp)
    {
        if (serviceProviderList.contains(sp))
        {
            return serviceProviderList.remove(sp);
        }
        return false;
    }

    public String sendNotification(String msg)
    {
        int hopCount = 1;
        boolean result = false;
        for (ServiceProvider sp : serviceProviderList)
        {
            result = sp.publishMessage(msg);
            successRateMap.put(sp,sp.getCurrentRate());
            if (result)
            {
                return "Succcess fully sent notification: \n" + msg + "  with " + hopCount + " hop(s)  via " + sp.getName();
            }
            else {

                System.out.println("Sending via another provider");
                hopCount++;
            }
        }

        return "Failure: Unable to send the notification even after " + hopCount + " tries";
    }
}
