

public class Warmup {
    public static int backtrackingSearch(int[] arr, int x, int forward, int back, Stack myStack) {
        int output = -1;
        int trackBack = 0;
        boolean found = false;
        for (int i = 0; i<arr.length & !found; i++){
            myStack.push(i);
            trackBack++;
            if (arr[i] == x){
                output = i;
                found = true;
            } else if (trackBack % forward == 0){ //should erase else?
                for (int b = 0; b<back; b++){
                    i = (int)myStack.pop();
                }
                i--;
            }
        }
        return output;
    }

    public static int consistentBinSearch(int[] arr, int x, Stack myStack) {
        int low = 0;
        int high = arr.length-1;
        boolean found = false;
        int output = -1;
        while (!found & high >= low){
            int middle = (high + low)/2;
            myStack.push(high);
            myStack.push(low);
            int temp = arr[middle];
            if (temp == x){
                output = middle;
                found = true;
            } else {
                if (temp < x){
                    low = middle + 1;
                }
                else {
                    high = middle -1;
                }
            }
            int inconsistencies = Consistency.isConsistent(arr);
            while (inconsistencies > 0){ //should add output = -1, found = false?
                inconsistencies--;
                low = (int)myStack.pop();
                high = (int)myStack.pop();
            }
        }
        return output;
    }
    
}
