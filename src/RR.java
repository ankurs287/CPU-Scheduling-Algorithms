import java.util.ArrayList;
import java.util.Collections;

class RR
{
    static ArrayList<Process> processes = new ArrayList<Process>();

    public static void main(String[] args)
    {
        int numofprocess = 10;
        double meanBurstTime = 0.1;
        double meanArrivalTime = 10;
        int quantum = 2;

        for (int i = 0; i < numofprocess; i++)
        {
            processes.add(new Process(meanArrivalTime, meanBurstTime));
        }

        //RR
        ArrayList<Process> q = new ArrayList<Process>();
        int totalTime = 100;
        int currentTime = 0;

        Collections.sort(processes, Process.ORDER_BY_ARRIVALTIME);

        while (currentTime < totalTime)
        {
            for (int i = 0; i < numofprocess; i++)
            {
                if (!processes.get(i).end && !processes.get(i).inQ && processes.get(i).arrivalTime <= currentTime)
                {
                    q.add(processes.get(i));
                    processes.get(i).inQ = true;
                }
            }

            if (q.size() == 0) currentTime++;
            for (int i = 0; i < q.size(); i++)
            {
                if (q.get(i).remBurstTime > 0)
                {
                    if (q.get(i).remBurstTime > quantum)
                    {
                        currentTime += quantum;
                        if (currentTime > totalTime) break;
                        q.get(i).remBurstTime -= quantum;
                    }
                    else
                    {
                        currentTime += q.get(i).remBurstTime;
                        if (currentTime > totalTime) break;
                        q.get(i).waitTime = currentTime - q.get(i).burstTime - q.get(i).arrivalTime;
                        q.get(i).turnAroundTime = q.get(i).burstTime + q.get(i).waitTime;
                        q.get(i).remBurstTime = 0;
                        q.get(i).end = true;
                        q.remove(i);
                        i--;
                    }

                    for (int j = 0; j < numofprocess; j++)
                    {
                        if (!processes.get(j).end && !processes.get(j).inQ && processes.get(j).arrivalTime <= currentTime)
                        {
                            q.add(processes.get(j));
                            processes.get(j).inQ = true;
                        }
                    }
                }
                else currentTime++;
            }
        }

        double avgTAT = 0, avgWT = 0, completedProcess=0;
        for (Process p : processes)
        {
            if (p.end)
            {
                avgTAT += p.turnAroundTime;
                avgWT += p.waitTime;
                completedProcess++;
            }
        }
        avgTAT /= numofprocess;
        avgWT /= numofprocess;
        System.out.println("Process Completed: "+completedProcess);
        System.out.println("Average Turn Around Time: " + avgTAT + "\n" + "Average Wait Time: " + avgWT);
    }
}