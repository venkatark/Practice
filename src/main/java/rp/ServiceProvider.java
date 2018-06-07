package rp;

import java.util.Random;

public class ServiceProvider {
    final private String name;
    private Integer successRate;
    private Integer totalCount = 0;
    private Integer successfullySend = 0;

    public ServiceProvider(String name, Integer sr)
    {
        this.name = name;
        successRate = sr;
    }

    boolean publishMessage(String msg)
    {
        totalCount++;
        Random random = new Random();
        boolean result = random.nextInt(100) < successRate;
        if (result)
            successfullySend++;
        return result;
    }

    public String getName()
    {
        return name;
    }

    public void setSuccessRate(Integer newRate)
    {
        successRate = newRate;
        System.out.println("SR changed from " + successRate + " to : " + newRate);
    }

    public Integer getCurrentRate()
    {
        return (successfullySend/totalCount)*100;
    }
}
