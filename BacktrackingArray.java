

public class BacktrackingArray implements Array<Integer>, Backtrack {
    private Stack stack;
    private int[] arr;
    private int size;
    private int lastIndex;
    private int ZERO = 0;
    private int ONE = 1;

    // Do not change the constructor's signature
    public BacktrackingArray(Stack stack, int size) {
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
        for (int i = 0; i < lastIndex & !found; i++){ //regular array search
           if (arr[i] == k){
               output = i;
               found = true;
           }
        }
    	return output;
    }

    @Override
    public void insert(Integer x) {
        if (lastIndex == size)
            throw new IllegalArgumentException("underlying data structure overflow");
        arr[lastIndex] = x;
        lastIndex++;
        stack.push(ONE); //signing for insertion operation.
    }

    @Override
    public void delete(Integer index) {
        if(index < 0 | index >= lastIndex)
            throw new IllegalArgumentException("entered index out of set bounds");
        stack.push(index); //will use to backtrack deleted index position
        stack.push(arr[index]); //will use to backtrack deleted index value
        stack.push(ZERO); //signing for delete operation
        for (int i = index; i < lastIndex-1; i++){ //shift array left over index.
            arr[i] = arr[i+1];
        }
        lastIndex--;
        arr[lastIndex] = 0;
    }

    @Override
    public Integer minimum() {
        if (lastIndex == 0)
            throw new IllegalArgumentException("can not return minimum for an empty set");
        int output = arr[0];
        for (int i = 1; i < lastIndex; i++){
            if (arr[i] < output)
                output = arr[i];
        }
        return output;
    }

    @Override
    public Integer maximum() {
        if (lastIndex == 0)
            throw new IllegalArgumentException("can not return maximum for an empty set");
        int output = arr[0];
        for (int i = 1; i < lastIndex; i++){
            if (arr[i] > output)
                output = arr[i];
        }
    	return output;
    }

    @Override
    public Integer successor(Integer index) {
        if (index < 0 | index >= lastIndex)
            throw new IllegalArgumentException("index out of bounds");
        int output = arr[index];
        boolean flag = false;
        for (int i = 0; i < lastIndex; i++){
            if (!flag & arr[i] > output){ //find first value that is larger than input index value.
                output=arr[i];
                flag = true;
            } else if (flag & arr[i] < output & arr[i] > arr[index]) { //compare values to determine successor.
                    output = arr[i];
            }
        }
        if (output == arr[index]){
            throw new IllegalArgumentException("successor does not exist");
        }
    	return output;
    }

    @Override
    public Integer predecessor(Integer index) {
        if (index < 0 | index >= lastIndex)
            throw new IllegalArgumentException("index out of bounds");
        int output = arr[index];
        boolean flag = false;
        for (int i = 0; i < lastIndex; i++){
            if (!flag & arr[i] < output){ //find first value that is smaller than input index value.
                output=arr[i];
                flag = true;
            } else if (flag & arr[i] > output & arr[i] < arr[index]) { //compare values to determine predecessor.
                    output = arr[i];
            }
        }
        if (output == arr[index]){
            throw new IllegalArgumentException("successor does not exist");
        }
        return output;
    }

    @Override
    public void backtrack() {
        if (!stack.isEmpty()){ //determine if needs to backtrack delete or insert operaton
            if((int)stack.pop() == 1) {     //backtrack insertion
                lastIndex--; //delete the last added value to the array
                arr[lastIndex] = 0;
            } else {    //backtrack delete
                int val = (int)stack.pop(); //find deleted val
                int index = (int)stack.pop(); //find deleted index
                for (int i = lastIndex; i>index; i--){
                    arr[i] = arr[i-1];
                }
                arr[index] = val;
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
