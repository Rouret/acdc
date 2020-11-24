pragma solidity >=0.4.21 <0.6.0;

contract Fibonacci {

    function run(uint number) public returns(uint result) {
        if (number == 0) return 0;
        else if (number == 1) return 1;
        else return Fibonacci.run(number - 1) + Fibonacci.run(number - 2);
    }
}
