pragma solidity >=0.4.21 <0.6.0;

contract MergeSort {
    /*
     * Adapted from: https://www.geeksforgeeks.org/merge-sort/
     */
    function sort(uint[] memory arr) public pure returns(uint[] memory) {
        uint n = arr.length;
        
        if (n < 2) {
            return arr;
        } else {
            uint mid = uint(n)/2;
            uint[] memory L = new uint[](mid);
            uint[] memory R = new uint[](n - mid);
            
            for(uint a = 0; a < mid; a++) {
                L[a] = arr[a];
            }
            
            for(uint b = mid; b < n; b++) {
                R[b - mid] = arr[b];
            }
            
            uint i = 0;
            uint j = 0;
            uint k = 0;
            
            while (i < L.length && j < R.length) {
                if (L[i] < R[j]) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }
            
            while (i < L.length) {
                arr[k] = L[i];
                i++;
                k++;
            }
            
            while (j < R.length) {
                arr[k] = R[j] ;
                j++;
                k++;
            }
            
            return arr;
        }
    }
}

