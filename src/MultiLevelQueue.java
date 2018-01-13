import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class MultiLevelQueue
{
    static ArrayList<Process> processes = new ArrayList<Process>();

    public static void main(String[] args)
    {
        int numofprocess = 10;
        double meanBurstTime = 0.1;
        double meanArrivalTime = 10;
        int quantum = 4;

        for (int i = 0; i < numofprocess; i++)
        {
            processes.add(new Process(meanArrivalTime, meanBurstTime));
            processes.get(i).priority = new Random().nextInt(3) + 1;
        }

        ArrayList<Process> systemQ = new ArrayList<Process>(); // Highest Priority 1 RR
        ArrayList<Process> userQ = new ArrayList<Process>();   // Medium Priority 2  RR
        ArrayList<Process> backgroundQ = new ArrayList<Process>();  // Lowest Priority 3 SJF

        int totalTime = 100;
        int currentTime = 0;

        Collections.sort(processes, Process.ORDER_BY_ARRIVALTIME);

        while (currentTime < totalTime)
        {
            for (int i = 0; i < numofprocess; i++)
            {
                if (!processes.get(i).end && !processes.get(i).inQ && processes.get(i).arrivalTime <= currentTime)
                {
                    if (processes.get(i).priority == 1)
                    {
                        systemQ.add(processes.get(i));
                        processes.get(i).inQ = true;
                    }
                    else if (processes.get(i).priority == 2)
                    {
                        userQ.add(processes.get(i));
                        processes.get(i).inQ = true;
                    }
                    else
                    {
                        backgroundQ.add(processes.get(i));
                        processes.get(i).inQ = true;
                    }
                }
            }

            if (systemQ.size() != 0)
            {
                for (int i = 0; i < systemQ.size(); i++)
                {
                    if (systemQ.get(i).remBurstTime > 0)
                    {
                        if (systemQ.get(i).remBurstTime > quantum)
                        {
                            currentTime += quantum;
                            if (currentTime > totalTime) break;
                            systemQ.get(i).remBurstTime -= quantum;
                        }
                        else
                        {
                            currentTime += systemQ.get(i).remBurstTime;
                            if (currentTime > totalTime) break;
                            systemQ.get(i).waitTime = currentTime - systemQ.get(i).burstTime - systemQ.get(i).arrivalTime;
                            systemQ.get(i).turnAroundTime = systemQ.get(i).burstTime + systemQ.get(i).waitTime;
                            systemQ.get(i).remBurstTime = 0;
                            systemQ.get(i).end = true;
                            systemQ.remove(i);
                            i--;
                        }

                        for (int j = 0; j < numofprocess; j++)
                        {
                            if (!processes.get(j).end && !processes.get(j).inQ && processes.get(j).arrivalTime <= currentTime)
                            {
                                if (processes.get(j).priority == 1)
                                {
                                    systemQ.add(processes.get(j));
                                    processes.get(j).inQ = true;
                                }
                            }
                        }
                    }
                }
            }
            else if (userQ.size() != 0)
            {
                for (int i = 0; i < userQ.size(); i++)
                {
                    if (userQ.get(i).remBurstTime > 0)
                    {
                        if (userQ.get(i).remBurstTime > quantum)
                        {
                            currentTime += quantum;
                            if (currentTime > totalTime) break;
                            userQ.get(i).remBurstTime -= quantum;
                        }
                        else
                        {
                            currentTime += userQ.get(i).remBurstTime;
                            if (currentTime > totalTime) break;
                            userQ.get(i).waitTime = currentTime - userQ.get(i).burstTime - userQ.get(i).arrivalTime;
                            userQ.get(i).turnAroundTime = userQ.get(i).burstTime + userQ.get(i).waitTime;
                            userQ.get(i).remBurstTime = 0;
                            userQ.get(i).end = true;
                            userQ.remove(i);
                            i--;
                        }

                        for (int j = 0; j < numofprocess; j++)
                        {
                            if (!processes.get(j).end && !processes.get(j).inQ && processes.get(j).arrivalTime <= currentTime)
                            {
                                if (processes.get(j).priority == 2)
                                {
                                    userQ.add(processes.get(j));
                                    processes.get(j).inQ = true;
                                }
                            }
                        }
                    }
                }
            }
            else if (backgroundQ.size() != 0)
            {
                Collections.sort(processes, Process.ORDER_BY_BURSTTIME);

                backgroundQ.get(0).waitTime = currentTime - backgroundQ.get(0).arrivalTime;
                currentTime += backgroundQ.get(0).burstTime;
                if (currentTime > totalTime) break;
                backgroundQ.get(0).turnAroundTime = backgroundQ.get(0).burstTime + backgroundQ.get(0).waitTime;
                backgroundQ.get(0).end = true;
                backgroundQ.remove(0);
            }

            else currentTime++;
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