//

// Time Complexity and explanation: You can use the following variables for easier reference.
// n denotes the number of requests, p denotes the size of the cache
// n and p can be different and there is no assumption which one is larger

// noEvict():time complexity is O(n*p)
/*this is because the inner loop,loops a total of cSize times which is inside an outer loop that loops a total of rSize times therfore
overall complexity is when both are multiplied.*/
// evictFIFO():time complexity is O(n*p)
/*this is because the inner loop repeats a total of cSize times and this is inside an outer loop whic repeats a total of rSize times.
total complexity is calculated by multiplying rSize and cSize.*/
// evictLFU():time complexity is O(np)^2
/*this is because there are two nested loops.the outermost loop runs a total of n times while the inner loop runs a total of p times but in the outer loop
there is an if else block.in the worst case for that algorithm the if block is executed and this has a while loop in it which runs a total of np times.*/
// evictLFD():time complexity is O(np)^3
/*this is because there is an outer for loop which runs a total of n times then and an inner for loop whic runs a total of p times.in this outer for loop
an external method updatepositions is called which has a time complexity of 'np' and after this method is called there is a while loop which runs a total of p
times but as it is in the outer for loop which runs n times the while loop has a time complexity 'np' also.multiply everything and thats how i got O(np)^3*/

class COMP108A1Paging {
	// no eviction
	// Aim:
	// do not evict any page
	// count number of hit and number of miss, and find the hit-miss pattern; return an object COMP108A1Output
	// Input:
	// cArray is an array containing the cache with cSize entries
	// rArray is an array containing the requeset sequence with rSize entries
	static COMP108A1Output noEvict(int[] cArray, int cSize, int[] rArray, int rSize) {
		COMP108A1Output output = new COMP108A1Output();
		int i=0;
		int j=0;
		int key;
		boolean found=false;
/*the outer for loop loops through the values in the request array and maps them to a variable called key.
the inner for loop,loops through the cache and compares the key with every value in the cache.
if the key equals a cache value the boolean variable found is
set to true.*/
		for(i=0;i<=rSize-1;i++){
			found=false;
			key=rArray[i];
			for(j=0;j<=cSize-1;j++){
				if(key==cArray[j]){
					found=true;
					break;
				}

				else{
					found=false;
					continue;
					}
				}
				if(found==true){//if boolean found variable is true,the if block code is executed
					output.hitCount++;
					output.hitPattern+="h";
					}
				else{//if boolean found variable is false,the else block code is executed
					output.missCount++;
					output.hitPattern+="m";
					}
			}

		return output;
	}

	// evict FIFO
	// Aim:
	// evict the number present in cache for longest time if next request is not in cache
	// count number of hit and number of miss, and find the hit-miss pattern; return an object COMP108A1Output
	// Input:
	// cArray is an array containing the cache with cSize entries
	// rArray is an array containing the requeset sequence with rSize entries
	static COMP108A1Output evictFIFO(int[] cArray, int cSize, int[] rArray, int rSize) {
		COMP108A1Output output = new COMP108A1Output();
		int i=0;
		int j=0;
		int key;
		int head=0;
		boolean found=false;
//the outer for loop loops through the values in the request array and maps them to a variable called key.
//the inner for loop,loops through the cache and compares the key with every value in the cache.
		for(i=0;i<=rSize-1;i++){
			found=false;
			key=rArray[i];
			for(j=0;j<=cSize-1;j++){
				if(key==cArray[j]){
					found=true;
					break;
				}
				else{
					found=false;
					continue;
				}
			}
			if(found==true){//if boolean found variable is true,the if block code is executed
				output.hitCount++;
				output.hitPattern+="h";
			}
			else{//if boolean found variable is false,the else block code is executed.
				cArray[head]=key;
				head=(head+1)%cSize;//this ensures a circular motion,so that that head does not increase past the sixe of cArray.
				output.missCount++;
				output.hitPattern+="m";
			}
		}

		return output;
	}

	// evict LFU
	// Aim:
	// evict the number that is least freently used so far if next request is not in cache
	// count number of hit and number of miss, and find the hit-miss pattern; return an object COMP108A1Output
	// Input:
	// cArray is an array containing the cache with cSize entries
	// rArray is an array containing the requeset sequence with rSize entries
/*This method creates an array of size cSize and set all the values in the array to the default count value 1.
the indexes in this array match the indexes of the values in the cache array.
e.g if 20 is in the cache array and has index 0 then its count value is stored at that index.
at index 0 also.*/
	public static int[] rArrayCount(int rSize,int[] rArray,int[] cArray,int cSize){
		int[] rCount=new int[cSize];
		int j=0;
		for(j=0;j<=cSize-1;j++){
			rCount[j]++;
				}
			return rCount;
			}
/*the outer for loop loops through the values in the request array and maps them to a variablr called key.
the inner for loop,loops through the cache and compares the key with every value in the cache.
if the key equals a cache value the boolean variable found is set to true.
when found is true the hit count is increased and h is added to the hit pattern but when found is false a while loop searches for the cache value which
has the least amount of requests,the position of this value in the cache array is stored in a variable pos.
this variable is then used to reset the count at that position back to the default value 1 and then the key replaces the value in the cache array
with the least amount of requests using the varible pos.*/
	static COMP108A1Output evictLFU(int[] cArray, int cSize, int[] rArray, int rSize) {
		COMP108A1Output output = new COMP108A1Output();
		int[] rCount=rArrayCount(rSize,rArray,cArray,cSize);//array containing number of times each value in the cache array is requested.
		int rCountLength=rCount.length;
		int i=0;
		int j=0;
		int pos=0;
		int key;
		boolean found=false;
		for(i=0;i<=rSize-1;i++){
			found=false;
			key=rArray[i];
			for(j=0;j<=cSize-1;j++){
				if(key==cArray[j]){
					found=true;
					rCount[j]++;
					break;
				}
				else{
					found=false;
				}
			}
			if(found==false){
				int k=1;
				int min=rCount[0];
				while(k<cSize){
					if(rCount[k]<min){
						min=rCount[k];
						pos=k;//stores position of value in cArray array with least amount of requests.
					}
					k++;
				}
				rCount[pos]=1;//resets count value.
				cArray[pos]=key;
				output.missCount++;
				output.hitPattern+="m";
		    }
		    else{
		        output.hitCount++;
		        output.hitPattern+="h";
		    }
		}

		return output;
	}

