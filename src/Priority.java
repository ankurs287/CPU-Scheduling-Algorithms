import java.util.ArrayList;
import java.util.Collections;

class Priority
{
    static ArrayList<Process> processes = new ArrayList<Process>();

    public static void main(String[] args)
    {
        int numofprocess = 10;
        double meanBurstTime = 0.1;
        double meanArrivalTime = 10;

        for (int i = 0; i < numofprocess; i++)
        {
            processes.add(new Process(meanArrivalTime, meanBurstTime));
        }

        ArrayList<Process> q = new ArrayList<Process>();
        int totalTime = 100;
        int currentTime = 0;

        Collections.sort(processes, Process.ORDER_BY_PRIORITY);

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

            Collections.sort(q, Process.ORDER_BY_PRIORITY);

            if (q.size() != 0)
            {
                q.get(0).remBurstTime -= 1;
                currentTime += 1;

                if (q.get(0).remBurstTime <= 0)
                {
                    q.get(0).waitTime = currentTime - q.get(0).arrivalTime - q.get(0).burstTime;
                    q.get(0).turnAroundTime = q.get(0).burstTime + q.get(0).waitTime;
                    q.get(0).end = true;
                    q.remove(0);
                }
            }
            else currentTime += 1;
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