import java.util.Comparator;
import java.util.Random;
//Arrival time for the process will be generated on the basis of poisson​ ​distribution​.
//Burst time for the process will be generated on the basis of exponential​ ​distribution.

class Process
{
    double arrivalTime;
    double waitTime;
    double burstTime;
    double turnAroundTime;
    double remBurstTime; //remaining burst time
    int priority;
    boolean end;
    boolean inQ;

    public Process(double meanArrivalTime, double meanBurstTime)
    {
        arrivalTime = getPoisson(meanArrivalTime);
        burstTime = (int) getExponential(meanBurstTime);
        remBurstTime = burstTime;
        priority = new Random().nextInt(10) + 1;
        end = false;
        inQ = false;
    }

    public static int getPoisson(double lambda)
    {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do
        {
            k++;
            p *= Math.random();
        } while (p > L);

        return k - 1;
    }

    public static double getExponential(double lambda)
    {
        Random rand = new Random();
        return Math.log(1 - rand.nextDouble()) / (-lambda);
    }

    static final Comparator<Process> ORDER_BY_ARRIVALTIME = new Comparator<Process>()
    {
        public int compare(Process a1, Process a2)
        {
            double c = a1.arrivalTime - a2.arrivalTime;
            if (c > 0) return 1;
            else if (c < 0) return -1;
            return 0;
        }
    };

    static final Comparator<Process> ORDER_BY_BURSTTIME = new Comparator<Process>()
    {
        public int compare(Process a1, Process a2)
        {
            double c = a1.burstTime - a2.burstTime;
            if (c > 0) return 1;
            else if (c < 0) return -1;

            c = ORDER_BY_ARRIVALTIME.compare(a1, a2);
            if (c > 0) return 1;
            else if (c < 0) return -1;
            return 0;
        }
    };

    static final Comparator<Process> ORDER_BY_PRIORITY = new Comparator<Process>()
    {
        public int compare(Process a1, Process a2)
        {
            double c = a1.priority - a2.priority;
            if (c > 0) return 1;
            else if (c < 0) return -1;

            c = ORDER_BY_ARRIVALTIME.compare(a1, a2);
            if (c > 0) return 1;
            else if (c < 0) return -1;
            return 0;
        }
    };

    @Override
    public String toString()
    {
        return "Process{" +
                "arrivalTime=" + arrivalTime +
                ", waitTime=" + waitTime +
                ", burstTime=" + burstTime +
                ", turnAroundTime=" + turnAroundTime +
                ", remBurstTime=" + remBurstTime +
                ", priority=" + priority +
                ", end=" + end +
                '}';
    }
}