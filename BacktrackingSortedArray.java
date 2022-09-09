

public class BacktrackingSortedArray implements Array<Integer>, Backtrack {
    private Stack stack;
    public int[] arr; // This field is public for grading purposes. By coding conventions and best practice it should be private.
    private int size;
    private int lastIndex;
    private int ZERO = 0;
    private int ONE = 1;

    // Do not change the constructor's signature
    public BacktrackingSortedArray(Stack stack, int size) {
        this.stack = stack;
        arr = new int[size];
        this.size = size;
        lastIndex = 0;
    }
    
    @Override
    public Integer get(int index){
        if (index >= lastIndex | index < 0)
            throw new IllegalArgumentException("index out of bounds.");
        return (arr[index]);
    }

    @Override
    public Integer search(int k) {
        int output = -1;
        boolean found = false;
        int high = lastIndex-1;
        int low = 0;
        while (!found & high >= low){ //binary search for value
            int middle = (high + low)/2;
            if (arr[middle] == k){
                output = middle;
                found = true;
            } else if (k < arr[middle]){
                high = middle -1;
            } else {
                low = middle + 1;
            }
        }
        return output;
    }

    @Override
    public void insert(Integer x) {
        if (lastIndex == size) //no more space available in array
            throw new IllegalArgumentException("underlaying data structure overflow");
        if (lastIndex == 0){ //array is currently empty, no comparisons needed
            stack.push(lastIndex);//store insert index
            arr[lastIndex] = x;
            lastIndex++;
        } else {
            int low = 0;
            int high = lastIndex-1;
            while (high > low){ //finds suspected index to input x
                int middle = (high+low)/2;
                if (arr[middle] < x)
                    low = middle +1;
                else
                    high = middle-1;
            }
            if (arr[low] < x){ //corrects index +1 if needed
                low++;
            }
            for (int i = lastIndex; i>low; i--){ //clear index, shift array right
                arr[i] = arr[i-1];
            }
            arr[low] = x; //input value to index
            stack.push(low);//store insert operation index in stack.
            lastIndex++;
        }
        stack.push(ONE); //sign of insert operation
    }

    @Override
    public void delete(Integer index) {
        if (index < 0 | index >= lastIndex)
            throw new IllegalArgumentException("index out of bounds");
        stack.push(arr[index]); //save delete operation to stack
        stack.push(index);
        stack.push(ZERO); //sign of delete operation
        for (int i = index; i < lastIndex-1; i++){ //shift array left over index.
            arr[i] = arr[i+1];
        }
        lastIndex--;
        arr[lastIndex] = 0;
    }

    @Override
    public Integer minimum() {
        if (lastIndex == 0)
            throw new IllegalArgumentException("no elements in dynamic set");
        return arr[0];
    }

    @Override
    public Integer maximum() {
        if (lastIndex == 0)
            throw new IllegalArgumentException("no elements in dynamic set");
        return arr[lastIndex-1];
    }

    @Override
    public Integer successor(Integer index) {
        if (lastIndex == 0)
            throw new IllegalArgumentException("can not return succesor for empty set");
        if (index < 0 | index>=lastIndex)
            throw new IllegalArgumentException("index out of bounds");
        if (index == lastIndex-1)
            throw new IllegalArgumentException("no successor for highest number in set");
        return arr[index+1];
    }

    @Override
    public Integer predecessor(Integer index) {
        if (lastIndex == 0)
            throw new IllegalArgumentException("can not return predecessor for empty set");
        if (index < 0 | index >= lastIndex)
            throw new IllegalArgumentException("index out of bounds");
        if (index == 0)
            throw new IllegalArgumentException("no predecessor for lowest number in set");
        return arr[index-1];
    }

    @Override
    public void backtrack() {
        if (!stack.isEmpty()){ //determine of insert backtrack or delete backtrack
            if ((int)stack.pop() == 1){  //backtrack insert
                int index = (int)stack.pop(); //store last index entered
                for (int i = index; i < lastIndex-1; i++){ //shift array left
                    arr[i] = arr[i+1];
                }
                lastIndex--;
                arr[lastIndex] = 0;
            } else { //backtrack delete
                int index = (int)stack.pop();
                int value = (int)stack.pop();
                for (int i = lastIndex; i>index; i--){ //shift array right to make room to re-enter deleted value
                    arr[i] = arr[i-1];
                }
                arr[index] = value;
                lastIndex++;
            }
        }
    }

    @Override
    public void retrack() {
		/////////////////////////////////////
		// Do not implement anything here! //
		/////////////////////////////////////
    }

    @Override
    public void print() {
        String output = "";
        for (int i = 0; i < lastIndex; i++){
            output = output + arr[i] + " ";
        }
        if (output.length() > 0){
            output = output.substring(0, output.length()-1);
        }
        System.out.println(output);
    }
}