	// evict LFD
	// Aim:
	// evict the number whose next request is the latest
	// count number of hit and number of miss, and find the hit-miss pattern; return an object COMP108A1Output
	// Input:
	// cArray is an array containing the cache with cSize entries
	// rArray is an array containing the requeset sequence with rSize entries
//The method belows creates an array to store the initial request positions of the values in the cache array.this array is later used in
//the update positions method.
	public static int[] firstFuturePosition(int[] cArray, int cSize, int[] rArray, int rSize){
		int[] rFutureRequests=new int[cSize];
		int i;
		int j;
		int key;
		int pos=0;
		boolean found=false;
		for(i=0;i<=cSize-1;i++){
			key=cArray[i];
			found=false;
			for(j=0;j<=rSize-1;j++){
				if(key==rArray[j]){
					pos=j+1;
					found=true;
					break;
				}
				else{
					continue;
				}
			}
		if(found==true){//if found is true the position of this value in the rArray is stored in the futureRequest array.
			rFutureRequests[i]=pos;
		}
		else{//stores the value below in the array if found is false.
			rFutureRequests[i]=10000000;//very large value used to imitate infinity.
		}
		}
		return rFutureRequests;
	}

//the method below updates the future request positions of the values in the cache array.
	public static int[] updatePositions(int cSize,int[] cArray,int rSize,int[] rArray,int currentPositionRequestArray){
		int[] futureRequests=firstFuturePosition(cArray,cSize,rArray,rSize);
		int i,j;
		int key;
		int l=currentPositionRequestArray;
		int pos=0;
		boolean found=false;
   		for(i=0;i<=cSize-1;i++){
   			key=cArray[i];
   			found=false;
   			for(j=l;j<=rSize-1;j++){//the iteration starts at the value l(this value is x) so that all values that have been requested are discarded during the iteration.
   				if(key==rArray[j]){
   					found=true;
   					pos=j;
   					break;
   				}
   				else{
   					found=false;
   					continue;
   				}
   			}
   			if(found==true){
   				futureRequests[i]=pos+1;//increases the pos value by one to get the correct position of the future request.
   			}
   			else{
   				futureRequests[i]=100000000;
   			}

   		}
   		return futureRequests;
   }

/*the outer loop in this algorithm loops through the request array and maps them to a variable key.
the inner loop then compares this key with evry value in the cache array
if the found variable is true at the end of the iteration,the variable x storing the position in the request array is increassed
hit count is increased,a h is added to the hit pattern
updatepositions method is called to update the future request position of the key in the array.
on the other if found is false,the update future requests method is called,then a while loop loops through this array to find the largest value
when largest value is found the position is stored in a variable pos.
this variable is then used to evict the cache value corresponding to the largest future request value and replace with the key.
then finally the miss count is increased and an m added to the miss pattern.*/
	static COMP108A1Output evictLFD(int[] cArray, int cSize, int[] rArray, int rSize) {
		COMP108A1Output output = new COMP108A1Output();
		//int[] futureRequests=futurePosition(cArray,cSize,rArray,rSize);
		int i;
		int j;
		boolean found=false;
		int key;
		int pos=0;
		int currentPositionRequestArray=1;//this variable stores the current position in the request array.this varaiable is used in the updatepositions method.
		for(i=0;i<=rSize-1;i++){
			found=false;
			key=rArray[i];
			for(j=0;j<=cSize-1;j++){
				if(key==cArray[j]){
					found=true;
					break;
				}
				else{
					found=false;
					continue;
				}
			}
			if(found==false){
				int [] updatedFutureRequests=updatePositions(cSize, cArray, rSize, rArray,currentPositionRequestArray);//calls method to update future requests.
				int k=1;
				int max=updatedFutureRequests[0];
				while(k<cSize){
					if(updatedFutureRequests[k]>max){
					max=updatedFutureRequests[k];
					pos=k;
					}
					k++;
				}
				cArray[pos]=key;
				output.missCount++;
				output.hitPattern+="m";

			}
			else{
				currentPositionRequestArray++;
				int [] updatedFutureRequests=updatePositions( cSize, cArray, rSize, rArray,currentPositionRequestArray);//calls method to update future requests
				output.hitCount++;
				output.hitPattern+="h";
			}
		}
		return output;
	}

}

